package org.cateproject.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.envers.Audited;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Audited
@Entity
public class IdentificationKey extends NonOwnedEntity {

    private static Logger logger = LoggerFactory.getLogger(IdentificationKey.class);

    private String description;
    
    private String identifier;
    
    private String title;
    
    @ManyToMany
    @JoinTable(name = "IdentificationKey_Taxon", joinColumns = {@JoinColumn(name = "IdentificationKey_id")}, inverseJoinColumns = {@JoinColumn(name = "taxa_id")})
    private List<Taxon> taxa = new ArrayList<Taxon>();

    @Lob
    private String compiledKey;
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getCompiledKey() {
        return this.compiledKey;
    }
    
    public void setCompiledKey(String compiledKey) {
        this.compiledKey = compiledKey;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@Override
	public void setTaxa(List<Taxon> taxa) {
		this.taxa = taxa;
	}

	@Override
	public List<Taxon> getTaxa() {
		return taxa;
	}
}
