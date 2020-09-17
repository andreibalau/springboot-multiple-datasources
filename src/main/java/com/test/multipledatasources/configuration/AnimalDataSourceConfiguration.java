package com.test.multipledatasources.configuration;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
// basePackageClasses -> is scanning the package of the classes not the class itself, take care !!!
@EnableJpaRepositories(basePackages = "com.test.multipledatasources.repository.animal", entityManagerFactoryRef = "animalEntityManagerFactory", transactionManagerRef = "animalTransactionManager")
public class AnimalDataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("animal.datasource")
    public DataSourceProperties animalDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("animal.datasource")
    public DataSource animalDataSource() {
        return DataSourceBuilder.create().driverClassName(animalDataSourceProperties().getDriverClassName())
                .url(animalDataSourceProperties().getUrl()).username(animalDataSourceProperties().getUsername())
                .password(animalDataSourceProperties().getPassword()).build();
    }

    @Primary
    @Bean(name = "animalEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean animalEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        HashMap<String, String> props = new HashMap<>();
        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");// FIXME: the hibernat.h2,jpa and so on
                                                                          // settings are not scaned !!! use
                                                                          // props,without these props the tables are
                                                                          // not created !!!
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.format_sql", "true");
        return builder.dataSource(animalDataSource()).persistenceUnit("animal-database")
                .packages("com.test.multipledatasources.model.animal").properties(props).build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager animalTransactionManager(
            final @Qualifier("animalEntityManagerFactory") LocalContainerEntityManagerFactoryBean animalEntityManagerFactory) {
        return new JpaTransactionManager(animalEntityManagerFactory.getObject());
    }
}
