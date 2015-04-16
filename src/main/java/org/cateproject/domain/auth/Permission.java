package org.cateproject.domain.auth;

import org.springframework.security.core.GrantedAuthority;


public enum Permission implements GrantedAuthority {

    PERMISSION_ADMINISTRATE,
    PERMISSION_EDIT,
    PERMISSION_CONFIGURE_TENANTS;
    
    public String getAuthority() {
        return this.name();
    }
}
