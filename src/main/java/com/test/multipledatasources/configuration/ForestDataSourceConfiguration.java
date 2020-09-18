package com.test.multipledatasources.configuration;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "com.test.multipledatasources.repository.forest", entityManagerFactoryRef = "forestEntityManagerFactory", transactionManagerRef = "forestTransactionManager")
public class ForestDataSourceConfiguration {

    @Value("${forest.datasource.url}")
    private String url;
    @Value("${forest.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${forest.datasource.username}")
    private String username;
    @Value("${forest.datasource.password}")
    private String password;

    @Value("${forest.hibernate.dialect}")
    private String hibernateDialect;
    @Value("${forest.hibernate.hbm2ddl.auto}")
    private String hibernateHbm2ddl;
    @Value("${forest.hibernate.show_sql}")
    private String hibernateShowSQL;
    @Value("${forest.hibernate.format_sql}")
    private String hibernateFormatSQL;
    @Value("${forest.hibernate.use_sql_comments}")
    private String hibernateSQLComments;

    @Bean(name = "forestDataSource")
    public DataSource forestDataSource() {
        return DataSourceBuilder.create().driverClassName(driverClassName).url(url).username(username)
                .password(password).build();
    }

    @Bean(name = "forestEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean forestEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        HashMap<String, String> props = new HashMap<>();
        props.put("hibernate.dialect", hibernateDialect);
        props.put("hibernate.show_sql", hibernateShowSQL);
        props.put("hibernate.hbm2ddl.auto", hibernateHbm2ddl);
        props.put("hibernate.format_sql", hibernateFormatSQL);
        return builder.dataSource(forestDataSource()).persistenceUnit("forest-database").properties(props)
                .packages("com.test.multipledatasources.model.forest").build();
    }

    @Bean(name = "forestTransactionManager")
    public PlatformTransactionManager forestTransactionManager(
            final @Qualifier("forestEntityManagerFactory") LocalContainerEntityManagerFactoryBean forestEntityManagerFactory) {
        return new JpaTransactionManager(forestEntityManagerFactory.getObject());
    }
}
