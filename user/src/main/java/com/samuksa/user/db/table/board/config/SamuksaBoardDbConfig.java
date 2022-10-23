package com.samuksa.user.db.table.board.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(
        basePackages = "com.samuksa.user.db.table.board.repository",
        entityManagerFactoryRef = "SamuksaBoardEntityFactoryBean",
        transactionManagerRef = "SamuksaBoardTransactionManager"
)
public class SamuksaBoardDbConfig {


    @Bean(name = "SamuksaBoardDbDataSource")
    @ConfigurationProperties(prefix = "spring.samuksa-board-db-datasource")
    public DataSource samuksaUserDbDataSource() { return DataSourceBuilder.create().build(); }


    @Bean(name = "SamuksaBoardEntityFactoryBean")
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(@Qualifier("SamuksaBoardDbDataSource") DataSource dataSource){
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.samuksa.user.db.table.board.entity");

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(adapter);
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean(name = "SamuksaBoardTransactionManager")
    public PlatformTransactionManager platformTransactionManager(@Qualifier("SamuksaBoardEntityFactoryBean") LocalContainerEntityManagerFactoryBean mf){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(mf.getObject());
        return jpaTransactionManager;
    }
}
