package org.cateproject.domain.admin;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
public class Tenant {
	
	@Id
    @GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    private String identifier;
    
    private String title;
    
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, String> properties = new HashMap<String,String>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, String> theme;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Map<String, String> getTheme() {
		return theme;
	}

	public void setTheme(Map<String, String> theme) {
		this.theme = theme;
	}
}
