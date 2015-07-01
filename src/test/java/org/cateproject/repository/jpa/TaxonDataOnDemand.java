package org.cateproject.repository.jpa;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.domain.Taxon;
import org.gbif.ecat.voc.NomenclaturalCode;
import org.gbif.ecat.voc.NomenclaturalStatus;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class TaxonDataOnDemand {
    
    private Random rnd = new SecureRandom();
    
    private List<Taxon> data;
    
    @Autowired
    DatasetDataOnDemand datasetDataOnDemand;
    
    @Autowired
    ReferenceDataOnDemand referenceDataOnDemand;
    
    @Autowired
    TaxonRepository taxonRepository;
    
    public Taxon getNewTransientTaxon(int index) {
        Taxon obj = new Taxon();
        setAcceptedNameUsage(obj, index);
        setAccessRights(obj, index);
        setBibliographicCitation(obj, index);
        setClazz(obj, index);
        setContributor(obj, index);
        setCreated(obj, index);
        setCreator(obj, index);
        setFamily(obj, index);
        setGenus(obj, index);
        setHigherClassification(obj, index);
        setInfraspecificEpithet(obj, index);
        setKingdom(obj, index);
        setLicense(obj, index);
        setModified(obj, index);
        setNamePublishedInString(obj, index);
        setNamePublishedInYear(obj, index);
        setNomenclaturalCode(obj, index);
        setNomenclaturalStatus(obj, index);
        setOrder(obj, index);
        setOriginalNameUsage(obj, index);
        setParentNameUsage(obj, index);
        setPhylum(obj, index);
        setRights(obj, index);
        setRightsHolder(obj, index);
        setScientificName(obj, index);
        setScientificNameAuthorship(obj, index);
        setScientificNameID(obj, index);
        setSpecificEpithet(obj, index);
        setSubgenus(obj, index);
        setTaxonId(obj, index);
        setTaxonRank(obj, index);
        setTaxonRemarks(obj, index);
        setTaxonomicStatus(obj, index);
        setVerbatimTaxonRank(obj, index);
        return obj;
    }
    
    public void setAcceptedNameUsage(Taxon obj, int index) {
        Taxon acceptedNameUsage = obj;
        obj.setAcceptedNameUsage(acceptedNameUsage);
    }
    
    public void setAccessRights(Taxon obj, int index) {
        String accessRights = "accessRights_" + index;
        obj.setAccessRights(accessRights);
    }
    
    public void setBibliographicCitation(Taxon obj, int index) {
        String bibliographicCitation = "bibliographicCitation_" + index;
        obj.setBibliographicCitation(bibliographicCitation);
    }
    
    public void setClazz(Taxon obj, int index) {
        String clazz = "clazz_" + index;
        obj.setClazz(clazz);
    }
    
    public void setContributor(Taxon obj, int index) {
        String contributor = "contributor_" + index;
        obj.setContributor(contributor);
    }
    
    public void setCreated(Taxon obj, int index) {
        DateTime created = null;
        obj.setCreated(created);
    }
    
    public void setCreator(Taxon obj, int index) {
        String creator = "creator_" + index;
        obj.setCreator(creator);
    }
    
    public void setFamily(Taxon obj, int index) {
        String family = "family_" + index;
        obj.setFamily(family);
    }
    
    public void setGenus(Taxon obj, int index) {
        String genus = "genus_" + index;
        obj.setGenus(genus);
    }
    
    public void setHigherClassification(Taxon obj, int index) {
        String higherClassification = "higherClassification_" + index;
        obj.setHigherClassification(higherClassification);
    }
    
    public void setInfraspecificEpithet(Taxon obj, int index) {
        String infraspecificEpithet = "infraspecificEpithet_" + index;
        obj.setInfraspecificEpithet(infraspecificEpithet);
    }
    
    public void setKingdom(Taxon obj, int index) {
        String kingdom = "kingdom_" + index;
        obj.setKingdom(kingdom);
    }
    
    public void setLicense(Taxon obj, int index) {
        String license = "license_" + index;
        obj.setLicense(license);
    }
    
    public void setModified(Taxon obj, int index) {
        DateTime modified = null;
        obj.setModified(modified);
    }
    
    public void setNamePublishedInString(Taxon obj, int index) {
        String namePublishedInString = "namePublishedInString_" + index;
        obj.setNamePublishedInString(namePublishedInString);
    }
    
    public void setNamePublishedInYear(Taxon obj, int index) {
        Integer namePublishedInYear = new Integer(index);
        obj.setNamePublishedInYear(namePublishedInYear);
    }
    
    public void setNomenclaturalCode(Taxon obj, int index) {
        NomenclaturalCode nomenclaturalCode = NomenclaturalCode.class.getEnumConstants()[0];
        obj.setNomenclaturalCode(nomenclaturalCode);
    }
    
    public void setNomenclaturalStatus(Taxon obj, int index) {
        NomenclaturalStatus nomenclaturalStatus = null;
        obj.setNomenclaturalStatus(nomenclaturalStatus);
    }
    
    public void setOrder(Taxon obj, int index) {
        String order = "order_" + index;
        obj.setOrder(order);
    }
    
    public void setOriginalNameUsage(Taxon obj, int index) {
        Taxon originalNameUsage = obj;
        obj.setOriginalNameUsage(originalNameUsage);
    }
    
    public void setParentNameUsage(Taxon obj, int index) {
        Taxon parentNameUsage = obj;
        obj.setParentNameUsage(parentNameUsage);
    }
    
    public void setPhylum(Taxon obj, int index) {
        String phylum = "phylum_" + index;
        obj.setPhylum(phylum);
    }
    
    public void setRights(Taxon obj, int index) {
        String rights = "rights_" + index;
        obj.setRights(rights);
    }
    
    public void setRightsHolder(Taxon obj, int index) {
        String rightsHolder = "rightsHolder_" + index;
        obj.setRightsHolder(rightsHolder);
    }
    
    public void setScientificName(Taxon obj, int index) {
        String scientificName = "scientificName_" + index;
        obj.setScientificName(scientificName);
    }
    
    public void setScientificNameAuthorship(Taxon obj, int index) {
        String scientificNameAuthorship = "scientificNameAuthorship_" + index;
        obj.setScientificNameAuthorship(scientificNameAuthorship);
    }
    
    public void setScientificNameID(Taxon obj, int index) {
        String scientificNameID = "scientificNameID_" + index;
        obj.setScientificNameID(scientificNameID);
    }
    
    public void setSpecificEpithet(Taxon obj, int index) {
        String specificEpithet = "specificEpithet_" + index;
        obj.setSpecificEpithet(specificEpithet);
    }
    
    public void setSubgenus(Taxon obj, int index) {
        String subgenus = "subgenus_" + index;
        obj.setSubgenus(subgenus);
    }
    
    public void setTaxonId(Taxon obj, int index) {
        String taxonId = "taxonId_" + index;
        obj.setTaxonId(taxonId);
    }
    
    public void setTaxonRank(Taxon obj, int index) {
        Rank taxonRank = Rank.class.getEnumConstants()[0];
        obj.setTaxonRank(taxonRank);
    }
    
    public void setTaxonRemarks(Taxon obj, int index) {
        String taxonRemarks = "taxonRemarks_" + index;
        obj.setTaxonRemarks(taxonRemarks);
    }
    
    public void setTaxonomicStatus(Taxon obj, int index) {
        TaxonomicStatus taxonomicStatus = TaxonomicStatus.class.getEnumConstants()[0];
        obj.setTaxonomicStatus(taxonomicStatus);
    }
    
    public void setVerbatimTaxonRank(Taxon obj, int index) {
        String verbatimTaxonRank = "verbatimTaxonRank_" + index;
        obj.setVerbatimTaxonRank(verbatimTaxonRank);
    }
    
    public Taxon getSpecificTaxon(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Taxon obj = data.get(index);
        Long id = obj.getId();
        return taxonRepository.findOne(id);
    }
    
    public Taxon getRandomTaxon() {
        init();
        Taxon obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return taxonRepository.findOne(id);
    }
    
    public boolean modifyTaxon(Taxon obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = taxonRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Taxon' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Taxon>();
        for (int i = 0; i < 10; i++) {
            Taxon obj = getNewTransientTaxon(i);
            try {
                taxonRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            taxonRepository.flush();
            data.add(obj);
        }
    }
    
    public void tearDown() {
    	taxonRepository.delete(taxonRepository.findAll());
    	data = new ArrayList<Taxon>();
    }
}
