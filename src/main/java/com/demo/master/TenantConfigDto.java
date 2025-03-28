package com.demo.master;

public record TenantConfigDto(
        String id,
        String name,
        String url,
        String username,
        String password,
        String driverClassName
) {
}
