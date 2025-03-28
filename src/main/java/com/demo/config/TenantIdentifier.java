package com.demo.config;

import com.demo.master.TenantDomain;
import com.demo.master.TenantDomainRepository;
import com.demo.master.TenantDto;
import com.demo.master.TenantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TenantIdentifier {
    private final String defaultTenant;
    private final List<TenantDto> tenants;

    public TenantIdentifier(
            @Value("${default.tenant}") String defaultTenant,
            TenantDomainRepository tenantDomainRepository,
            TenantRepository tenantRepository) {
        this.defaultTenant = defaultTenant;
        List<TenantDomain> tenantDomains = tenantDomainRepository.findAll();
        this.tenants = tenantRepository.findAll()
                .stream()
                .map(t -> new TenantDto(
                        t.id(),
                        t.name(),
                        t.databaseUrl(),
                        t.databaseUsername(),
                        t.databasePassword(),
                        t.databaseDriverClassName(),
                        tenantDomains.stream()
                                .filter(d -> d.tenantId().equals(t.id()))
                                .map(TenantDomain::domainUrl)
                                .toList()))
                .toList();
    }

    public String identifyTenant(String domainUrl) {
        return tenants.stream()
                .filter(t -> t.tenantDomains().contains(domainUrl))
                .map(TenantDto::name)
                .findFirst()
                .orElse(defaultTenant);
    }
}
