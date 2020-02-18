package org.cgcgframework.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.cgcgframework.core.annotation.CBean;
import org.cgcgframework.core.context.ApplicationProperties;

import javax.annotation.Resource;
import javax.sql.DataSource;

@CBean
public class DataSourceTemplate {

    private DataSource dataSource;

    @Resource
    private ApplicationProperties applicationProperties;

    public DataSource getDataSource() {
        if (this.dataSource != null) {
            return this.dataSource;
        }
        synchronized ("CurrentDataSource") {
            if (this.dataSource != null) {
                return this.dataSource;
            }
            final HikariDataSource dataSource = new HikariDataSource();
            final String jdbcUrl = this.applicationProperties.getProperty("cgcg.datasource.jdbcUrl");
            final String userName = this.applicationProperties.getProperty("cgcg.datasource.username");
            final String password = this.applicationProperties.getProperty("cgcg.datasource.password");
            final String driverClassName = this.applicationProperties.getProperty("cgcg.datasource.driverClassName");
            dataSource.setJdbcUrl(jdbcUrl);
            dataSource.setUsername(userName);
            dataSource.setPassword(password);
            dataSource.setDriverClassName(driverClassName);
            this.dataSource = dataSource;
        }
        return this.dataSource;
    }
}
