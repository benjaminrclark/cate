package org.cateproject.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.cateproject.domain.constants.Locality;
import org.cateproject.domain.constants.Sex;
import org.cateproject.domain.constants.TypeDesignationType;
import org.cateproject.domain.constants.TypeStatus;
import org.gbif.ecat.voc.Rank;
import org.hibernate.envers.Audited;

@Entity
@Audited
public class TypeAndSpecimen extends NonOwnedEntity {

    @Enumerated
    private TypeStatus typeStatus;

    @Enumerated
    private TypeDesignationType typeDesignationType;

    private String typeDesignatedBy;

    private String scientificName;

    @Enumerated
    private Rank taxonRank;

    private String bibliographicCitation;

    @NotNull
    private String occurrenceId;

    private String institutionCode;

    private String collectionCode;
    
    private Double decimalLatitude;
    
    private Double decimalLongitude;

    private String catalogNumber;

    private String locality;
    
    @Enumerated
    private Locality location;

    @Enumerated
    private Sex sex;

    private String recordedBy;

    private String source;

    private String verbatimEventDate;

    private String verbatimLongitude;

    private String verbatimLatitude;

    private String verbatimLabel;

    @ManyToMany
    @JoinTable(name = "Type_And_Specimen_Taxon", joinColumns = {@JoinColumn(name = "Type_And_Specimen_id")}, inverseJoinColumns = {@JoinColumn(name = "taxa_id")})
    private List<Taxon> taxa = new ArrayList<Taxon>();

	@Transient
	public String getIdentifier() {
		return this.getOccurrenceId();
	}
	
	public TypeStatus getTypeStatus() {
        return this.typeStatus;
    }
    
    public void setTypeStatus(TypeStatus typeStatus) {
        this.typeStatus = typeStatus;
    }
    
    public TypeDesignationType getTypeDesignationType() {
        return this.typeDesignationType;
    }
    
    public void setTypeDesignationType(TypeDesignationType typeDesignationType) {
        this.typeDesignationType = typeDesignationType;
    }
    
    public String getTypeDesignatedBy() {
        return this.typeDesignatedBy;
    }
    
    public void setTypeDesignatedBy(String typeDesignatedBy) {
        this.typeDesignatedBy = typeDesignatedBy;
    }
    
    public String getScientificName() {
        return this.scientificName;
    }
    
    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }
    
    public Rank getTaxonRank() {
        return this.taxonRank;
    }
    
    public void setTaxonRank(Rank taxonRank) {
        this.taxonRank = taxonRank;
    }
    
    public String getBibliographicCitation() {
        return this.bibliographicCitation;
    }
    
    public void setBibliographicCitation(String bibliographicCitation) {
        this.bibliographicCitation = bibliographicCitation;
    }
    
    public String getOccurrenceId() {
        return this.occurrenceId;
    }
    
    public void setOccurrenceId(String occurrenceId) {
        this.occurrenceId = occurrenceId;
    }
    
    public String getInstitutionCode() {
        return this.institutionCode;
    }
    
    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }
    
    public String getCollectionCode() {
        return this.collectionCode;
    }
    
    public void setCollectionCode(String collectionCode) {
        this.collectionCode = collectionCode;
    }
    
    public Double getDecimalLatitude() {
        return this.decimalLatitude;
    }
    
    public void setDecimalLatitude(Double decimalLatitude) {
        this.decimalLatitude = decimalLatitude;
    }
    
    public Double getDecimalLongitude() {
        return this.decimalLongitude;
    }
    
    public void setDecimalLongitude(Double decimalLongitude) {
        this.decimalLongitude = decimalLongitude;
    }
    
    public String getCatalogNumber() {
        return this.catalogNumber;
    }
    
    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }
    
    public String getLocality() {
        return this.locality;
    }
    
    public void setLocality(String locality) {
        this.locality = locality;
    }
    
    public Locality getLocation() {
        return this.location;
    }
    
    public void setLocation(Locality location) {
        this.location = location;
    }
    
    public Sex getSex() {
        return this.sex;
    }
    
    public void setSex(Sex sex) {
        this.sex = sex;
    }
    
    public String getRecordedBy() {
        return this.recordedBy;
    }
    
    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }
    
    public String getSource() {
        return this.source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getVerbatimEventDate() {
        return this.verbatimEventDate;
    }
    
    public void setVerbatimEventDate(String verbatimEventDate) {
        this.verbatimEventDate = verbatimEventDate;
    }
    
    public String getVerbatimLongitude() {
        return this.verbatimLongitude;
    }
    
    public void setVerbatimLongitude(String verbatimLongitude) {
        this.verbatimLongitude = verbatimLongitude;
    }
    
    public String getVerbatimLatitude() {
        return this.verbatimLatitude;
    }
    
    public void setVerbatimLatitude(String verbatimLatitude) {
        this.verbatimLatitude = verbatimLatitude;
    }
    
    public String getVerbatimLabel() {
        return this.verbatimLabel;
    }
    
    public void setVerbatimLabel(String verbatimLabel) {
        this.verbatimLabel = verbatimLabel;
    }
    
    public List<Taxon> getTaxa() {
        return this.taxa;
    }
    
    public void setTaxa(List<Taxon> taxa) {
        this.taxa = taxa;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
