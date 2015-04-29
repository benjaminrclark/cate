package org.cateproject.domain;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.cateproject.domain.constants.Location;
import org.gbif.ecat.voc.EstablishmentMeans;
import org.gbif.ecat.voc.LifeStage;
import org.gbif.ecat.voc.OccurrenceStatus;
import org.hibernate.envers.Audited;

@Audited
@Entity
public class Distribution extends OwnedEntity {

    @NotNull
    private String identifier;

    @Enumerated
    private Location location;
    
    private String locality;

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
}
