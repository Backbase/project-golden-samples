package org.quarkus.openapi.generator.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Tenant {
    private String id;
    private String name;
    private Set<String> realms = new HashSet();

    public Tenant() {
    }

    public String getId() {
        return this.id;
    }

    public Tenant setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Tenant setName(String name) {
        this.name = name;
        return this;
    }

    public Set<String> getRealms() {
        return this.realms;
    }

    public Tenant setRealms(Set<String> realms) {
        this.realms = realms;
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Tenant tenant = (Tenant)o;
            return Objects.equals(this.id, tenant.id) && Objects.equals(this.name, tenant.name) && Objects.equals(this.realms, tenant.realms);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.id, this.name, this.realms});
    }
}
