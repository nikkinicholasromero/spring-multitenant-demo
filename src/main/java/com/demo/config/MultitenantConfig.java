package com.demo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.demo.domain",
        entityManagerFactoryRef = "multitenantEntityManagerFactory",
        transactionManagerRef = "multitenantTransactionManager"
)
public class MultitenantConfig {
    @Value("${default.tenant}")
    private String defaultTenant;

    @Bean
    public DataSource multitenantDataSource(DataSource masterDataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(masterDataSource);
        Map<Object, Object> resolvedDataSources = new HashMap<>();

        List<Map<String, Object>> tenants = jdbcTemplate.queryForList("SELECT name, url, username, password FROM tenant_config");

        for (Map<String, Object> tenant : tenants) {
            DataSourceBuilder<?> builder = DataSourceBuilder.create()
                    .url((String) tenant.get("url"))
                    .username((String) tenant.get("username"))
                    .password((String) tenant.get("password"))
                    .driverClassName("org.postgresql.Driver");

            resolvedDataSources.put(tenant.get("name"), builder.build());
        }

        AbstractRoutingDataSource dataSource = new MultitenantDataSource();
        dataSource.setDefaultTargetDataSource(resolvedDataSources.get(defaultTenant));
        dataSource.setTargetDataSources(resolvedDataSources);
        dataSource.afterPropertiesSet();
        return dataSource;
    }


    @Bean(name = "multitenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean multitenantEntityManagerFactory(
            DataSource multitenantDataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(multitenantDataSource)
                .packages("com.demo.domain")
                .build();
    }


    @Bean(name = "multitenantTransactionManager")
    public PlatformTransactionManager multitenantTransactionManager(
            @Qualifier("multitenantEntityManagerFactory") LocalContainerEntityManagerFactoryBean multitenantEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(multitenantEntityManagerFactory.getObject()));
    }
}
