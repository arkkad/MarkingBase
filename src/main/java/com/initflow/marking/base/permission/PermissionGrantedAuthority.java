package com.initflow.marking.base.permission;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Set;

public class PermissionGrantedAuthority implements GrantedAuthority {
//    public PermissionGrantedAuthority(String role) {
//        super(role);
//    }


    private static final long serialVersionUID = 510L;
    private final String role;
    private final Set<String> permissions;
//    private

    public PermissionGrantedAuthority(String role, Set<String> permissions) {
        Assert.hasText(role, "A granted authority textual representation is required");
        this.role = role;
        this.permissions = Collections.unmodifiableSet(permissions);
    }

    public String getAuthority() {
        return this.role;
    }

    public Set<String> getPermissions(){
        return this.permissions;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof PermissionGrantedAuthority && this.role.equals(((PermissionGrantedAuthority) obj).role);
        }
    }

    public int hashCode() {
        return this.role.hashCode();
    }

    public String toString() {
        return this.role;
    }
}
