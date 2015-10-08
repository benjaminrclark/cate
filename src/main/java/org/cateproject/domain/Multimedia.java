package org.cateproject.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.cateproject.domain.constants.DCMIType;
import org.cateproject.domain.util.MultimediaFile;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.joda.time.DateTime;

@Audited
@Entity
public class Multimedia extends NonOwnedEntity {
	
    @NotNull
    @NaturalId
    private String identifier;
    
    private String title;

    private String references;

    private String description;

    private String spatial;

    private Double latitude;

    private Double longitude;

    private String format;

    private String publisher;

    private String audience;
    
    private Integer width;
    
    private Integer height;
    
    @Enumerated
    private DCMIType type;
    
    private Long size;
    
    private String hash;
    
    private String localFileName;
    
    @Type(type="dateTimeUserType")
    private DateTime fileLastModified;
    
    @ManyToMany
    @JoinTable(name = "Multimedia_Taxon", joinColumns = {@JoinColumn(name = "Multimedia_id")}, inverseJoinColumns = {@JoinColumn(name = "taxa_id")})
    private List<Taxon> taxa = new ArrayList<Taxon>();
    
    @ManyToOne
    private Term term;
    
    @Transient
    private Set<MultimediaFile> multimediaFiles = new HashSet<MultimediaFile>();

    public void setMultimediaFiles(Set<MultimediaFile> multimediaFiles) {
        this.multimediaFiles = multimediaFiles;
    }

    public Set<MultimediaFile> getMultimediaFiles() {
        return multimediaFiles;
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
    
    public String getReferences() {
        return this.references;
    }
    
    public void setReferences(String references) {
        this.references = references;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSpatial() {
        return this.spatial;
    }
    
    public void setSpatial(String spatial) {
        this.spatial = spatial;
    }
    
    public Double getLatitude() {
        return this.latitude;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public Double getLongitude() {
        return this.longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    public String getFormat() {
        return this.format;
    }
    
    public void setFormat(String format) {
        this.format = format;
    }
    
    public String getPublisher() {
        return this.publisher;
    }
    
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
    public String getAudience() {
        return this.audience;
    }
    
    public void setAudience(String audience) {
        this.audience = audience;
    }
    
    public Integer getWidth() {
        return this.width;
    }
    
    public void setWidth(Integer width) {
        this.width = width;
    }
    
    public Integer getHeight() {
        return this.height;
    }
    
    public void setHeight(Integer height) {
        this.height = height;
    }
    
    public Long getSize() {
        return this.size;
    }
    
    public void setSize(Long size) {
        this.size = size;
    }
    
    public String getHash() {
        return this.hash;
    }
    
    public void setHash(String hash) {
        this.hash = hash;
    }
    
    public String getLocalFileName() {
        return this.localFileName;
    }
    
    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }
    
    public List<Taxon> getTaxa() {
        return this.taxa;
    }
    
    public void setTaxa(List<Taxon> taxa) {
        this.taxa = taxa;
    }
    
    public Term getTerm() {
        return this.term;
    }
    
    public void setTerm(Term term) {
        this.term = term;
    }
    
    public void setFileLastModified(DateTime fileLastModified) {
    	this.fileLastModified = fileLastModified;
    }
    
    public DateTime getFileLastModified() {
    	return this.fileLastModified;
    }

	public DCMIType getType() {
		return type;
	}

	public void setType(DCMIType type) {
		this.type = type;
	}
}
