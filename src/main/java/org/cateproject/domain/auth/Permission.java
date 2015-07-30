package org.cateproject.domain.auth;

import org.springframework.security.core.GrantedAuthority;


public enum Permission implements GrantedAuthority {

    ROLE_ADMINISTRATE,
    ROLE_EDIT,
    ROLE_CONFIGURE_SYSTEM;
    
    public String getAuthority() {
        return this.name();
    }
}
