package org.cateproject.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.gbif.ecat.voc.NomenclaturalCode;
import org.gbif.ecat.voc.NomenclaturalStatus;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.hibernate.annotations.NaturalId;
import org.hibernate.envers.Audited;

@Entity
@Audited
public class Taxon extends NonOwnedEntity {

    @NotNull
    @NaturalId
    private String taxonId;

    private String scientificNameID;

    private Integer namePublishedInYear;

    private String higherClassification;

    private String kingdom;

    private String phylum;

    private String clazz;

    @Column(name = "ordr")
    private String order;

    private String family;

    private String genus;

    private String subgenus;

    private String specificEpithet;

    private String infraspecificEpithet;

    @Enumerated(EnumType.STRING)
    private Rank taxonRank;

    private String verbatimTaxonRank;

    private String scientificNameAuthorship;

    @Enumerated
    private NomenclaturalCode nomenclaturalCode;

    @Enumerated
    private TaxonomicStatus taxonomicStatus;

    private String taxonRemarks;
    
    private String bibliographicCitation;
    
    private String namePublishedInString;

    @OneToMany(mappedBy = "parentNameUsage")
    private Set<org.cateproject.domain.Taxon> children = new HashSet<org.cateproject.domain.Taxon>();

    @ManyToOne
    private org.cateproject.domain.Taxon parentNameUsage;

    @OneToMany(mappedBy = "acceptedNameUsage")
    private Set<org.cateproject.domain.Taxon> synonyms = new HashSet<org.cateproject.domain.Taxon>();

    @ManyToOne
    private org.cateproject.domain.Taxon acceptedNameUsage;

    @ManyToOne
    private org.cateproject.domain.Taxon originalNameUsage;

    @OneToMany(mappedBy = "taxon")
    private Set<Description> descriptions = new HashSet<Description>();

    @ManyToMany(mappedBy = "taxa")
    private List<Multimedia> multimedia = new ArrayList<Multimedia>();

    @ManyToMany(mappedBy = "taxa")
    private List<Reference> references = new ArrayList<Reference>();

    @ManyToMany(mappedBy = "taxa")
    private List<TypeAndSpecimen> typesAndSpecimens = new ArrayList<TypeAndSpecimen>();

    @ManyToOne
    private Reference namePublishedIn;

    @ManyToOne
    private Reference nameAccordingTo;

    @OneToMany(mappedBy = "taxon")
    private Set<Distribution> distribution = new HashSet<Distribution>();

    @OneToMany(mappedBy = "taxon")
    private Set<MeasurementOrFact> measurementsOrFacts = new HashSet<MeasurementOrFact>();

    private String scientificName;
    
    @ManyToMany(mappedBy = "taxa")
    private List<Term> terms = new ArrayList<Term>();

    @Transient
    private List<org.cateproject.domain.Taxon> higherTaxa;

    @OneToMany(mappedBy = "taxon")
    private Set<VernacularName> vernacularNames = new HashSet<VernacularName>();
    
    private NomenclaturalStatus nomenclaturalStatus;

    public void setHigherTaxa(List<org.cateproject.domain.Taxon> higherTaxa) {
        this.higherTaxa = higherTaxa;
    }

    public List<org.cateproject.domain.Taxon> getHigherTaxa() {
        if (higherTaxa == null) {
            List<Taxon> list = new ArrayList<Taxon>();
            calculateHigherTaxa(this, list);
            setHigherTaxa(list);
        }
        return higherTaxa;
    }

    private void calculateHigherTaxa(org.cateproject.domain.Taxon t, List<org.cateproject.domain.Taxon> higherTaxa) {
        if (t.getParentNameUsage() != null) {
            calculateHigherTaxa(t.getParentNameUsage(), higherTaxa);
        }
        higherTaxa.add(t);
    }
    
	@Transient
	public String getIdentifier() {
		return this.getTaxonId();
	}
	
	@Transient
	public void setIdentifier(String identifier) {
		this.setTaxonId(identifier);
	}
	
	public String getTaxonId() {
        return this.taxonId;
    }
    
    public void setTaxonId(String taxonId) {
        this.taxonId = taxonId;
    }
    
    public String getScientificNameID() {
        return this.scientificNameID;
    }
    
    public void setScientificNameID(String scientificNameID) {
        this.scientificNameID = scientificNameID;
    }
    
    public Integer getNamePublishedInYear() {
        return this.namePublishedInYear;
    }
    
    public void setNamePublishedInYear(Integer namePublishedInYear) {
        this.namePublishedInYear = namePublishedInYear;
    }
    
    public String getHigherClassification() {
        return this.higherClassification;
    }
    
    public void setHigherClassification(String higherClassification) {
        this.higherClassification = higherClassification;
    }
    
    public String getKingdom() {
        return this.kingdom;
    }
    
    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
    }
    
    public String getPhylum() {
        return this.phylum;
    }
    
    public void setPhylum(String phylum) {
        this.phylum = phylum;
    }
    
    public String getClazz() {
        return this.clazz;
    }
    
    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
    
    public String getOrder() {
        return this.order;
    }
    
    public void setOrder(String order) {
        this.order = order;
    }
    
    public String getFamily() {
        return this.family;
    }
    
    public void setFamily(String family) {
        this.family = family;
    }
    
    public String getGenus() {
        return this.genus;
    }
    
    public void setGenus(String genus) {
        this.genus = genus;
    }
    
    public String getSubgenus() {
        return this.subgenus;
    }
    
    public void setSubgenus(String subgenus) {
        this.subgenus = subgenus;
    }
    
    public String getSpecificEpithet() {
        return this.specificEpithet;
    }
    
    public void setSpecificEpithet(String specificEpithet) {
        this.specificEpithet = specificEpithet;
    }
    
    public String getInfraspecificEpithet() {
        return this.infraspecificEpithet;
    }
    
    public void setInfraspecificEpithet(String infraspecificEpithet) {
        this.infraspecificEpithet = infraspecificEpithet;
    }
    
    public Rank getTaxonRank() {
        return this.taxonRank;
    }
    
    public void setTaxonRank(Rank taxonRank) {
        this.taxonRank = taxonRank;
    }
    
    public String getVerbatimTaxonRank() {
        return this.verbatimTaxonRank;
    }
    
    public void setVerbatimTaxonRank(String verbatimTaxonRank) {
        this.verbatimTaxonRank = verbatimTaxonRank;
    }
    
    public String getScientificNameAuthorship() {
        return this.scientificNameAuthorship;
    }
    
    public void setScientificNameAuthorship(String scientificNameAuthorship) {
        this.scientificNameAuthorship = scientificNameAuthorship;
    }
    
    public NomenclaturalCode getNomenclaturalCode() {
        return this.nomenclaturalCode;
    }
    
    public void setNomenclaturalCode(NomenclaturalCode nomenclaturalCode) {
        this.nomenclaturalCode = nomenclaturalCode;
    }
    
    public TaxonomicStatus getTaxonomicStatus() {
        return this.taxonomicStatus;
    }
    
    public void setTaxonomicStatus(TaxonomicStatus taxonomicStatus) {
        this.taxonomicStatus = taxonomicStatus;
    }
    
    public String getTaxonRemarks() {
        return this.taxonRemarks;
    }
    
    public void setTaxonRemarks(String taxonRemarks) {
        this.taxonRemarks = taxonRemarks;
    }
    
    public String getBibliographicCitation() {
        return this.bibliographicCitation;
    }
    
    public void setBibliographicCitation(String bibliographicCitation) {
        this.bibliographicCitation = bibliographicCitation;
    }
    
    public String getNamePublishedInString() {
        return this.namePublishedInString;
    }
    
    public void setNamePublishedInString(String namePublishedInString) {
        this.namePublishedInString = namePublishedInString;
    }
    
    public Set<Taxon> getChildren() {
        return this.children;
    }
    
    public void setChildren(Set<Taxon> children) {
        this.children = children;
    }
    
    public Taxon getParentNameUsage() {
        return this.parentNameUsage;
    }
    
    public void setParentNameUsage(Taxon parentNameUsage) {
        this.parentNameUsage = parentNameUsage;
    }
    
    public Set<Taxon> getSynonyms() {
        return this.synonyms;
    }
    
    public void setSynonyms(Set<Taxon> synonyms) {
        this.synonyms = synonyms;
    }
    
    public Taxon getAcceptedNameUsage() {
        return this.acceptedNameUsage;
    }
    
    public void setAcceptedNameUsage(Taxon acceptedNameUsage) {
        this.acceptedNameUsage = acceptedNameUsage;
    }
    
    public Taxon getOriginalNameUsage() {
        return this.originalNameUsage;
    }
    
    public void setOriginalNameUsage(Taxon originalNameUsage) {
        this.originalNameUsage = originalNameUsage;
    }
    
    public Set<Description> getDescriptions() {
        return this.descriptions;
    }
    
    public void setDescriptions(Set<Description> descriptions) {
        this.descriptions = descriptions;
    }
    
    public List<Multimedia> getMultimedia() {
        return this.multimedia;
    }
    
    public void setMultimedia(List<Multimedia> multimedia) {
        this.multimedia = multimedia;
    }
    
    public List<Reference> getReferences() {
        return this.references;
    }
    
    public void setReferences(List<Reference> references) {
        this.references = references;
    }
    
    public List<TypeAndSpecimen> getTypesAndSpecimens() {
        return this.typesAndSpecimens;
    }
    
    public void setTypesAndSpecimens(List<TypeAndSpecimen> typesAndSpecimens) {
        this.typesAndSpecimens = typesAndSpecimens;
    }
    
    public Reference getNamePublishedIn() {
        return this.namePublishedIn;
    }
    
    public void setNamePublishedIn(Reference namePublishedIn) {
        this.namePublishedIn = namePublishedIn;
    }
    
    public Reference getNameAccordingTo() {
        return this.nameAccordingTo;
    }
    
    public void setNameAccordingTo(Reference nameAccordingTo) {
        this.nameAccordingTo = nameAccordingTo;
    }
    
    public Set<Distribution> getDistribution() {
        return this.distribution;
    }
    
    public void setDistribution(Set<Distribution> distribution) {
        this.distribution = distribution;
    }
    
    public Set<MeasurementOrFact> getMeasurementsOrFacts() {
        return this.measurementsOrFacts;
    }
    
    public void setMeasurementsOrFacts(Set<MeasurementOrFact> measurementsOrFacts) {
        this.measurementsOrFacts = measurementsOrFacts;
    }
    
    public String getScientificName() {
        return this.scientificName;
    }
    
    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }
    
    public List<Term> getTerms() {
        return this.terms;
    }
    
    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }
    
    public Set<VernacularName> getVernacularNames() {
        return this.vernacularNames;
    }
    
    public void setVernacularNames(Set<VernacularName> vernacularNames) {
        this.vernacularNames = vernacularNames;
    }
    
    public NomenclaturalStatus getNomenclaturalStatus() {
        return this.nomenclaturalStatus;
    }
    
    public void setNomenclaturalStatus(NomenclaturalStatus nomenclaturalStatus) {
        this.nomenclaturalStatus = nomenclaturalStatus;
    }

	@Override
	public void setTaxa(List<Taxon> taxa) {
		throw new UnsupportedOperationException("Taxon does not support setTaxa");
	}

	@Override
	public List<Taxon> getTaxa() {
		throw new UnsupportedOperationException("Taxon does not support getTaxa");
	}
}
