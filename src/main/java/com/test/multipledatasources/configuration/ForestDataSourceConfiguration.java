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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
// basePackageClasses -> is scanning the package of the classes not the class itself, take care !!!
@EnableJpaRepositories(basePackages = "com.test.multipledatasources.repository.forest", entityManagerFactoryRef = "forestEntityManagerFactory", transactionManagerRef = "forestTransactionManager")
public class ForestDataSourceConfiguration {

    @Bean
    @ConfigurationProperties("forest.datasource")
    public DataSourceProperties forestDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("forest.datasource")
    public DataSource forestDataSource() {
        return DataSourceBuilder.create().driverClassName(forestDataSourceProperties().getDriverClassName())
                .url(forestDataSourceProperties().getUrl()).username(forestDataSourceProperties().getUsername())
                .password(forestDataSourceProperties().getPassword()).build();
    }

    @Bean(name = "forestEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean forestEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        HashMap<String, String> props = new HashMap<>();
        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");// FIXME: the hibernat.h2,jpa and so on
                                                                          // settings are not scaned !!! use props,
                                                                          // without these props the tables are not
                                                                          // created !!!
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.format_sql", "true");
        return builder.dataSource(forestDataSource()).persistenceUnit("forest-database").properties(props)
                .packages("com.test.multipledatasources.model.forest").build();
    }

    @Bean
    public PlatformTransactionManager forestTransactionManager(
            final @Qualifier("forestEntityManagerFactory") LocalContainerEntityManagerFactoryBean forestEntityManagerFactory) {
        return new JpaTransactionManager(forestEntityManagerFactory.getObject());
    }
}
