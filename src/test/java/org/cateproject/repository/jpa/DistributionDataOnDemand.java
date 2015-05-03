package org.cateproject.repository.jpa;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.cateproject.domain.Distribution;
import org.cateproject.domain.Taxon;
import org.cateproject.domain.constants.Location;
import org.gbif.ecat.voc.EstablishmentMeans;
import org.gbif.ecat.voc.LifeStage;
import org.gbif.ecat.voc.OccurrenceStatus;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class DistributionDataOnDemand {
	@Autowired
	private TaxonDataOnDemand taxonDataOnDemand;
	
	public void setTaxon(Distribution obj, int index) {
        Taxon taxon = taxonDataOnDemand.getRandomTaxon();
        obj.setTaxon(taxon);
    }
    
    private Random rnd = new SecureRandom();
    
    private List<Distribution> data;
    
    @Autowired
    DatasetDataOnDemand datasetDataOnDemand;
    
    @Autowired
    DistributionRepository distributionRepository;
    
    public Distribution getNewTransientDistribution(int index) {
        Distribution obj = new Distribution();
        setAccessRights(obj, index);
        setContributor(obj, index);
        setCountryCode(obj, index);
        setCreated(obj, index);
        setCreator(obj, index);
        setEstablishmentMeans(obj, index);
        setIdentifier(obj, index);
        setLicense(obj, index);
        setLifeStage(obj, index);
        setLocation(obj, index);
        setLocality(obj, index);
        setModified(obj, index);
        setOccurrenceRemarks(obj, index);
        setOccurrenceStatus(obj, index);
        setRights(obj, index);
        setRightsHolder(obj, index);
        setTaxon(obj, index);
        return obj;
    }
    
    public void setAccessRights(Distribution obj, int index) {
        String accessRights = "accessRights_" + index;
        obj.setAccessRights(accessRights);
    }
    
    public void setContributor(Distribution obj, int index) {
        String contributor = "contributor_" + index;
        obj.setContributor(contributor);
    }
    
    public void setCountryCode(Distribution obj, int index) {
        String countryCode = "countryCode_" + index;
        obj.setCountryCode(countryCode);
    }
    
    public void setCreated(Distribution obj, int index) {
        DateTime created = null;
        obj.setCreated(created);
    }
    
    public void setCreator(Distribution obj, int index) {
        String creator = "creator_" + index;
        obj.setCreator(creator);
    }
    
    public void setEstablishmentMeans(Distribution obj, int index) {
        EstablishmentMeans establishmentMeans = EstablishmentMeans.class.getEnumConstants()[0];
        obj.setEstablishmentMeans(establishmentMeans);
    }
    
    public void setIdentifier(Distribution obj, int index) {
        String identifier = "identifier_" + index;
        obj.setIdentifier(identifier);
    }
    
    public void setLicense(Distribution obj, int index) {
        String license = "license_" + index;
        obj.setLicense(license);
    }
    
    public void setLifeStage(Distribution obj, int index) {
        LifeStage lifeStage = LifeStage.class.getEnumConstants()[0];
        obj.setLifeStage(lifeStage);
    }
    
    public void setLocation(Distribution obj, int index) {
        Location location = Location.class.getEnumConstants()[0];
        obj.setLocation(location);
    }
    
    public void setLocality(Distribution obj, int index) {
        String locality = "locality_" + index;
        obj.setLocality(locality);
    }
    
    public void setModified(Distribution obj, int index) {
        DateTime modified = null;
        obj.setModified(modified);
    }
    
    public void setOccurrenceRemarks(Distribution obj, int index) {
        String occurrenceRemarks = "occurrenceRemarks_" + index;
        obj.setOccurrenceRemarks(occurrenceRemarks);
    }
    
    public void setOccurrenceStatus(Distribution obj, int index) {
        OccurrenceStatus occurrenceStatus = OccurrenceStatus.class.getEnumConstants()[0];
        obj.setOccurrenceStatus(occurrenceStatus);
    }
    
    public void setRights(Distribution obj, int index) {
        String rights = "rights_" + index;
        obj.setRights(rights);
    }
    
    public void setRightsHolder(Distribution obj, int index) {
        String rightsHolder = "rightsHolder_" + index;
        obj.setRightsHolder(rightsHolder);
    }
    
    public Distribution getSpecificDistribution(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Distribution obj = data.get(index);
        Long id = obj.getId();
        return distributionRepository.findOne(id);
    }
    
    public Distribution getRandomDistribution() {
        init();
        Distribution obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return distributionRepository.findOne(id);
    }
    
    public boolean modifyDistribution(Distribution obj) {
        return false;
    }
    
    public void init() {
        int from = 0;
        int to = 10;
        data = distributionRepository.findAll(new org.springframework.data.domain.PageRequest(from / to, to)).getContent();
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Distribution' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Distribution>();
        for (int i = 0; i < 10; i++) {
            Distribution obj = getNewTransientDistribution(i);
            try {
                distributionRepository.save(obj);
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            distributionRepository.flush();
            data.add(obj);
        }
    }
}
