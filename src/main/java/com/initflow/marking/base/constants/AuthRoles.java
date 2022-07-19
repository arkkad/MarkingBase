package com.initflow.marking.base.constants;

public class AuthRoles {

    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String HAS_ADMIN_AUTH = "hasAnyAuthority('ROLE_ADMIN')";
    public static final String HAS_USER_AUTH = "hasAnyAuthority('ROLE_ADMIN')";
    public static final String HAS_ADMIN_USER_AUTH = "hasAnyAuthority('ROLE_USER','ROLE_ADMIN')";

    private AuthRoles(){}
}
