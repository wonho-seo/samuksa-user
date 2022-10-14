package com.samuksa.user.config.mybatis.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(
        basePackages = "com.samuksa.user.db.table.samuksa_user_db.repository",
        entityManagerFactoryRef = "SamuksaUserEntityFactoryBean",
        transactionManagerRef = "SamuksaUserTransactionManager"
)
@MapperScan(value = "com.samuksa.user.config.security.mapper", sqlSessionTemplateRef = "UserSessionTemplate")
public class SamuksaUserDbConfig {

    @Value("${spring.samuksa-user-db-datasource.usermapperlocations}")
    String mPath;

    @Bean(name = "SamuksaUserDbDataSource")
    @ConfigurationProperties(prefix = "spring.samuksa-user-db-datasource")
    @Primary
    public DataSource samuksaUserDbDataSource() { return DataSourceBuilder.create().build(); }
    @Bean(name = "UserSqlSessionFactory")
    @Primary
    public SqlSessionFactory SqlSessionFactory(@Qualifier("SamuksaUserDbDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResource(mPath));
        sqlSessionFactoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:config/mybatis-config.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "UserSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("UserSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    @Bean(name = "SamuksaUserEntityFactoryBean")
    @Primary
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(@Qualifier("SamuksaUserDbDataSource") DataSource dataSource){
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.samuksa.user.db.table.samuksa_user_db.entity");

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(adapter);
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "SamuksaUserTransactionManager")
    @Primary
    public PlatformTransactionManager platformTransactionManager(@Qualifier("SamuksaUserEntityFactoryBean") LocalContainerEntityManagerFactoryBean mf){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(mf.getObject());
        return jpaTransactionManager;
    }
}
