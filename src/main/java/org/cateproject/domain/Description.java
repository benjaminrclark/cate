package org.cateproject.domain;

import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cateproject.domain.constants.DescriptionType;
import org.hibernate.envers.Audited;

@Audited
@Entity
public class Description extends OwnedEntity {

    @Lob
    private String description;

    @Enumerated
    private DescriptionType type;

    private String source;

    private String audience;
    
    private Locale language;

    @NotNull
    @ManyToOne
    private Taxon taxon;

    @NotNull
    private String identifier;
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public DescriptionType getType() {
        return this.type;
    }
    
    public void setType(DescriptionType type) {
        this.type = type;
    }
    
    public String getSource() {
        return this.source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getAudience() {
        return this.audience;
    }
    
    public void setAudience(String audience) {
        this.audience = audience;
    }
    
    public Locale getLanguage() {
        return this.language;
    }
    
    public void setLanguage(Locale language) {
        this.language = language;
    }
    
    public Taxon getTaxon() {
        return this.taxon;
    }
    
    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
