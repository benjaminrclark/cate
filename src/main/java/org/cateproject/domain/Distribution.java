package org.cateproject.domain;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.gbif.ecat.voc.EstablishmentMeans;
import org.gbif.ecat.voc.LifeStage;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.cateproject.domain.constants.Locality;
import org.gbif.ecat.voc.OccurrenceStatus;
import org.hibernate.envers.Audited;

@Audited
@Entity
public class Distribution extends OwnedEntity {

    @NotNull
    private String identifier;

    @Enumerated
    private Locality location;
    
    private String loclity;

    private String countryCode;

    @Enumerated
    private LifeStage lifeStage;

    @Enumerated
    private OccurrenceStatus occurrenceStatus;
    
    private String occurrenceRemarks;
    
    @Enumerated
    private EstablishmentMeans establishmentMeans;

    @NotNull
    @ManyToOne
    private Taxon taxon;
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public Locality getLocation() {
        return this.location;
    }
    
    public void setLocation(Locality location) {
        this.location = location;
    }
    
    public String getLoclity() {
        return this.loclity;
    }
    
    public void setLoclity(String loclity) {
        this.loclity = loclity;
    }
    
    public String getCountryCode() {
        return this.countryCode;
    }
    
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    
    public LifeStage getLifeStage() {
        return this.lifeStage;
    }
    
    public void setLifeStage(LifeStage lifeStage) {
        this.lifeStage = lifeStage;
    }
    
    public OccurrenceStatus getOccurrenceStatus() {
        return this.occurrenceStatus;
    }
    
    public void setOccurrenceStatus(OccurrenceStatus occurrenceStatus) {
        this.occurrenceStatus = occurrenceStatus;
    }
    
    public String getOccurrenceRemarks() {
        return this.occurrenceRemarks;
    }
    
    public void setOccurrenceRemarks(String occurrenceRemarks) {
        this.occurrenceRemarks = occurrenceRemarks;
    }
    
    public EstablishmentMeans getEstablishmentMeans() {
        return this.establishmentMeans;
    }
    
    public void setEstablishmentMeans(EstablishmentMeans establishmentMeans) {
        this.establishmentMeans = establishmentMeans;
    }
    
    public Taxon getTaxon() {
        return this.taxon;
    }
    
    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
