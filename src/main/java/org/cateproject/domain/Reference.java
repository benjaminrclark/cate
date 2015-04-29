package org.cateproject.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import org.cateproject.domain.constants.ReferenceType;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

@Entity
@Audited
public class Reference extends NonOwnedEntity {

    @NotNull
    private String identifier;

    private String bibliographicCitation;

    private String date;

    private String source;

    @Lob
    private String description;

    private String subject;

    @Type(type = "languageUserType")
    private Locale language;

    private String taxonRemarks;
    
    private String title;

    @Enumerated
    private ReferenceType type;

    @ManyToMany
    @JoinTable(name = "Reference_Taxon", joinColumns = {@JoinColumn(name = "Reference_id")}, inverseJoinColumns = {@JoinColumn(name = "taxa_id")})
    private List<Taxon> taxa = new ArrayList<Taxon>();
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public String getBibliographicCitation() {
        return this.bibliographicCitation;
    }
    
    public void setBibliographicCitation(String bibliographicCitation) {
        this.bibliographicCitation = bibliographicCitation;
    }
    
    public String getDate() {
        return this.date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getSource() {
        return this.source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSubject() {
        return this.subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public Locale getLanguage() {
        return this.language;
    }
    
    public void setLanguage(Locale language) {
        this.language = language;
    }
    
    public String getTaxonRemarks() {
        return this.taxonRemarks;
    }
    
    public void setTaxonRemarks(String taxonRemarks) {
        this.taxonRemarks = taxonRemarks;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public ReferenceType getType() {
        return this.type;
    }
    
    public void setType(ReferenceType type) {
        this.type = type;
    }
    
    public List<Taxon> getTaxa() {
        return this.taxa;
    }
    
    public void setTaxa(List<Taxon> taxa) {
        this.taxa = taxa;
    }
}
