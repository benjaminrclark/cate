package org.cateproject.domain.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;

@Entity
public class UserGroup extends Principal {

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Permission> permissions = new HashSet<Permission>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "groups")
    private List<UserAccount> members = new ArrayList<UserAccount>();
    
    @Transient
    public Collection<GrantedAuthority> getGrantedAuthorities() {
        return (Collection) permissions;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Set<Permission> getPermissions() {
        return this.permissions;
    }
    
    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
    
    public List<UserAccount> getMembers() {
        return this.members;
    }
    
    public void setMembers(List<UserAccount> members) {
        this.members = members;
    }
    
    public String toString() {
        return "UserGroup<" + this.getId() + ">";
    }
}
