package com.demo.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {
    private final TenantIdentifier tenantIdentifier;

    public TenantFilter(TenantIdentifier tenantIdentifier) {
        this.tenantIdentifier = tenantIdentifier;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {
        TenantContext.setCurrentTenant(tenantIdentifier.identifyTenant(request.getHeader(HttpHeaders.ORIGIN)));
        chain.doFilter(request, response);
    }
}
