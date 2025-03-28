package com.demo.config;

import com.demo.master.TenantConfig;
import com.demo.master.TenantConfigRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.demo.domain",
        entityManagerFactoryRef = "multitenantEntityManagerFactory",
        transactionManagerRef = "multitenantTransactionManager")
public class MultitenantConfig {
    @Value("${default.tenant}")
    private String defaultTenant;

    @Bean(name = "multitenantDataSource")
    public DataSource dataSource(TenantConfigRepository tenantConfigRepository) {
        Map<Object, Object> targetDataSources = tenantConfigRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        TenantConfig::name,
                        tenant -> DataSourceBuilder.create()
                                .url(tenant.url())
                                .username(tenant.username())
                                .password(tenant.password())
                                .driverClassName(tenant.driverClassName())
                                .build()));

        AbstractRoutingDataSource dataSource = new MultitenantDataSource();
        dataSource.setDefaultTargetDataSource(targetDataSources.get(defaultTenant));
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.afterPropertiesSet();
        return dataSource;
    }

    @Bean(name = "multitenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("multitenantDataSource") DataSource dataSource, EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource).packages("com.demo.domain").build();
    }

    @Bean(name = "multitenantTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("multitenantEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactoryBean.getObject()));
    }
}
