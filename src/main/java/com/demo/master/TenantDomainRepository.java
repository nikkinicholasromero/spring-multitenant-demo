package com.demo.master;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantDomainRepository extends JpaRepository<TenantDomain, String> {
}
