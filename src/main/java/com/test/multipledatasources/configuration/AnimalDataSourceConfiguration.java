package com.test.multipledatasources.configuration;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "com.test.multipledatasources.repository.animal", entityManagerFactoryRef = "animalEntityManagerFactory", transactionManagerRef = "animalTransactionManager")
public class AnimalDataSourceConfiguration {

    @Value("${animal.datasource.url}")
    private String url;
    @Value("${animal.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${animal.datasource.username}")
    private String username;
    @Value("${animal.datasource.password}")
    private String password;

    @Value("${animal.hibernate.dialect}")
    private String hibernateDialect;
    @Value("${animal.hibernate.hbm2ddl.auto}")
    private String hibernateHbm2ddl;
    @Value("${animal.hibernate.show_sql}")
    private String hibernateShowSQL;
    @Value("${animal.hibernate.format_sql}")
    private String hibernateFormatSQL;
    @Value("${animal.hibernate.use_sql_comments}")
    private String hibernateSQLComments;

    @Primary
    @Bean(name = "animalDataSource")
    public DataSource animalDataSource() {
        return DataSourceBuilder.create().driverClassName(driverClassName).url(url).username(username)
                .password(password).build();
    }

    @Primary
    @Bean(name = "animalEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean animalEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        HashMap<String, String> props = new HashMap<>();
        props.put("hibernate.dialect", hibernateDialect);
        props.put("hibernate.show_sql", hibernateShowSQL);
        props.put("hibernate.hbm2ddl.auto", hibernateHbm2ddl);
        props.put("hibernate.format_sql", hibernateFormatSQL);
        return builder.dataSource(animalDataSource()).persistenceUnit("animal-database")
                .packages("com.test.multipledatasources.model.animal").properties(props).build();
    }

    @Primary
    @Bean(name = "animalTransactionManager")
    public PlatformTransactionManager animalTransactionManager(
            final @Qualifier("animalEntityManagerFactory") LocalContainerEntityManagerFactoryBean animalEntityManagerFactory) {
        return new JpaTransactionManager(animalEntityManagerFactory.getObject());
    }
}
