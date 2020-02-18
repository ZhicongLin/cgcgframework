package org.cgcgframework.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public abstract class SqlSessionDaoSupport {
    private SqlSessionTemplate sqlSessionTemplate;

    public SqlSessionDaoSupport() {
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        if (this.sqlSessionTemplate == null || sqlSessionFactory != this.sqlSessionTemplate.getSqlSessionFactory()) {
            this.sqlSessionTemplate = this.createSqlSessionTemplate(sqlSessionFactory);
        }

    }

    protected SqlSessionTemplate createSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    public final SqlSessionFactory getSqlSessionFactory() {
        return this.sqlSessionTemplate != null ? this.sqlSessionTemplate.getSqlSessionFactory() : null;
    }

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public SqlSession getSqlSession() {
        return this.sqlSessionTemplate;
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return this.sqlSessionTemplate;
    }

}
