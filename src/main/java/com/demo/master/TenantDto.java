package com.demo.master;

import java.util.List;

public record TenantDto(
        String id,
        String name,
        String databaseUrl,
        String databaseUsername,
        String databasePassword,
        String databaseDriverClassName,
        List<String> tenantDomains
) {
}
