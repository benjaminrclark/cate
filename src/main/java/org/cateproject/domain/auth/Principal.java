package org.cateproject.domain.auth;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.cateproject.multitenant.MultitenantAware;
import org.cateproject.multitenant.MultitenantContextHolder;

@MappedSuperclass
public class Principal implements MultitenantAware {

    private String tenant;
    
    @Id
    @GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTenant() {
        return this.tenant;
    }
    
    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
	
    @PrePersist
    public void prePersist() {
		this.setTenant(MultitenantContextHolder.getContext().getTenantId());
	}
   
}
