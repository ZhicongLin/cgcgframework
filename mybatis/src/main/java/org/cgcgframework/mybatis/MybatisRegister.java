package org.cgcgframework.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.cgcgframework.core.ApplicationRegister;
import org.cgcgframework.core.CgcgScanBeanFactory;
import org.cgcgframework.core.CgcgScanner;
import org.cgcgframework.core.RegisterWare;
import org.cgcgframework.core.annotation.CBean;
import org.cgcgframework.core.context.ApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * @author zhicong.lin
 */
@CBean
public class MybatisRegister implements RegisterWare {
    private static final SqlSessionFactory SQL_SESSION_FACTORY;

    static {
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream("mybatis.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SQL_SESSION_FACTORY = new SqlSessionFactoryBuilder().build(inputStream);
    }
    @Override
    public void register(CgcgScanner scanner) {
        for (String pkg : ApplicationRegister.getPgs()) {
            final Set<Class<?>> contexts = scanner.scan(pkg);
            for (Class<?> context : contexts) {
                final Mapper mapper = context.getAnnotation(Mapper.class);
                if (mapper != null) {
                    initBean(context);
                }
            }
        }
    }

    private  void  initBean(Class<?> clazz) {
        final SqlSession sqlSession = SQL_SESSION_FACTORY.openSession(true);
        final Object bean = sqlSession.getMapper(clazz);
        final CgcgScanBeanFactory beanFactory = new CgcgScanBeanFactory();
        beanFactory.setClazz(clazz);
        beanFactory.setPkg(clazz.getPackage().getName());
        beanFactory.setBean(bean);
        ApplicationContext.putBean(beanFactory);
    }

}
