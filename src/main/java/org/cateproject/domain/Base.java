package org.cateproject.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.joda.time.DateTime;

@MappedSuperclass
@Audited
public abstract class Base {
	
	@Id
    @GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;
    
    @Version
    @Column(name = "version")
    private Integer version;

    private String creator;
    
    private String accessRights;
    
    @Type(type="dateTimeUserType")
    private DateTime created;
    
    @Type(type="dateTimeUserType")
    private DateTime modified;
    
    private String license;
    
    private String rights;
    
    private String rightsHolder;
    
    private String contributor;
    
    @ManyToOne
    private Dataset dataset;
    
    @Transient
    private transient String line;

    @Transient
    private transient Integer lineNumber;
    
    public abstract String getIdentifier();
    
    public abstract void setIdentifier(String identifier);
    
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public String getCreator() {
        return this.creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    public String getAccessRights() {
        return this.accessRights;
    }
    
    public void setAccessRights(String accessRights) {
        this.accessRights = accessRights;
    }
    
    public DateTime getCreated() {
        return this.created;
    }
    
    public void setCreated(DateTime created) {
        this.created = created;
    }
    
    public DateTime getModified() {
        return this.modified;
    }
    
    public void setModified(DateTime modified) {
        this.modified = modified;
    }
    
    public String getLicense() {
        return this.license;
    }
    
    public void setLicense(String license) {
        this.license = license;
    }
    
    public String getRights() {
        return this.rights;
    }
    
    public void setRights(String rights) {
        this.rights = rights;
    }
    
    public String getRightsHolder() {
        return this.rightsHolder;
    }
    
    public void setRightsHolder(String rightsHolder) {
        this.rightsHolder = rightsHolder;
    }
    
    public String getContributor() {
        return this.contributor;
    }
    
    public void setContributor(String contributor) {
        this.contributor = contributor;
    }
    
    public Dataset getDataset() {
        return this.dataset;
    }
    
    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }
    
    public String getLine() {
        return this.line;
    }
    
    public void setLine(String line) {
        this.line = line;
    }
    
    public Integer getLineNumber() {
        return this.lineNumber;
    }
    
    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    @Override
    public String toString() {
            return this.getClass().getSimpleName() + "<" + this.getIdentifier() + ">";
    }
}
