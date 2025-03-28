package com.demo.master;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@Entity
@Table(name = "tenant_domains")
public class TenantDomain extends BaseEntity<String> implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "domain_url")
    private String domainUrl;

    @Override
    public String id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public String domainUrl() {
        return domainUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TenantDomain other = (TenantDomain) o;

        return new EqualsBuilder()
                .append(id, other.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("tenantId", tenantId)
                .append("domainUrl", domainUrl)
                .toString();
    }
}
