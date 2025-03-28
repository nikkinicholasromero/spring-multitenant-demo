package com.demo.master;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@Entity
@Table(name = "tenants")
public class Tenant extends BaseEntity<String> implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "database_url")
    private String databaseUrl;

    @Column(name = "database_username")
    private String databaseUsername;

    @Column(name = "database_password")
    private String databasePassword;

    @Column(name = "database_driver_class_name")
    private String databaseDriverClassName;

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String databaseUrl() {
        return databaseUrl;
    }

    public String databaseUsername() {
        return databaseUsername;
    }

    public String databasePassword() {
        return databasePassword;
    }

    public String databaseDriverClassName() {
        return databaseDriverClassName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Tenant other = (Tenant) o;

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
                .append("name", name)
                .append("databaseUrl", databaseUrl)
                .append("databaseUsername", databaseUsername)
                .append("databasePassword", "********")
                .append("databaseDriverClassName", databaseDriverClassName)
                .toString();
    }
}
