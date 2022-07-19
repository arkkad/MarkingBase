package com.initflow.marking.base.permission;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class JsonPermissionGrantedAuthority implements Serializable {

    private static final long serialVersionUID = 510L;
    private String role;
    private Set<String> permissions;

    public JsonPermissionGrantedAuthority(){}

    public JsonPermissionGrantedAuthority(String role, Set<String> permissions) {
        this.role = role;
        this.permissions = new HashSet<>(permissions);
    }

    public JsonPermissionGrantedAuthority(PermissionGrantedAuthority permissionGrantedAuthority) {
        this.role = permissionGrantedAuthority.getAuthority();
        this.permissions = new HashSet<>(permissionGrantedAuthority.getPermissions());
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

}
