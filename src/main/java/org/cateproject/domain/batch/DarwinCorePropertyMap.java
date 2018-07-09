package org.cateproject.domain.batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.cateproject.domain.Base;
import org.cateproject.domain.Dataset;
import org.cateproject.domain.Description;
import org.cateproject.domain.Distribution;
import org.cateproject.domain.Multimedia;
import org.cateproject.domain.MeasurementOrFact;
import org.cateproject.domain.Node;
import org.cateproject.domain.Reference;
import org.cateproject.domain.Taxon;
import org.cateproject.domain.TypeAndSpecimen;
import org.cateproject.domain.VernacularName;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.UnknownTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DarwinCorePropertyMap {
	
   private static Logger logger = LoggerFactory.getLogger(DarwinCorePropertyMap.class);
	
   public static Map<Term, String> baseTerms = new HashMap<Term,String>();
	
   public static Map<Term,String> taxonTerms = new HashMap<Term,String>();
   
   public static Set<Term> requiredTaxonTerms = new HashSet<Term>();
    
   public static Map<Term,String> distributionTerms = new HashMap<Term,String>();
   
   public static Set<Term> requiredDistributionTerms = new HashSet<Term>();
    
   public static Map<Term,String> descriptionTerms = new HashMap<Term,String>();
   
   public static Set<Term> requiredDescriptionTerms = new HashSet<Term>();
    
   public static Map<Term,String> referenceTerms = new HashMap<Term,String>();
   
   public static Set<Term> requiredReferenceTerms = new HashSet<Term>();
    
   public static Map<Term,String> multimediaTerms = new HashMap<Term,String>();
   
   public static Set<Term> requiredMultimediaTerms = new HashSet<Term>();
   
   public static Map<Term,String> typeAndSpecimenTerms = new HashMap<Term,String>();
   
   public static Set<Term> requiredTypeAndSpecimenTerms = new HashSet<Term>();
   
   public static Map<Term,String> vernacularNameTerms = new HashMap<Term,String>();
   
   public static Set<Term> requiredVernacularNameTerms = new HashSet<Term>();
   
   public static Map<Term,String> measurementOrFactTerms = new HashMap<Term,String>();
   
   public static Set<Term> requiredMeasurementOrFactTerms = new HashSet<Term>();
   
   public static Map<Term,String> nodeTerms = new HashMap<Term,String>();
   
   public static Set<Term> requiredNodeTerms = new HashSet<Term>();
   
   public static Map<Term,String> datasetTerms = new HashMap<Term,String>();
   
   public static Set<Term> requiredDatasetTerms = new HashSet<Term>();
   
   public static Map<Term,String> termTerms = new HashMap<Term,String>();
   
   public static Set<Term> requiredTermTerms = new HashSet<Term>();
   
   public static Map<Term,Map<Term,String>> allowedExtensions = new HashMap<Term,Map<Term,String>>();
   
   public static Map<Term,Set<Term>> requiredExtensions = new HashMap<Term,Set<Term>>();
   
   private static TermFactory termFactory = new TermFactory();
   
   static {
	    baseTerms.put(DcTerm.creator, "creator");
	    baseTerms.put(DcTerm.contributor, "contributor");
	    baseTerms.put(DcTerm.created, "created");
	    baseTerms.put(DcTerm.modified, "modified");
	    baseTerms.put(DcTerm.accessRights, "accessRights");
	    baseTerms.put(DcTerm.rights, "rights");
	    baseTerms.put(DcTerm.rightsHolder, "rightsHolder");
	    baseTerms.put(DcTerm.license, "license");
	    baseTerms.put(DwcTerm.datasetID,"dataset.identifier");
   	    baseTerms.put(DwcTerm.datasetName,"dataset.title");
	   
	    taxonTerms.putAll(baseTerms);
   	    taxonTerms.put(DwcTerm.taxonID, "taxonId");
   	    taxonTerms.put(DwcTerm.acceptedNameUsage,"acceptedNameUsage.scientificName");
   	    taxonTerms.put(DwcTerm.acceptedNameUsageID,"acceptedNameUsage.taxonId");
   	    taxonTerms.put(DcTerm.bibliographicCitation,"bibliographicCitation");
   	    taxonTerms.put(DwcTerm.class_,"clazz");
   	    taxonTerms.put(DwcTerm.family,"family");
   	    taxonTerms.put(DwcTerm.genus,"genus");
   	    taxonTerms.put(DwcTerm.infraspecificEpithet,"infraspecificEpithet");
   	    taxonTerms.put(DwcTerm.kingdom,"kingdom");
   	    taxonTerms.put(DwcTerm.nameAccordingTo,"nameAccordingTo");
   	    taxonTerms.put(DwcTerm.namePublishedInID,"namePublishedIn.identifier");
   	    taxonTerms.put(DwcTerm.namePublishedIn,"namePublishedInString");
   	    taxonTerms.put(DwcTerm.namePublishedInYear,"namePublishedInYear");
   	    taxonTerms.put(DwcTerm.nomenclaturalCode,"nomenclaturalCode");
   	    taxonTerms.put(DwcTerm.nomenclaturalStatus,"nomenclaturalStatus");
   	    taxonTerms.put(DwcTerm.order,"ordr");
   	    taxonTerms.put(DwcTerm.originalNameUsageID,"originalNameUsage.taxonId");
   	    taxonTerms.put(DwcTerm.originalNameUsage,"originalNameUsage.scientificName");
   	    taxonTerms.put(DwcTerm.parentNameUsageID,"parentNameUsage.taxonId");
   	    taxonTerms.put(DwcTerm.parentNameUsage,"parentNameUsage.scientificName");
   	    taxonTerms.put(DwcTerm.phylum,"phylum");
   	    taxonTerms.put(DwcTerm.scientificName,"scientificName");
   	    taxonTerms.put(DwcTerm.scientificNameAuthorship,"scientificNameAuthorship");
   	    taxonTerms.put(DwcTerm.scientificNameID,"scientificNameID");
   	    taxonTerms.put(DcTerm.references,"references");
   	    taxonTerms.put(DwcTerm.specificEpithet,"specificEpithet");
   	    taxonTerms.put(DwcTerm.subgenus,"subgenus");
   	    taxonTerms.put(DwcTerm.taxonomicStatus,"taxonomicStatus");
   	    taxonTerms.put(DwcTerm.taxonRank,"taxonRank");
   	    taxonTerms.put(DwcTerm.taxonRemarks,"taxonRemarks");
   	    taxonTerms.put(DwcTerm.verbatimTaxonRank,"verbatimTaxonRank");
   	   
   	    requiredTaxonTerms.add(DwcTerm.taxonID);
   	    requiredTaxonTerms.add(DwcTerm.scientificName);
   	
   	    distributionTerms.putAll(baseTerms);
   	    distributionTerms.put(DcTerm.identifier, "identifier");
   	    distributionTerms.put(DwcTerm.taxonID, "taxon.taxonId");
   	    distributionTerms.put(DwcTerm.locality, "loclity");
   	    distributionTerms.put(DwcTerm.locationID, "location");
            distributionTerms.put(DwcTerm.occurrenceRemarks, "occurrenceRemarks");
   	    distributionTerms.put(DwcTerm.occurrenceStatus, "occurrenceStatus");
   	    distributionTerms.put(DwcTerm.establishmentMeans, "establishmentMeans");
   	   
            requiredDistributionTerms.add(DcTerm.identifier);
   	    requiredDistributionTerms.add(DwcTerm.taxonID);
   	    requiredDistributionTerms.add(DwcTerm.locationID);
   	
   	    descriptionTerms.putAll(baseTerms);
  	    descriptionTerms.put(DcTerm.identifier, "identifier");
            descriptionTerms.put(DwcTerm.taxonID, "taxon.taxonId");
   	    descriptionTerms.put(DcTerm.audience, "audience");
   	    descriptionTerms.put(DcTerm.description, "dscription");
	    descriptionTerms.put(DcTerm.language, "language");
   	    descriptionTerms.put(DcTerm.source, "source");
   	    descriptionTerms.put(DcTerm.type, "type");
   	   
            requiredDescriptionTerms.add(DcTerm.identifier);
   	    requiredDescriptionTerms.add(DwcTerm.taxonID);
   	    requiredDescriptionTerms.add(DcTerm.description);
   	    requiredDescriptionTerms.add(DcTerm.type);
   	
   	    referenceTerms.putAll(baseTerms);
   	    referenceTerms.put(DcTerm.identifier, "identifier");
   	    referenceTerms.put(DwcTerm.taxonID, "taxa[0].taxonId");
   	    referenceTerms.put(DcTerm.bibliographicCitation, "bibliographicCitation");
   	    referenceTerms.put(DcTerm.date, "date");
   	    referenceTerms.put(DcTerm.description, "description");
   	    referenceTerms.put(DcTerm.language, "language");
   	    referenceTerms.put(DcTerm.source, "source");
   	    referenceTerms.put(DcTerm.subject, "subject");
   	    referenceTerms.put(DwcTerm.taxonRemarks, "taxonRemarks");
   	    referenceTerms.put(DcTerm.title, "title");
   	    referenceTerms.put(DcTerm.type, "type");
   	   
            requiredReferenceTerms.add(DcTerm.identifier); 
   	    requiredReferenceTerms.add(DcTerm.bibliographicCitation);
   	
            multimediaTerms.putAll(baseTerms);
            multimediaTerms.put(DcTerm.identifier, "identifier");
   	    multimediaTerms.put(DwcTerm.taxonID, "taxa[0].taxonId");
   	    multimediaTerms.put(DcTerm.audience, "audience");
   	    multimediaTerms.put(DcTerm.description, "description");
   	    multimediaTerms.put(DcTerm.format, "format");
            multimediaTerms.put(DcTerm.type, "type");
     	    multimediaTerms.put(Wgs84Term.latitude, "latitude");
    	    multimediaTerms.put(Wgs84Term.longitude, "longitude");
    	    multimediaTerms.put(DcTerm.spatial, "locality");
   	    multimediaTerms.put(DcTerm.publisher, "publisher");
   	    multimediaTerms.put(DcTerm.references, "references");
   	    multimediaTerms.put(DcTerm.subject, "subject");
   	    multimediaTerms.put(DcTerm.title, "title");
   	    multimediaTerms.put(SddTerm.termID, "term");
   	    
   	    requiredMultimediaTerms.add(DcTerm.identifier);
   	
            measurementOrFactTerms.putAll(baseTerms);
   	    measurementOrFactTerms.put(DwcTerm.measurementID, "identifier");
   	    measurementOrFactTerms.put(DwcTerm.taxonID, "taxon.taxonId");
   	    measurementOrFactTerms.put(DwcTerm.measurementAccuracy, "accuracy");
   	    measurementOrFactTerms.put(DwcTerm.measurementDeterminedBy, "determinedBy");
   	    measurementOrFactTerms.put(DwcTerm.measurementDeterminedDate, "determinedDate");
   	    measurementOrFactTerms.put(DwcTerm.measurementMethod, "mthod");
   	    measurementOrFactTerms.put(DwcTerm.measurementRemarks, "remarks");
   	    measurementOrFactTerms.put(DwcTerm.measurementType, "type.identifier");
   	    measurementOrFactTerms.put(DwcTerm.measurementUnit, "unit");
   	    measurementOrFactTerms.put(DwcTerm.measurementValue, "valu");
   	   
            requiredMeasurementOrFactTerms.add(DcTerm.identifier); 
   	    requiredMeasurementOrFactTerms.add(DwcTerm.taxonID);
   	    requiredMeasurementOrFactTerms.add(DwcTerm.measurementValue);
   	    requiredMeasurementOrFactTerms.add(DwcTerm.measurementType);
   	
   	    vernacularNameTerms.putAll(baseTerms);
   	    vernacularNameTerms.put(DcTerm.identifier, "identifier");
   	    vernacularNameTerms.put(DwcTerm.taxonID, "taxon.taxonId");
   	    vernacularNameTerms.put(DwcTerm.countryCode, "countryCode");
   	    vernacularNameTerms.put(DcTerm.language, "lang");
   	    vernacularNameTerms.put(DwcTerm.lifeStage, "lifeStage");
   	    vernacularNameTerms.put(DwcTerm.locality, "locality");
   	    vernacularNameTerms.put(DwcTerm.locationID, "location");
   	    vernacularNameTerms.put(GbifTerm.organismPart, "organismPart");
   	    vernacularNameTerms.put(GbifTerm.isPlural, "plural");
   	    vernacularNameTerms.put(GbifTerm.isPreferredName, "preferredName");
   	    vernacularNameTerms.put(DwcTerm.sex, "sex");
   	    vernacularNameTerms.put(DcTerm.source, "source");
   	    vernacularNameTerms.put(DwcTerm.taxonRemarks, "taxonRemarks");
   	    vernacularNameTerms.put(DcTerm.temporal, "temporal");
   	    vernacularNameTerms.put(DwcTerm.vernacularName, "vernacularName");
   	   
            requiredVernacularNameTerms.add(DcTerm.identifier); 
   	    requiredVernacularNameTerms.add(DwcTerm.taxonID);
   	    requiredVernacularNameTerms.add(DwcTerm.vernacularName);

   	    typeAndSpecimenTerms.putAll(baseTerms);
   	    typeAndSpecimenTerms.put(DcTerm.identifier, "identifier");
   	    typeAndSpecimenTerms.put(DwcTerm.taxonID, "taxa[0].taxonId");
   	    typeAndSpecimenTerms.put(DcTerm.bibliographicCitation, "bibliographicCitation");
   	    typeAndSpecimenTerms.put(DwcTerm.catalogNumber, "catalogNumber");
   	    typeAndSpecimenTerms.put(DwcTerm.collectionCode, "collectionCode");
   	    typeAndSpecimenTerms.put(DwcTerm.decimalLatitude, "decimalLatitude");
   	    typeAndSpecimenTerms.put(DwcTerm.decimalLongitude, "decimalLongitude");
   	    typeAndSpecimenTerms.put(DwcTerm.institutionCode, "institutionCode");
   	    typeAndSpecimenTerms.put(DwcTerm.locality, "locality");
   	    typeAndSpecimenTerms.put(DwcTerm.locationID, "location");
   	    typeAndSpecimenTerms.put(DwcTerm.recordedBy, "recordedBy");
   	    typeAndSpecimenTerms.put(DwcTerm.scientificName, "scientificName");
   	    typeAndSpecimenTerms.put(DwcTerm.sex, "sex");
   	    typeAndSpecimenTerms.put(DcTerm.source, "source");
   	    typeAndSpecimenTerms.put(DwcTerm.taxonRank, "taxonRank");
   	    typeAndSpecimenTerms.put(GbifTerm.typeDesignatedBy, "typeDesignatedBy");
   	    typeAndSpecimenTerms.put(GbifTerm.typeDesignationType, "typeDesignationType");
   	    typeAndSpecimenTerms.put(DwcTerm.typeStatus, "typeStatus");
   	    typeAndSpecimenTerms.put(DwcTerm.verbatimEventDate, "verbatimEventDate");
	    typeAndSpecimenTerms.put(GbifTerm.verbatimLabel, "verbatimLabel");
	    typeAndSpecimenTerms.put(DwcTerm.verbatimLatitude, "verbatimLatitude");
	    typeAndSpecimenTerms.put(DwcTerm.verbatimLongitude, "verbatimLongitude");
	    
	    requiredTypeAndSpecimenTerms.add(DcTerm.identifier);
	    
	    nodeTerms.putAll(baseTerms);
   	    nodeTerms.put(DcTerm.identifier, "identifier");
   	    nodeTerms.put(SddTerm.inapplicableIfID, "inapplicableIf");
   	    nodeTerms.put(SddTerm.applicableIfID, "applicableIf");
   	    nodeTerms.put(SddTerm.parameters, "parameters");
   	    nodeTerms.put(SddTerm.termID, "term.identifier");
   	    nodeTerms.put(SddTerm.parentID, "parent.identifier");
   	    
   	    requiredNodeTerms.add(DcTerm.identifier);
   	    requiredNodeTerms.add(DwcTerm.datasetID);
   	    
     	    datasetTerms.putAll(baseTerms);
     	    datasetTerms.put(DwcTerm.taxonID, "taxa[0].taxonId");
	    datasetTerms.put(DcTerm.identifier, "identifier");
	    datasetTerms.put(DcTerm.description, "description");
	    datasetTerms.put(DcTerm.title, "title");
	    datasetTerms.put(SddTerm.parameters, "parameters");
	    
	    requiredDatasetTerms.add(DcTerm.identifier);
	    
	    termTerms.putAll(baseTerms);
     	    termTerms.put(DwcTerm.taxonID, "taxa[0].taxonId");
	    termTerms.put(DcTerm.identifier, "identifier");
	    termTerms.put(DcTerm.description, "description");
	    termTerms.put(DcTerm.title, "title");
	    termTerms.put(DcTerm.type, "type");
	    termTerms.put(DwcTerm.measurementUnit, "unit");
	    termTerms.put(SddTerm.termID, "charactr.identifier");
	    
	    requiredTermTerms.add(DcTerm.identifier);
	    
	    allowedExtensions.put(DwcTerm.Taxon, taxonTerms);
	    allowedExtensions.put(GbifTerm.Description, descriptionTerms);
	    allowedExtensions.put(GbifTerm.Distribution, distributionTerms);
	    allowedExtensions.put(GbifTerm.Multimedia, multimediaTerms);
	    allowedExtensions.put(GbifTerm.TypesAndSpecimen, typeAndSpecimenTerms);
	    allowedExtensions.put(DwcTerm.MeasurementOrFact, measurementOrFactTerms);
	    allowedExtensions.put(GbifTerm.Reference, referenceTerms);
	    allowedExtensions.put(GbifTerm.VernacularName, vernacularNameTerms);
	    allowedExtensions.put(SddTerm.Node, nodeTerms);
	    allowedExtensions.put(SddTerm.Dataset, datasetTerms);
	    allowedExtensions.put(SkosTerm.Concept, termTerms);
	    
	    requiredExtensions.put(DwcTerm.Taxon, requiredTaxonTerms);
	    requiredExtensions.put(GbifTerm.Description, requiredDescriptionTerms);
	    requiredExtensions.put(GbifTerm.Distribution, requiredDistributionTerms);
	    requiredExtensions.put(GbifTerm.Multimedia, requiredMultimediaTerms);
	    requiredExtensions.put(GbifTerm.TypesAndSpecimen, requiredTypeAndSpecimenTerms);
	    requiredExtensions.put(DwcTerm.MeasurementOrFact, requiredMeasurementOrFactTerms);
	    requiredExtensions.put(GbifTerm.Reference, requiredReferenceTerms);
	    requiredExtensions.put(GbifTerm.VernacularName, requiredVernacularNameTerms);
	    requiredExtensions.put(SddTerm.Node, requiredNodeTerms);
	    requiredExtensions.put(SddTerm.Dataset, requiredDatasetTerms);
	    requiredExtensions.put(SkosTerm.Concept, requiredTermTerms);
   	
   }

    public static Map<Term, String> getPropertyMap(Term term) {
	    if(term.equals(DwcTerm.Taxon)) {
	    	return taxonTerms;
	    } else if(term.equals(GbifTerm.Description)) {
	    	return descriptionTerms;
	    } else if(term.equals(GbifTerm.Distribution)) {
	    	return distributionTerms;
	    } else if(term.equals(DwcTerm.MeasurementOrFact)) {
	    	return measurementOrFactTerms;
	    } else if(term.equals(GbifTerm.VernacularName)) {
	    	return vernacularNameTerms;
	    } else if(term.equals(GbifTerm.Multimedia)) {
	    	return multimediaTerms;
	    } else if(term.equals(GbifTerm.TypesAndSpecimen)) {
	    	return typeAndSpecimenTerms;
	    } else if(term.equals(GbifTerm.Reference)) {
	    	return referenceTerms;
	    } else if(term.equals(SkosTerm.Concept)) {
	    	return termTerms;
	    } else if(term.equals(SddTerm.Dataset)) {
	    	return datasetTerms;
	    } else if(term.equals(SddTerm.Node)) {
	    	return nodeTerms;
	    }else {
	    	throw new IllegalArgumentException(term.qualifiedName() + " is not a supported term");
	    }
    }
    
    public static SortedSet<Term> getTerms(Term term) {
    	SortedSet<Term> terms = new TreeSet<Term>();
	    if(term.equals(DwcTerm.Taxon)) {
	    	terms.addAll(taxonTerms.keySet());
	    } else if(term.equals(GbifTerm.Description)) {
	    	terms.addAll(descriptionTerms.keySet());
	    } else if(term.equals(GbifTerm.Distribution)) {
	    	terms.addAll(distributionTerms.keySet());
	    }  else if(term.equals(DwcTerm.MeasurementOrFact)) {
	    	terms.addAll(measurementOrFactTerms.keySet());
	    } else if(term.equals(GbifTerm.VernacularName)) {
	    	terms.addAll(vernacularNameTerms.keySet());
	    } else if(term.equals(GbifTerm.Multimedia)) {
	    	terms.addAll(multimediaTerms.keySet());
	    } else if(term.equals(GbifTerm.TypesAndSpecimen)) {
	    	terms.addAll(typeAndSpecimenTerms.keySet());
	    } else if(term.equals(GbifTerm.Reference)) {
	    	terms.addAll(referenceTerms.keySet());
	    } else if(term.equals(SddTerm.Node)) {
	    	terms.addAll(nodeTerms.keySet());
	    } else if(term.equals(SddTerm.Dataset)) {
	    	terms.addAll(datasetTerms.keySet());
	    } else if(term.equals(SkosTerm.Concept)) {
	    	terms.addAll(termTerms.keySet());
	    } else {
	    	throw new IllegalArgumentException(term.qualifiedName() + " is not a supported term");
	    }
	    return terms;
    }

	public static Set<Term> getPossibleExtensions(String[] fields) {
		Set<Term> terms = new HashSet<Term>();
		for(String field : fields) {
			Term term = termFactory.findTerm(field);
			if(!(term instanceof UnknownTerm)) {
			    terms.add(term);
			} else {
				logger.info(term.qualifiedName() + " is unknown");
			}
		}
		Set<Term> possibleExtensions = new HashSet<Term>();
		
		for(Term extension : allowedExtensions.keySet()) {
			logger.info("Checking " + extension);
			logger.info("Extension contains terms? " + allowedExtensions.get(extension).keySet().containsAll(terms) + " extension contains required terms? " + terms.containsAll(requiredExtensions.get(extension)));
			if(allowedExtensions.get(extension).keySet().containsAll(terms) && terms.containsAll(requiredExtensions.get(extension))) {
				possibleExtensions.add(extension);
			}
		}
	
		return possibleExtensions;
	}

	public static List<String> getAllowedExtensions() {
		List<String> extensions = new ArrayList<String>();
		for(Term term : allowedExtensions.keySet()) {
			extensions.add(term.toString());
		}
		return extensions;
	}

	public static Class<? extends Base> getClazz(String extension) {
		if(extension.equals("dwc:Taxon")) {
			return Taxon.class;
		} else if(extension.equals("sdd:Dataset")) {
			return Dataset.class;
		} else if(extension.equals("gbif:Description")) {
			return Description.class;
		} else if(extension.equals("gbif:Distribution")) {
			return Distribution.class;
		} else if(extension.equals("gbif:Multimedia")) {
			return Multimedia.class;
		} else if(extension.equals("gbif:Image")) {
			return Multimedia.class;
		} else if(extension.equals("dwc:MeasurementOrFact")) {
			return MeasurementOrFact.class;
		} else if(extension.equals("sdd:Node")) {
			return Node.class;
		} else if(extension.equals("gbif:Reference")) {
			return Reference.class;
		} else if(extension.equals("skos:Concept")) {
			return org.cateproject.domain.Term.class;
		} else if(extension.equals("gbif:TypeAndSpecimen")) {
			return TypeAndSpecimen.class;
		} else if(extension.equals("gbif:VernacularName")) {
			return VernacularName.class;
		} else {
		    throw new IllegalArgumentException(extension + " cannot be mapped to a domain object");
		}
	}

	public static List<String> getFields(Term term) {
		if(allowedExtensions.containsKey(term)) {
			return new ArrayList<String>(allowedExtensions.get(term).values());
		} else {
		    throw new IllegalArgumentException(term.toString() + " is not an allowed extension");
		}
	}
}
