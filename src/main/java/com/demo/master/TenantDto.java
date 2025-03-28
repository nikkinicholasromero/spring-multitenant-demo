package com.demo.master;

public record TenantDto(
        String id,
        String name,
        String databaseUrl,
        String databaseUsername,
        String databasePassword,
        String databaseDriverClassName
) {
}
