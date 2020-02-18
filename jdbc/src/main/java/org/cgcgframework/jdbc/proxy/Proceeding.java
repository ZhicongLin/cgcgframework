package org.cgcgframework.jdbc.proxy;

import com.alibaba.fastjson.JSON;
import com.google.common.base.CaseFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.cgcgframework.core.context.ApplicationContext;
import org.cgcgframework.jdbc.DataSourceTemplate;
import org.cgcgframework.jdbc.annotation.Sql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.sql.DataSource;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@ToString
public class Proceeding {
    private Method method;
    private Object[] arguments;
    private Class<?> target;
    private Object instance;
    private Logger logger;
    private String logName;

    public Proceeding(Method method, Object[] arguments, Class<?> target) {
        this.method = method;
        this.arguments = arguments;
        this.target = target;
        this.logName = target.getName() + "#" + method.getName();
        this.logger = LoggerFactory.getLogger(this.logName);
    }

    public static Object jdk(Class interfaceClass) {
        final InvocationHandler invocationHandler = (proxy, method, args) -> invoke(new Proceeding(method, args, interfaceClass), method.getReturnType());
        return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, invocationHandler);
    }

    /**
     * 包装调用方法：进行预处理、调用后处理
     */
    public static <T> T invoke(Proceeding proceeding, Class<T> returnType) {
        Object result = null;
        final Method method = proceeding.getMethod();
        final Sql sqlAnnotation = method.getAnnotation(Sql.class);
        final DataSourceTemplate dataSourceTemplate = ApplicationContext.getBean(DataSourceTemplate.class);
        final DataSource dataSource = dataSourceTemplate.getDataSource();
        final String sql = sqlAnnotation.value();
        loggers(proceeding, method, sqlAnnotation);
        try {
            final Connection connection = dataSource.getConnection();
            final PreparedStatement ps = connection.prepareStatement(sql);
            final Object[] arguments = proceeding.getArguments();
            if (arguments != null && arguments.length > 0) {
                for (int i = 0; i < arguments.length; i++) {
                    ps.setObject(i + 1, arguments[i]);
                }
            }
            final boolean execute = ps.execute();
            if (execute) {
                final ResultSet resultSet = ps.getResultSet();
                List objects = new ArrayList<>();
                while (resultSet.next()) {
                    final Type grt = method.getGenericReturnType();
                    if (grt instanceof ParameterizedTypeImpl) {
                        final ParameterizedTypeImpl genericReturnType = (ParameterizedTypeImpl) grt;
                        final Class<?> rawType = genericReturnType.getRawType();
                        if (List.class.isAssignableFrom(rawType)) {
                            toList(resultSet, objects, genericReturnType);
                        }
                    } else {
                        final String typeName = grt.getTypeName();
                        final Class<?> aClass = Class.forName(typeName);
                        Object row = null;
                        if (List.class.isAssignableFrom(aClass)) {
                            Proceeding.resultSetResole(resultSet, objects, Map.class);
                            if (objects.size() > 0) {
                                row = objects;
                            }
                        } else {
                            row = getObject(resultSet, aClass);
                            objects.add(row);
                        }
                        proceeding.getLogger().debug("ResultSize\t<= {}", objects.size());
                        proceeding.getLogger().trace("ResultSet\t<= {}", JSON.toJSONString(row));
                        return (T) row;
                    }
                }
                proceeding.getLogger().debug("ResultSize\t<= {}", objects.size());
                proceeding.getLogger().trace("ResultSet\t<= {}", JSON.toJSONString(objects));
                return (T) objects;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) result;
    }

    private static void loggers(Proceeding proceeding, Method method, Sql sqlAnnotation) {
        final Logger logger = proceeding.getLogger();
        logger.debug("PrepareStatement\t=> {}", sqlAnnotation.value());
        final Object[] arguments = proceeding.getArguments();
        if (arguments != null) {
            logger.debug("Parameters\t=> {}", JSON.toJSON(arguments));
        }
    }

    private static Object getObject(ResultSet resultSet, Class<?> aClass) throws InstantiationException, IllegalAccessException, SQLException {
        final Object row = aClass.newInstance();
        final Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            final String name = field.getName();
            field.setAccessible(true);
            try {
                final Object columnValue = resultSet.getObject(name);
                if (columnValue != null) {
                    field.set(row, columnValue);
                }
            } catch (Exception e) {
                field.set(row, resultSet.getObject(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)));
            }
        }
        return row;
    }

    private static void toList(ResultSet resultSet, List objects, ParameterizedTypeImpl genericReturnType) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        final Type[] actualTypeArguments = genericReturnType.getActualTypeArguments();
        Type actualTypeArgument;
        final Type at1 = actualTypeArguments[0];
        if (at1 instanceof ParameterizedTypeImpl) {
            actualTypeArgument = ((ParameterizedTypeImpl) at1).getRawType();
        } else if ("?".equals(at1.getTypeName())){
            actualTypeArgument = Map.class;
        } else {
            actualTypeArgument = Class.forName(at1.getTypeName());
        }
        final String typeName = actualTypeArgument.getTypeName();
        Class<?> genericComponentType = Class.forName(typeName);
        Proceeding.resultSetResole(resultSet, objects, genericComponentType);
    }

    private static void resultSetResole(ResultSet resultSet, List objects, Class<?> genericComponentType) throws SQLException, InstantiationException, IllegalAccessException {
        if (Map.class.isAssignableFrom(genericComponentType)) {
            Map<String, Object> row = new HashMap<>();
            final ResultSetMetaData metaData = resultSet.getMetaData();
            final int columnCount = metaData.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                final String columnName = metaData.getColumnName(i + 1);
                final Object object = resultSet.getObject(columnName);
                row.put(columnName, object);
            }
            objects.add(row);
        } else {
            final Object row = getObject(resultSet, genericComponentType);
            objects.add(row);
        }
    }
}
