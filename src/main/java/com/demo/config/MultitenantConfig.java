package com.demo.config;

import com.demo.master.Tenant;
import com.demo.master.TenantRepository;
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
    public DataSource dataSource(TenantRepository tenantRepository) {
        Map<Object, Object> targetDataSources = tenantRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Tenant::name,
                        tenant -> DataSourceBuilder.create()
                                .url(tenant.databaseHost())
                                .username(tenant.databaseUsername())
                                .password(tenant.databasePassword())
                                .driverClassName(tenant.databaseDriverClassName())
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
