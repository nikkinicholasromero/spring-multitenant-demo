package com.demo.master;

public record TenantConfigDto(
        String name,
        String url,
        String username,
        String password
) {
}
