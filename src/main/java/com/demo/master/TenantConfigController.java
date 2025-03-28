package com.demo.master;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class TenantConfigController {
    private final TenantConfigRepository tenantConfigRepository;

    public TenantConfigController(
            TenantConfigRepository tenantConfigRepository) {
        this.tenantConfigRepository = tenantConfigRepository;
    }

    @GetMapping("/tenant")
    public List<TenantConfigDto> getTenants() {
        return tenantConfigRepository.findAll()
                .stream()
                .map(e -> new TenantConfigDto(
                        e.id(),
                        e.name(),
                        e.url(),
                        e.username(),
                        e.password(),
                        e.driverClassName()))
                .toList();
    }
}
