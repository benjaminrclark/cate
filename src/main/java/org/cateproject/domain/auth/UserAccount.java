package org.cateproject.domain.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.NaturalId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
public class UserAccount extends Principal implements UserDetails {

    private String password;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    @NaturalId
    private String username;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Permission> permissions = new HashSet<Permission>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE })
    private List<UserGroup> groups = new ArrayList<UserGroup>();

    @Autowired(required = false)
    transient PasswordEncoder passwordEncoder;

    private String email;

    private String firstName;

    private String lastName;

    public UserAccount() {
        passwordEncoder = new BCryptPasswordEncoder();
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }
    
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
    
    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Set<Permission> getPermissions() {
        return this.permissions;
    }
    
    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
    
    public List<UserGroup> getGroups() {
        return this.groups;
    }
    
    public void setGroups(List<UserGroup> groups) {
        this.groups = groups;
    }
    
    public PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }
    
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void encodePassword() {
        this.password = passwordEncoder.encode(this.password);
    }

    @Transient
    public Collection<org.springframework.security.core.GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
        grantedAuthorities.addAll(permissions);
        for (UserGroup group : groups) {
            grantedAuthorities.addAll(group.getGrantedAuthorities());
        }
        return grantedAuthorities;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        UserAccount userAccount = (UserAccount) other;
        return ObjectUtils.equals(this.username, username);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(this.username);
    }
    
    public String toString() {
        return "UserAccount<" + this.getId() + ">";
    }
}
