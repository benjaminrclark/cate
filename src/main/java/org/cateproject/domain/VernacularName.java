package org.cateproject.domain;

import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cateproject.domain.constants.Location;
import org.cateproject.domain.constants.Sex;
import org.gbif.ecat.voc.LifeStage;
import org.hibernate.envers.Audited;

@Audited
@Entity
public class VernacularName extends OwnedEntity {

    @NotNull
    private String vernacularName;
    
    @NotNull
    private String identifier;

    private String source;

    private Locale lang;

    private String temporal;

    @Enumerated
    private Location location;

    private String locality;

    private String countryCode;

    @Enumerated
    private Sex sex;

    private Boolean plural;

    private Boolean preferredName;

    private String organismPart;

    private String taxonRemarks;

    @Enumerated
    private LifeStage lifeStage;

    @NotNull
    @ManyToOne
    private Taxon taxon;
    
    public String getVernacularName() {
        return this.vernacularName;
    }
    
    public void setVernacularName(String vernacularName) {
        this.vernacularName = vernacularName;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public String getSource() {
        return this.source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public Locale getLang() {
        return this.lang;
    }
    
    public void setLang(Locale lang) {
        this.lang = lang;
    }
    
    public String getTemporal() {
        return this.temporal;
    }
    
    public void setTemporal(String temporal) {
        this.temporal = temporal;
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public String getLocality() {
        return this.locality;
    }
    
    public void setLocality(String locality) {
        this.locality = locality;
    }
    
    public String getCountryCode() {
        return this.countryCode;
    }
    
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    
    public Sex getSex() {
        return this.sex;
    }
    
    public void setSex(Sex sex) {
        this.sex = sex;
    }
    
    public Boolean getPlural() {
        return this.plural;
    }
    
    public void setPlural(Boolean plural) {
        this.plural = plural;
    }
    
    public Boolean getPreferredName() {
        return this.preferredName;
    }
    
    public void setPreferredName(Boolean preferredName) {
        this.preferredName = preferredName;
    }
    
    public String getOrganismPart() {
        return this.organismPart;
    }
    
    public void setOrganismPart(String organismPart) {
        this.organismPart = organismPart;
    }
    
    public String getTaxonRemarks() {
        return this.taxonRemarks;
    }
    
    public void setTaxonRemarks(String taxonRemarks) {
        this.taxonRemarks = taxonRemarks;
    }
    
    public LifeStage getLifeStage() {
        return this.lifeStage;
    }
    
    public void setLifeStage(LifeStage lifeStage) {
        this.lifeStage = lifeStage;
    }
    
    public Taxon getTaxon() {
        return this.taxon;
    }
    
    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }
}
