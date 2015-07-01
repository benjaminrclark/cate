package org.cateproject.multitenant.domain;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.proxy.HibernateProxyHelper;

public class Multitenant {

    private String identifier;
    
    private String hostname;

    private String databaseUsername;

    private String databasePassword;

    private String databaseUrl;

    private String adminEmail;

    private String adminPassword;
    
    private String ownerEmail;
    
    private String ownerPassword;

    private String title;

	private Long id;

	private String driverClassName;

	private Integer version;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getDatabaseUsername() {
		return databaseUsername;
	}

	public void setDatabaseUsername(String databaseUsername) {
		this.databaseUsername = databaseUsername;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getDriverClassName() {
		return driverClassName;
	}
	
    @Override
    public boolean equals(Object other) {
    	// check for self-comparison
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if ((other.getClass().equals(this.getClass()))) {

            Multitenant tenant = (Multitenant) other;
            if (this.getIdentifier() == null && tenant.getIdentifier() == null) {

                if (this.getId() != null && tenant.getId() != null) {
                    return ObjectUtils.equals(this.getId(), tenant.getId());
                } else {
                    return false;
                }
            } else {
                return ObjectUtils.equals(this.identifier, tenant.getIdentifier());
            }
        } else if (HibernateProxyHelper.getClassWithoutInitializingProxy(other)
                .equals(this.getClass())) {
            // Case to check when proxies are involved
            Multitenant tenant = (Multitenant) other;

            if (this.identifier == null && tenant.getIdentifier() == null) {
                return false;
            } else {
                return ObjectUtils.equals(this.identifier, tenant.getIdentifier());
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(this.identifier);
    }

	public Integer getVersion() {
		return version;
	}
	
	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public String getOwnerPassword() {
		return ownerPassword;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public void setOwnerPassword(String ownerPassword) {
		this.ownerPassword = ownerPassword;
	}    
}
