package com.samuksa.user.config.mybatis.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SamuksaUserDbDataSourceClass {

    @Bean(name = "SamuksaUserDbDataSource")
    @ConfigurationProperties(prefix = "spring.samuksa-user-db-datasource")
    public DataSource samuksaUserDbDataSource() { return DataSourceBuilder.create().build(); }

}
