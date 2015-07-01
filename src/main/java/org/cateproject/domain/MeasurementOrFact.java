package org.cateproject.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.joda.time.DateTime;

@Audited
@Entity
public class MeasurementOrFact extends OwnedEntity {

    @ManyToOne
    private Term type;

    @NotNull
    private String identifier;

    @NotNull
    @ManyToOne
    private Taxon taxon;

    private String accuracy;

    private String unit;

    private DateTime determinedDate;

    private String determinedBy;

    private String method;

    private String remarks;

    private String value;
    
    public Term getType() {
        return this.type;
    }
    
    public void setType(Term type) {
        this.type = type;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public Taxon getTaxon() {
        return this.taxon;
    }
    
    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }
    
    public String getAccuracy() {
        return this.accuracy;
    }
    
    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }
    
    public String getUnit() {
        return this.unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public DateTime getDeterminedDate() {
        return this.determinedDate;
    }
    
    public void setDeterminedDate(DateTime determinedDate) {
        this.determinedDate = determinedDate;
    }
    
    public String getDeterminedBy() {
        return this.determinedBy;
    }
    
    public void setDeterminedBy(String determinedBy) {
        this.determinedBy = determinedBy;
    }
    
    public String getMethod() {
        return this.method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public String getRemarks() {
        return this.remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
}
