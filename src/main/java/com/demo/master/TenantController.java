package com.demo.master;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class TenantController {
    private final TenantRepository tenantRepository;

    public TenantController(
            TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @GetMapping("/tenant")
    public List<TenantDto> getTenants() {
        return tenantRepository.findAll()
                .stream()
                .map(e -> new TenantDto(
                        e.id(),
                        e.name(),
                        e.databaseUrl(),
                        e.databaseUsername(),
                        e.databasePassword(),
                        e.databaseDriverClassName()))
                .toList();
    }
}
