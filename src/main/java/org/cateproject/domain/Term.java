package org.cateproject.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.cateproject.domain.constants.CharacterType;
import org.hibernate.envers.Audited;

@Entity
@Audited
public class Term extends NonOwnedEntity {

    private String description;

    @OneToMany(mappedBy = "term")
    private List<Image> images = new ArrayList<Image>();

    @OneToMany(mappedBy = "charactr")
    private List<org.cateproject.domain.Term> states = new ArrayList<org.cateproject.domain.Term>();

    @ManyToOne
    private org.cateproject.domain.Term charactr;

    private String unit;

    @Enumerated(EnumType.STRING)
    private CharacterType type;

    private Integer ordr;
    
    private String title;

    @NotNull
    private String identifier;
    
    @ManyToMany
    @JoinTable(name = "Term_Taxon", joinColumns = {@JoinColumn(name = "Term_id")}, inverseJoinColumns = {@JoinColumn(name = "taxa_id")})
    private List<Taxon> taxa = new ArrayList<Taxon>();
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<Image> getImages() {
        return this.images;
    }
    
    public void setImages(List<Image> images) {
        this.images = images;
    }
    
    public List<Term> getStates() {
        return this.states;
    }
    
    public void setStates(List<Term> states) {
        this.states = states;
    }
    
    public Term getCharactr() {
        return this.charactr;
    }
    
    public void setCharactr(Term charactr) {
        this.charactr = charactr;
    }
    
    public String getUnit() {
        return this.unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public CharacterType getType() {
        return this.type;
    }
    
    public void setType(CharacterType type) {
        this.type = type;
    }
    
    public Integer getOrdr() {
        return this.ordr;
    }
    
    public void setOrdr(Integer ordr) {
        this.ordr = ordr;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public List<Taxon> getTaxa() {
        return this.taxa;
    }
    
    public void setTaxa(List<Taxon> taxa) {
        this.taxa = taxa;
    }
}
