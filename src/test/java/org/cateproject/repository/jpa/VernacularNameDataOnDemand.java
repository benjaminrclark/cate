package org.cateproject.repository.jpa;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.domain.Taxon;
import org.cateproject.domain.VernacularName;
import org.cateproject.domain.constants.Location;
import org.cateproject.domain.constants.Sex;
import org.gbif.ecat.voc.LifeStage;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class VernacularNameDataOnDemand {
	
private Random rnd = new SecureRandom();
    
    private List<VernacularName> data;
    
    @Autowired
    DatasetDataOnDemand datasetDataOnDemand;
    
    @Autowired
    VernacularNameRepository vernacularNameRepository;
    
    public VernacularName getNewTransientVernacularName(int index) {
        VernacularName obj = new VernacularName();
        setAccessRights(obj, index);
        setContributor(obj, index);
        setCountryCode(obj, index);
        setCreated(obj, index);
        setCreator(obj, index);
        setIdentifier(obj, index);
        setLang(obj, index);
        setLicense(obj, index);
        setLifeStage(obj, index);
        setLocality(obj, index);
        setLocation(obj, index);
        setModified(obj, index);
        setOrganismPart(obj, index);
        setPlural(obj, index);
        setPreferredName(obj, index);
        setRights(obj, index);
        setRightsHolder(obj, index);
        setSex(obj, index);
        setSource(obj, index);
        setTaxon(obj, index);
        setTaxonRemarks(obj, index);
        setTemporal(obj, index);
        setVernacularName(obj, index);
        return obj;
    }
    
    public void setAccessRights(VernacularName obj, int index) {
        String accessRights = "accessRights_" + index;
        obj.setAccessRights(accessRights);
    }
    
    public void setContributor(VernacularName obj, int index) {
        String contributor = "contributor_" + index;
        obj.setContributor(contributor);
    }
    
    public void setCountryCode(VernacularName obj, int index) {
        String countryCode = "countryCode_" + index;
        obj.setCountryCode(countryCode);
    }
    
    public void setCreated(VernacularName obj, int index) {
        DateTime created = null;
        obj.setCreated(created);
    }
    
    public void setCreator(VernacularName obj, int index) {
        String creator = "creator_" + index;
        obj.setCreator(creator);
    }
    
    public void setIdentifier(VernacularName obj, int index) {
        String identifier = "identifier_" + index;
        obj.setIdentifier(identifier);
    }
    
    public void setLang(VernacularName obj, int index) {
        Locale lang = null;
        obj.setLang(lang);
    }
    
    public void setLicense(VernacularName obj, int index) {
        String license = "license_" + index;
        obj.setLicense(license);
    }
    
    public void setLifeStage(VernacularName obj, int index) {
        LifeStage lifeStage = LifeStage.class.getEnumConstants()[0];
        obj.setLifeStage(lifeStage);
    }
    
    public void setLocality(VernacularName obj, int index) {
        String locality = "locality_" + index;
        obj.setLocality(locality);
    }
    
    public void setLocation(VernacularName obj, int index) {
        Location location = Location.class.getEnumConstants()[0];
        obj.setLocation(location);
    }
    
    public void setModified(VernacularName obj, int index) {
        DateTime modified = null;
        obj.setModified(modified);
    }
    
    public void setOrganismPart(VernacularName obj, int index) {
        String organismPart = "organismPart_" + index;
        obj.setOrganismPart(organismPart);
    }
    
    public void setPlural(VernacularName obj, int index) {
        Boolean plural = Boolean.TRUE;
        obj.setPlural(plural);
    }
    
    public void setPreferredName(VernacularName obj, int index) {
        Boolean preferredName = Boolean.TRUE;
        obj.setPreferredName(preferredName);
    }
    
    public void setRights(VernacularName obj, int index) {
        String rights = "rights_" + index;
        obj.setRights(rights);
    }
    
    public void setRightsHolder(VernacularName obj, int index) {
        String rightsHolder = "rightsHolder_" + index;
        obj.setRightsHolder(rightsHolder);
    }
    
    public void setSex(VernacularName obj, int index) {
        Sex sex = Sex.class.getEnumConstants()[0];
        obj.setSex(sex);
    }
    
    public void setSource(VernacularName obj, int index) {
        String source = "source_" + index;
        obj.setSource(source);
    }
    
    public void setTaxonRemarks(VernacularName obj, int index) {
        String taxonRemarks = "taxonRemarks_" + index;
        obj.setTaxonRemarks(taxonRemarks);
    }
    
    public void setTemporal(VernacularName obj, int index) {
        String temporal = "temporal_" + index;
        obj.setTemporal(temporal);
    }
    
    public void setVernacularName(VernacularName obj, int index) {
        String vernacularName = "vernacularName_" + index;
        obj.setVernacularName(vernacularName);
    }
    
    public VernacularName getSpecificVernacularName(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        VernacularName obj = data.get(index);
        Long id = obj.getId();
        return vernacularNameRepository.findOne(id);
    }
    
    public VernacularName getRandomVernacularName() {
        init();
        VernacularName obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return vernacularNameRepository.findOne(id);
    }
    
    public boolean modifyVernacularName(VernacularName obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = vernacularNameRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'VernacularName' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<VernacularName>();
        for (int i = 0; i < 10; i++) {
            VernacularName obj = getNewTransientVernacularName(i);
            try {
                vernacularNameRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            vernacularNameRepository.flush();
            data.add(obj);
        }
    }
	
	@Autowired
	private TaxonDataOnDemand taxonDataOnDemand;
	
	public void setTaxon(VernacularName obj, int index) {
        Taxon taxon = taxonDataOnDemand.getRandomTaxon();
        obj.setTaxon(taxon);
    }
}
