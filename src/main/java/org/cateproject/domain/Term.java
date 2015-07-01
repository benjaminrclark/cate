package org.cateproject.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
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
    private List<Multimedia> multimedia = new ArrayList<Multimedia>();

    @OneToMany(mappedBy = "character")
    private List<org.cateproject.domain.Term> states = new ArrayList<org.cateproject.domain.Term>();

    @ManyToOne
    private org.cateproject.domain.Term character;

    private String unit;

    @Enumerated(EnumType.STRING)
    private CharacterType type;

    @Column(name = "ordr")
    private Integer order;
    
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
    
    public List<Multimedia> getMultimedia() {
        return this.multimedia;
    }
    
    public void setMultimedia(List<Multimedia> multimedia) {
        this.multimedia = multimedia;
    }
    
    public List<Term> getStates() {
        return this.states;
    }
    
    public void setStates(List<Term> states) {
        this.states = states;
    }
    
    public Term getCharacter() {
        return this.character;
    }
    
    public void setCharacter(Term character) {
        this.character = character;
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
    
    public Integer getOrder() {
        return this.order;
    }
    
    public void setOrder(Integer order) {
        this.order = order;
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
