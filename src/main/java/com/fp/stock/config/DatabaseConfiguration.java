package com.fp.stock.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.beans.PropertyVetoException;

@Configuration
@EnableJpaRepositories("com.fp.stock.repository")
@EnableTransactionManagement
public class DatabaseConfiguration {

    final private Logger log  = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Bean
    DataSource dataSource(DataSourceProperties dataSourceProperties) throws PropertyVetoException {

        log.info("Configuring Datasource");

        if (dataSourceProperties.getUrl() == null) {
            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }

        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        return dataSource;
    }
}
