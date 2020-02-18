package org.cgcgframework.mybatis;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public final class SqlSessionUtils {

    public static SqlSession getSqlSession(SqlSessionFactory sessionFactory) {
        ExecutorType executorType = sessionFactory.getConfiguration().getDefaultExecutorType();
        return getSqlSession(sessionFactory, executorType);
    }

    public static SqlSession getSqlSession(SqlSessionFactory sessionFactory, ExecutorType executorType) {
        SqlSession session = null;
        if (session != null) {
            return session;
        } else {
            session = sessionFactory.openSession(executorType);
            return session;
        }
    }

    public static void closeSqlSession(SqlSession session, SqlSessionFactory sessionFactory) {
        session.close();
    }

    public static boolean isSqlSessionTransactional(SqlSession session, SqlSessionFactory sessionFactory) {
        return false;
    }

}
