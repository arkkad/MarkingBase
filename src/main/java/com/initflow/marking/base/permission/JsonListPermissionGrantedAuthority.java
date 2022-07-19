package com.initflow.marking.base.permission;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class JsonListPermissionGrantedAuthority implements Serializable {

    private List<JsonPermissionGrantedAuthority> auths;

    public JsonListPermissionGrantedAuthority(){
    }

    public JsonListPermissionGrantedAuthority(List<JsonPermissionGrantedAuthority> auths) {
        this.auths = auths;
    }

    public List<JsonPermissionGrantedAuthority> getAuths() {
        return auths;
    }

    public void setAuths(List<JsonPermissionGrantedAuthority> auths) {
        this.auths = auths;
    }

    public void setAuthsByPermissionGrantedAuth(List<PermissionGrantedAuthority> auths) {
        this.auths = auths != null
                ? auths.stream().map(JsonPermissionGrantedAuthority::new).collect(Collectors.toList())
                : null;
    }
}
