package com.samuksa.user.domain.userinfo.config;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@MapperScan(value = "com.samuksa.user.domain.userinfo.mapper", sqlSessionTemplateRef = "InfosqlSessionTemplate")
public class UserInfoMybatisConfig {

    @Value("${spring.datasource.user-info-mapper-location}")
    String mPath;

    private final DataSource samuksaUserDbDataSource;

    @Bean
    public SqlSessionFactory InfosqlSessionFactory() throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(samuksaUserDbDataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResource(mPath));
        sqlSessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:config/mybatis-config.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate InfosqlSessionTemplate(@Qualifier("InfosqlSessionFactory") SqlSessionFactory sq){
        return new SqlSessionTemplate(sq);
    }
}
