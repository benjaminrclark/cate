package org.cateproject.domain.batch;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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

import org.junit.Before;
import org.junit.Test;

public class DarwinCorePropertyMapTest {

    DarwinCorePropertyMap darwinCorePropertyMap;  


    @Before
    public void setUp() throws Exception {
        darwinCorePropertyMap = new DarwinCorePropertyMap();
    }

    @Test
    public void testGetFields() {

       assertThat("darwinCorePropertyMap should contain the expected fields for dwc:Taxon", 
         darwinCorePropertyMap.getFields(DwcTerm.Taxon),
         containsInAnyOrder(
          "creator",
          "contributor",
          "created",
          "modified",
          "rightsHolder",
          "license",
          "rights",
          "accessRights",
          "dataset.title",
          "dataset.identifier",
          "nomenclaturalCode",
          "references",
          "nomenclaturalStatus",
          "phylum",
          "parentNameUsage.scientificName",
          "parentNameUsage.taxonId",
          "namePublishedInYear",
          "scientificNameAuthorship",
          "scientificName",
          "acceptedNameUsage.scientificName",
          "taxonRemarks",
          "originalNameUsage.scientificName",
          "namePublishedInString",
          "taxonomicStatus",
          "family",
          "kingdom",
          "specificEpithet",
          "infraspecificEpithet",
          "scientificNameID",
          "verbatimTaxonRank",
          "originalNameUsage.taxonId",
          "bibliographicCitation",
          "namePublishedIn.identifier",
          "ordr",
          "nameAccordingTo",
          "subgenus",
          "taxonRank",
          "acceptedNameUsage.taxonId",
          "genus",
          "clazz",
          "taxonId"
        ));

       assertThat("darwinCorePropertyMap should contain the expected fields for gbif:Distribution", 
         darwinCorePropertyMap.getFields(GbifTerm.Distribution),
         containsInAnyOrder(
          "creator",
          "contributor",
          "created",
          "modified",
          "rightsHolder",
          "license",
          "rights",
          "accessRights",
          "dataset.title",
          "dataset.identifier",
          "identifier",
          "taxon.taxonId",
          "loclity",
          "location",
          "occurrenceRemarks",
          "occurrenceStatus",
          "establishmentMeans"
          ));

       assertThat("darwinCorePropertyMap should contain the expected fields for gbif:Description", 
         darwinCorePropertyMap.getFields(GbifTerm.Description),
         containsInAnyOrder(
          "creator",
          "contributor",
          "created",
          "modified",
          "rightsHolder",
          "license",
          "rights",
          "accessRights",
          "dataset.title",
          "dataset.identifier",
          "identifier",
          "taxon.taxonId",
          "audience",
          "dscription",
          "language",
          "source",
          "type"
          ));

       assertThat("darwinCorePropertyMap should contain the expected fields for gbif:Reference", 
         darwinCorePropertyMap.getFields(GbifTerm.Reference),
         containsInAnyOrder(
          "creator",
          "contributor",
          "created",
          "modified",
          "rightsHolder",
          "license",
          "rights",
          "accessRights",
          "dataset.title",
          "dataset.identifier",
          "identifier",
          "taxa[0].taxonId",
          "bibliographicCitation",
          "date",
          "description",
          "language",
          "source",
          "subject",
          "taxonRemarks",
          "title",
          "type"
          ));

       assertThat("darwinCorePropertyMap should contain the expected fields for gbif:Multimedia", 
         darwinCorePropertyMap.getFields(GbifTerm.Multimedia),
         containsInAnyOrder(
          "creator",
          "contributor",
          "created",
          "modified",
          "rightsHolder",
          "license",
          "rights",
          "accessRights",
          "dataset.title",
          "dataset.identifier",
          "identifier",
          "taxa[0].taxonId",
          "audience",
          "description",
          "format",
          "type",
          "latitude",
          "longitude",
          "locality",
          "publisher",
          "references",
          "subject",
          "title",
          "term"
          ));

       assertThat("darwinCorePropertyMap should contain the expected fields for dwc:MeasurementOrFact", 
         darwinCorePropertyMap.getFields(DwcTerm.MeasurementOrFact),
         containsInAnyOrder(
          "creator",
          "contributor",
          "created",
          "modified",
          "rightsHolder",
          "license",
          "rights",
          "accessRights",
          "dataset.title",
          "dataset.identifier",
          "identifier",
          "taxon.taxonId",
          "accuracy",
          "determinedBy",
          "determinedDate",
          "mthod",
          "remarks",
          "type.identifier",
          "unit",
          "valu"
          ));

       assertThat("darwinCorePropertyMap should contain the expected fields for gbif:VernacularName", 
         darwinCorePropertyMap.getFields(GbifTerm.VernacularName),
         containsInAnyOrder(
          "creator",
          "contributor",
          "created",
          "modified",
          "rightsHolder",
          "license",
          "rights",
          "accessRights",
          "dataset.title",
          "dataset.identifier",
          "identifier",
          "taxon.taxonId",
          "countryCode",
          "lang",
          "lifeStage",
          "locality",
          "location",
          "organismPart",
          "plural",
          "preferredName",
          "sex",
          "source",
          "taxonRemarks",
          "temporal",
          "vernacularName"
          ));

       assertThat("darwinCorePropertyMap should contain the expected fields for gbif:TypesAndSpecimen", 
         darwinCorePropertyMap.getFields(GbifTerm.TypesAndSpecimen),
         containsInAnyOrder(
          "creator",
          "contributor",
          "created",
          "modified",
          "rightsHolder",
          "license",
          "rights",
          "accessRights",
          "dataset.title",
          "dataset.identifier",
          "identifier",
          "taxa[0].taxonId",
          "bibliographicCitation",
          "catalogNumber",
          "collectionCode",
          "decimalLatitude",
          "decimalLongitude",
          "institutionCode",
          "locality",
          "location",
          "recordedBy",
          "scientificName",
          "sex",
          "source",
          "taxonRank",
          "typeDesignatedBy",
          "typeDesignationType",
          "typeStatus",
          "verbatimEventDate",
          "verbatimLabel",
          "verbatimLatitude",
          "verbatimLongitude"
          ));

       assertThat("darwinCorePropertyMap should contain the expected fields for sdd:Node", 
         darwinCorePropertyMap.getFields(SddTerm.Node),
         containsInAnyOrder(
          "creator",
          "contributor",
          "created",
          "modified",
          "rightsHolder",
          "license",
          "rights",
          "accessRights",
          "dataset.title",
          "dataset.identifier",
          "identifier",
          "inapplicableIf",
          "applicableIf",
          "parameters",
          "term.identifier",
          "parent.identifier"
          ));

       assertThat("darwinCorePropertyMap should contain the expected fields for sdd:Dataset", 
         darwinCorePropertyMap.getFields(SddTerm.Dataset),
         containsInAnyOrder(
          "creator",
          "contributor",
          "created",
          "modified",
          "rightsHolder",
          "license",
          "rights",
          "accessRights",
          "dataset.title",
          "dataset.identifier",
          "identifier",
          "taxa[0].taxonId",
          "description",
          "title",
          "parameters"
          ));

       assertThat("darwinCorePropertyMap should contain the expected fields for skos:Concept", 
         darwinCorePropertyMap.getFields(SkosTerm.Concept),
         containsInAnyOrder(
          "creator",
          "contributor",
          "created",
          "modified",
          "rightsHolder",
          "license",
          "rights",
          "accessRights",
          "dataset.title",
          "dataset.identifier",
          "identifier",
          "taxa[0].taxonId",
          "description",
          "title",
          "type",
          "unit",
          "charactr.identifier"
          ));

       boolean thrown = false;

       try {
         darwinCorePropertyMap.getFields(DcTerm.creator);
       } catch (IllegalArgumentException iae) {
         thrown = true;
       }

       assertTrue("darwinCorePropertyMap should throw an illegal argument exception for an unexpected term", thrown);
    }

    @Test
    public void testGetTerms() {

        Set<Term> taxonTerms = new HashSet<Term>();
        taxonTerms.add(DcTerm.creator);
        taxonTerms.add(DcTerm.contributor);
        taxonTerms.add(DcTerm.created);
        taxonTerms.add(DcTerm.modified);
        taxonTerms.add(DcTerm.rightsHolder);
        taxonTerms.add(DcTerm.license);
        taxonTerms.add(DcTerm.rights);
        taxonTerms.add(DcTerm.accessRights);
        taxonTerms.add(DwcTerm.datasetName);
        taxonTerms.add(DwcTerm.datasetID);
        taxonTerms.add(DwcTerm.nomenclaturalCode);
        taxonTerms.add(DcTerm.references);
        taxonTerms.add(DwcTerm.nomenclaturalStatus);
        taxonTerms.add(DwcTerm.phylum);
        taxonTerms.add(DwcTerm.parentNameUsage);
        taxonTerms.add(DwcTerm.parentNameUsageID);
        taxonTerms.add(DwcTerm.namePublishedInYear);
        taxonTerms.add(DwcTerm.scientificNameAuthorship);
        taxonTerms.add(DwcTerm.scientificName);
        taxonTerms.add(DwcTerm.acceptedNameUsage);
        taxonTerms.add(DwcTerm.taxonRemarks);
        taxonTerms.add(DwcTerm.originalNameUsage);
        taxonTerms.add(DwcTerm.namePublishedIn);
        taxonTerms.add(DwcTerm.taxonomicStatus);
        taxonTerms.add(DwcTerm.family);
        taxonTerms.add(DwcTerm.kingdom);
        taxonTerms.add(DwcTerm.specificEpithet);
        taxonTerms.add(DwcTerm.infraspecificEpithet);
        taxonTerms.add(DwcTerm.scientificNameID);
        taxonTerms.add(DwcTerm.verbatimTaxonRank);
        taxonTerms.add(DwcTerm.originalNameUsageID);
        taxonTerms.add(DcTerm.bibliographicCitation);
        taxonTerms.add(DwcTerm.namePublishedInID);
        taxonTerms.add(DwcTerm.order);
        taxonTerms.add(DwcTerm.nameAccordingTo);
        taxonTerms.add(DwcTerm.subgenus);
        taxonTerms.add(DwcTerm.taxonRank);
        taxonTerms.add(DwcTerm.acceptedNameUsageID);
        taxonTerms.add(DwcTerm.genus);
        taxonTerms.add(DwcTerm.class_);
        taxonTerms.add(DwcTerm.taxonID);

       assertEquals("darwinCorePropertyMap should contain the expected terms for dwc:Taxon", darwinCorePropertyMap.getTerms(DwcTerm.Taxon), taxonTerms);

        Set<Term> descriptionTerms = new HashSet<Term>();
        descriptionTerms.add(DcTerm.creator);
        descriptionTerms.add(DcTerm.contributor);
        descriptionTerms.add(DcTerm.created);
        descriptionTerms.add(DcTerm.modified);
        descriptionTerms.add(DcTerm.rightsHolder);
        descriptionTerms.add(DcTerm.license);
        descriptionTerms.add(DcTerm.rights);
        descriptionTerms.add(DcTerm.accessRights);
        descriptionTerms.add(DwcTerm.datasetName);
        descriptionTerms.add(DwcTerm.datasetID);
        descriptionTerms.add(DcTerm.identifier);
        descriptionTerms.add(DwcTerm.taxonID);
        descriptionTerms.add(DcTerm.audience);
        descriptionTerms.add(DcTerm.description);
        descriptionTerms.add(DcTerm.language);
        descriptionTerms.add(DcTerm.source);
        descriptionTerms.add(DcTerm.type);

       assertEquals("darwinCorePropertyMap should contain the expected terms for gbif:Description", darwinCorePropertyMap.getTerms(GbifTerm.Description), descriptionTerms);

        Set<Term> distributionTerms = new HashSet<Term>();
        distributionTerms.add(DcTerm.creator);
        distributionTerms.add(DcTerm.contributor);
        distributionTerms.add(DcTerm.created);
        distributionTerms.add(DcTerm.modified);
        distributionTerms.add(DcTerm.rightsHolder);
        distributionTerms.add(DcTerm.license);
        distributionTerms.add(DcTerm.rights);
        distributionTerms.add(DcTerm.accessRights);
        distributionTerms.add(DwcTerm.datasetName);
        distributionTerms.add(DwcTerm.datasetID);
        distributionTerms.add(DcTerm.identifier);
        distributionTerms.add(DwcTerm.taxonID);
        distributionTerms.add(DwcTerm.locality);
        distributionTerms.add(DwcTerm.locationID);
        distributionTerms.add(DwcTerm.occurrenceRemarks);
        distributionTerms.add(DwcTerm.occurrenceStatus);
        distributionTerms.add(DwcTerm.establishmentMeans);

       assertEquals("darwinCorePropertyMap should contain the expected terms for gbif:Distribution", darwinCorePropertyMap.getTerms(GbifTerm.Distribution), distributionTerms);

        Set<Term> measurementOrFactTerms = new HashSet<Term>();
        measurementOrFactTerms.add(DcTerm.creator);
        measurementOrFactTerms.add(DcTerm.contributor);
        measurementOrFactTerms.add(DcTerm.created);
        measurementOrFactTerms.add(DcTerm.modified);
        measurementOrFactTerms.add(DcTerm.rightsHolder);
        measurementOrFactTerms.add(DcTerm.license);
        measurementOrFactTerms.add(DcTerm.rights);
        measurementOrFactTerms.add(DcTerm.accessRights);
        measurementOrFactTerms.add(DwcTerm.datasetName);
        measurementOrFactTerms.add(DwcTerm.datasetID);
   	measurementOrFactTerms.add(DwcTerm.measurementID);
   	measurementOrFactTerms.add(DwcTerm.taxonID);
   	measurementOrFactTerms.add(DwcTerm.measurementAccuracy);
   	measurementOrFactTerms.add(DwcTerm.measurementDeterminedBy);
   	measurementOrFactTerms.add(DwcTerm.measurementDeterminedDate);
   	measurementOrFactTerms.add(DwcTerm.measurementMethod);
   	measurementOrFactTerms.add(DwcTerm.measurementRemarks);
   	measurementOrFactTerms.add(DwcTerm.measurementType);
   	measurementOrFactTerms.add(DwcTerm.measurementUnit);
   	measurementOrFactTerms.add(DwcTerm.measurementValue);

       assertEquals("darwinCorePropertyMap should contain the expected terms for dwc:MeasurementOrFact", darwinCorePropertyMap.getTerms(DwcTerm.MeasurementOrFact), measurementOrFactTerms);

        Set<Term> vernacularNameTerms = new HashSet<Term>();
        vernacularNameTerms.add(DcTerm.creator);
        vernacularNameTerms.add(DcTerm.contributor);
        vernacularNameTerms.add(DcTerm.created);
        vernacularNameTerms.add(DcTerm.modified);
        vernacularNameTerms.add(DcTerm.rightsHolder);
        vernacularNameTerms.add(DcTerm.license);
        vernacularNameTerms.add(DcTerm.rights);
        vernacularNameTerms.add(DcTerm.accessRights);
        vernacularNameTerms.add(DwcTerm.datasetName);
        vernacularNameTerms.add(DwcTerm.datasetID);
   	vernacularNameTerms.add(DcTerm.identifier);
   	vernacularNameTerms.add(DwcTerm.taxonID);
   	vernacularNameTerms.add(DwcTerm.countryCode);
   	vernacularNameTerms.add(DcTerm.language);
   	vernacularNameTerms.add(DwcTerm.lifeStage);
   	vernacularNameTerms.add(DwcTerm.locality);
   	vernacularNameTerms.add(DwcTerm.locationID);
   	vernacularNameTerms.add(GbifTerm.organismPart);
   	vernacularNameTerms.add(GbifTerm.isPlural);
   	vernacularNameTerms.add(GbifTerm.isPreferredName);
   	vernacularNameTerms.add(DwcTerm.sex);
   	vernacularNameTerms.add(DcTerm.source);
   	vernacularNameTerms.add(DwcTerm.taxonRemarks);
   	vernacularNameTerms.add(DcTerm.temporal);
   	vernacularNameTerms.add(DwcTerm.vernacularName);

       assertEquals("darwinCorePropertyMap should contain the expected terms for gbif:VernacularName", darwinCorePropertyMap.getTerms(GbifTerm.VernacularName), vernacularNameTerms);

        Set<Term> multimediaTerms = new HashSet<Term>();
        multimediaTerms.add(DcTerm.creator);
        multimediaTerms.add(DcTerm.contributor);
        multimediaTerms.add(DcTerm.created);
        multimediaTerms.add(DcTerm.modified);
        multimediaTerms.add(DcTerm.rightsHolder);
        multimediaTerms.add(DcTerm.license);
        multimediaTerms.add(DcTerm.rights);
        multimediaTerms.add(DcTerm.accessRights);
        multimediaTerms.add(DwcTerm.datasetName);
        multimediaTerms.add(DwcTerm.datasetID);
        multimediaTerms.add(DcTerm.identifier);
   	multimediaTerms.add(DwcTerm.taxonID);
   	multimediaTerms.add(DcTerm.audience);
   	multimediaTerms.add(DcTerm.description);
   	multimediaTerms.add(DcTerm.format);
        multimediaTerms.add(DcTerm.type);
     	multimediaTerms.add(Wgs84Term.latitude);
    	multimediaTerms.add(Wgs84Term.longitude);
    	multimediaTerms.add(DcTerm.spatial);
   	multimediaTerms.add(DcTerm.publisher);
   	multimediaTerms.add(DcTerm.references);
   	multimediaTerms.add(DcTerm.subject);
   	multimediaTerms.add(DcTerm.title);
   	multimediaTerms.add(SddTerm.termID);

       assertEquals("darwinCorePropertyMap should contain the expected terms for gbif:Multimedia", darwinCorePropertyMap.getTerms(GbifTerm.Multimedia), multimediaTerms);

        Set<Term> typeAndSpecimenTerms = new HashSet<Term>();
        typeAndSpecimenTerms.add(DcTerm.creator);
        typeAndSpecimenTerms.add(DcTerm.contributor);
        typeAndSpecimenTerms.add(DcTerm.created);
        typeAndSpecimenTerms.add(DcTerm.modified);
        typeAndSpecimenTerms.add(DcTerm.rightsHolder);
        typeAndSpecimenTerms.add(DcTerm.license);
        typeAndSpecimenTerms.add(DcTerm.rights);
        typeAndSpecimenTerms.add(DcTerm.accessRights);
        typeAndSpecimenTerms.add(DwcTerm.datasetName);
        typeAndSpecimenTerms.add(DwcTerm.datasetID);
   	typeAndSpecimenTerms.add(DcTerm.identifier);
   	typeAndSpecimenTerms.add(DwcTerm.taxonID);
   	typeAndSpecimenTerms.add(DcTerm.bibliographicCitation);
   	typeAndSpecimenTerms.add(DwcTerm.catalogNumber);
   	typeAndSpecimenTerms.add(DwcTerm.collectionCode);
   	typeAndSpecimenTerms.add(DwcTerm.decimalLatitude);
   	typeAndSpecimenTerms.add(DwcTerm.decimalLongitude);
   	typeAndSpecimenTerms.add(DwcTerm.institutionCode);
   	typeAndSpecimenTerms.add(DwcTerm.locality);
   	typeAndSpecimenTerms.add(DwcTerm.locationID);
   	typeAndSpecimenTerms.add(DwcTerm.recordedBy);
   	typeAndSpecimenTerms.add(DwcTerm.scientificName);
   	typeAndSpecimenTerms.add(DwcTerm.sex);
   	typeAndSpecimenTerms.add(DcTerm.source);
   	typeAndSpecimenTerms.add(DwcTerm.taxonRank);
   	typeAndSpecimenTerms.add(GbifTerm.typeDesignatedBy);
   	typeAndSpecimenTerms.add(GbifTerm.typeDesignationType);
   	typeAndSpecimenTerms.add(DwcTerm.typeStatus);
   	typeAndSpecimenTerms.add(DwcTerm.verbatimEventDate);
	typeAndSpecimenTerms.add(GbifTerm.verbatimLabel);
	typeAndSpecimenTerms.add(DwcTerm.verbatimLatitude);
	typeAndSpecimenTerms.add(DwcTerm.verbatimLongitude);

       assertEquals("darwinCorePropertyMap should contain the expected terms for gbif:TypesAndSpecimen", darwinCorePropertyMap.getTerms(GbifTerm.TypesAndSpecimen), typeAndSpecimenTerms);

        Set<Term> referenceTerms = new HashSet<Term>();
        referenceTerms.add(DcTerm.creator);
        referenceTerms.add(DcTerm.contributor);
        referenceTerms.add(DcTerm.created);
        referenceTerms.add(DcTerm.modified);
        referenceTerms.add(DcTerm.rightsHolder);
        referenceTerms.add(DcTerm.license);
        referenceTerms.add(DcTerm.rights);
        referenceTerms.add(DcTerm.accessRights);
        referenceTerms.add(DwcTerm.datasetName);
        referenceTerms.add(DwcTerm.datasetID);
   	referenceTerms.add(DcTerm.identifier);
   	referenceTerms.add(DwcTerm.taxonID);
   	referenceTerms.add(DcTerm.bibliographicCitation);
   	referenceTerms.add(DcTerm.date);
   	referenceTerms.add(DcTerm.description);
   	referenceTerms.add(DcTerm.language);
   	referenceTerms.add(DcTerm.source);
   	referenceTerms.add(DcTerm.subject);
   	referenceTerms.add(DwcTerm.taxonRemarks);
   	referenceTerms.add(DcTerm.title);
   	referenceTerms.add(DcTerm.type);

       assertEquals("darwinCorePropertyMap should contain the expected terms for gbif:Reference", darwinCorePropertyMap.getTerms(GbifTerm.Reference), referenceTerms);

        Set<Term> nodeTerms = new HashSet<Term>();
        nodeTerms.add(DcTerm.creator);
        nodeTerms.add(DcTerm.contributor);
        nodeTerms.add(DcTerm.created);
        nodeTerms.add(DcTerm.modified);
        nodeTerms.add(DcTerm.rightsHolder);
        nodeTerms.add(DcTerm.license);
        nodeTerms.add(DcTerm.rights);
        nodeTerms.add(DcTerm.accessRights);
        nodeTerms.add(DwcTerm.datasetName);
        nodeTerms.add(DwcTerm.datasetID);
   	nodeTerms.add(DcTerm.identifier);
   	nodeTerms.add(SddTerm.inapplicableIfID);
   	nodeTerms.add(SddTerm.applicableIfID);
   	nodeTerms.add(SddTerm.parameters);
   	nodeTerms.add(SddTerm.termID);
   	nodeTerms.add(SddTerm.parentID);

       assertEquals("darwinCorePropertyMap should contain the expected terms for sdd:Node", darwinCorePropertyMap.getTerms(SddTerm.Node), nodeTerms);

        Set<Term> datasetTerms = new HashSet<Term>();
        datasetTerms.add(DcTerm.creator);
        datasetTerms.add(DcTerm.contributor);
        datasetTerms.add(DcTerm.created);
        datasetTerms.add(DcTerm.modified);
        datasetTerms.add(DcTerm.rightsHolder);
        datasetTerms.add(DcTerm.license);
        datasetTerms.add(DcTerm.rights);
        datasetTerms.add(DcTerm.accessRights);
        datasetTerms.add(DwcTerm.datasetName);
        datasetTerms.add(DwcTerm.datasetID);
     	datasetTerms.add(DwcTerm.taxonID);
	datasetTerms.add(DcTerm.identifier);
	datasetTerms.add(DcTerm.description);
	datasetTerms.add(DcTerm.title);
	datasetTerms.add(SddTerm.parameters);

       assertEquals("darwinCorePropertyMap should contain the expected terms for sdd:Dataset", darwinCorePropertyMap.getTerms(SddTerm.Dataset), datasetTerms);

        Set<Term> termTerms = new HashSet<Term>();
        termTerms.add(DcTerm.creator);
        termTerms.add(DcTerm.contributor);
        termTerms.add(DcTerm.created);
        termTerms.add(DcTerm.modified);
        termTerms.add(DcTerm.rightsHolder);
        termTerms.add(DcTerm.license);
        termTerms.add(DcTerm.rights);
        termTerms.add(DcTerm.accessRights);
        termTerms.add(DwcTerm.datasetName);
        termTerms.add(DwcTerm.datasetID);
     	termTerms.add(DwcTerm.taxonID);
	termTerms.add(DcTerm.identifier);
	termTerms.add(DcTerm.description);
	termTerms.add(DcTerm.title);
	termTerms.add(DcTerm.type);
	termTerms.add(DwcTerm.measurementUnit);
	termTerms.add(SddTerm.termID);

       assertEquals("darwinCorePropertyMap should contain the expected terms for skos:Concept", darwinCorePropertyMap.getTerms(SkosTerm.Concept), termTerms);

       boolean thrown = false;

       try {
         darwinCorePropertyMap.getTerms(DcTerm.creator);
       } catch (IllegalArgumentException iae) {
         thrown = true;
       }

       assertTrue("darwinCorePropertyMap should throw an illegal argument exception for an unexpected term", thrown);
    }

    @Test
    public void testGetClazz() {
       assertEquals("darwinCorePropertyMap should return Taxon for 'dwc:Taxon'", Taxon.class, darwinCorePropertyMap.getClazz("dwc:Taxon"));
       assertEquals("darwinCorePropertyMap should return Dataset for 'sdd:Dataset'", Dataset.class, darwinCorePropertyMap.getClazz("sdd:Dataset"));
       assertEquals("darwinCorePropertyMap should return Description for 'gbif:Description'", Description.class, darwinCorePropertyMap.getClazz("gbif:Description"));
       assertEquals("darwinCorePropertyMap should return Distribution for 'gbif:Distribution'", Distribution.class, darwinCorePropertyMap.getClazz("gbif:Distribution"));
       assertEquals("darwinCorePropertyMap should return Multimedia for 'gbif:Multimedia'", Multimedia.class, darwinCorePropertyMap.getClazz("gbif:Multimedia"));
       assertEquals("darwinCorePropertyMap should return Multimedia for 'gbif:Image'", Multimedia.class, darwinCorePropertyMap.getClazz("gbif:Image"));
       assertEquals("darwinCorePropertyMap should return MeasurementOrFact for 'dwc:MeasurementOrFact'", MeasurementOrFact.class, darwinCorePropertyMap.getClazz("dwc:MeasurementOrFact"));
       assertEquals("darwinCorePropertyMap should return Node for 'sdd:Node'", Node.class, darwinCorePropertyMap.getClazz("sdd:Node"));
       assertEquals("darwinCorePropertyMap should return Reference for 'gbif:Reference'", Reference.class, darwinCorePropertyMap.getClazz("gbif:Reference"));
       assertEquals("darwinCorePropertyMap should return Term for 'skos:Concept'", org.cateproject.domain.Term.class, darwinCorePropertyMap.getClazz("skos:Concept"));
       assertEquals("darwinCorePropertyMap should return TypeAndSpecimen for 'gbif:TypeAndSpecimen'", TypeAndSpecimen.class, darwinCorePropertyMap.getClazz("gbif:TypeAndSpecimen"));
       assertEquals("darwinCorePropertyMap should return VernacularName for 'gbif:VernacularName'", VernacularName.class, darwinCorePropertyMap.getClazz("gbif:VernacularName"));
       assertEquals("darwinCorePropertyMap should return Taxon for 'dwc:Taxon'", Taxon.class, darwinCorePropertyMap.getClazz("dwc:Taxon"));

       boolean thrown = false;

       try {
         darwinCorePropertyMap.getClazz("dc:creator");
       } catch (IllegalArgumentException iae) {
         thrown = true;
       }

       assertTrue("darwinCorePropertyMap should throw an illegal argument exception for an unexpected term", thrown);
    }

    @Test
    public void testGetPropertyMap() {
       assertEquals("darwinCorePropertyMap should return taxonTerms for 'dwc:Taxon'", darwinCorePropertyMap.taxonTerms, darwinCorePropertyMap.getPropertyMap(DwcTerm.Taxon));
       assertEquals("darwinCorePropertyMap should return descriptionTerms for 'gbif:Description'", darwinCorePropertyMap.descriptionTerms, darwinCorePropertyMap.getPropertyMap(GbifTerm.Description));
       assertEquals("darwinCorePropertyMap should return distributionTerms for 'gbif:Distribution'", darwinCorePropertyMap.distributionTerms, darwinCorePropertyMap.getPropertyMap(GbifTerm.Distribution));
       assertEquals("darwinCorePropertyMap should return measurementOrFactTerms for 'dwc:MeasurementOrFact'", darwinCorePropertyMap.measurementOrFactTerms, darwinCorePropertyMap.getPropertyMap(DwcTerm.MeasurementOrFact));
       assertEquals("darwinCorePropertyMap should return vernacularNameTerms for 'gbif:VernacularName'", darwinCorePropertyMap.vernacularNameTerms, darwinCorePropertyMap.getPropertyMap(GbifTerm.VernacularName));
       assertEquals("darwinCorePropertyMap should return multimediaTerms for 'gbif:Multimedia'", darwinCorePropertyMap.multimediaTerms, darwinCorePropertyMap.getPropertyMap(GbifTerm.Multimedia));
       assertEquals("darwinCorePropertyMap should return typeAndSpecimenTerms for 'dwc:TypesAndSpecimen'", darwinCorePropertyMap.typeAndSpecimenTerms, darwinCorePropertyMap.getPropertyMap(GbifTerm.TypesAndSpecimen));
       assertEquals("darwinCorePropertyMap should return referenceTerms for 'gbif:Reference'", darwinCorePropertyMap.referenceTerms, darwinCorePropertyMap.getPropertyMap(GbifTerm.Reference));
       assertEquals("darwinCorePropertyMap should return termTerms for 'skos:Concept'", darwinCorePropertyMap.termTerms, darwinCorePropertyMap.getPropertyMap(SkosTerm.Concept));
       assertEquals("darwinCorePropertyMap should return datasetTerms for 'sdd:Dataset'", darwinCorePropertyMap.datasetTerms, darwinCorePropertyMap.getPropertyMap(SddTerm.Dataset));
       assertEquals("darwinCorePropertyMap should return nodeTerms for 'sdd:Node'", darwinCorePropertyMap.nodeTerms, darwinCorePropertyMap.getPropertyMap(SddTerm.Node));

       boolean thrown = false;

       try {
         darwinCorePropertyMap.getPropertyMap(DcTerm.creator);
       } catch (IllegalArgumentException iae) {
         thrown = true;
       }

       assertTrue("darwinCorePropertyMap should throw an illegal argument exception for an unexpected term", thrown);
    }


    @Test
    public void testGetPossibleExtensions() {
        Set<Term> expectedExtensions = new HashSet<Term>();
        expectedExtensions.add(DwcTerm.Taxon);
        String[] fields = new String[]{
          "creator",
          "class",
          "taxonID",
          "scientificName"
        };
        
       assertEquals("darwinCorePropertyMap should return Taxon for the submitted fields", expectedExtensions, darwinCorePropertyMap.getPossibleExtensions(fields));
    }

    @Test
    public void testGetPossibleExtensionsWithUnknownTerm() {
        Set<Term> expectedExtensions = new HashSet<Term>();
        expectedExtensions.add(DwcTerm.Taxon);
        String[] fields = new String[]{
          "creator",
          "class",
          "taxonID",
          "scientificName",
          "http://example.com/term"
        };
        
       assertEquals("darwinCorePropertyMap should return Taxon for the submitted fields", expectedExtensions, darwinCorePropertyMap.getPossibleExtensions(fields));
    }

    @Test
    public void testAllowedExtensions() {
       assertThat("darwinCorePropertyMap should return the expected extensions", 
         darwinCorePropertyMap.getAllowedExtensions(),
         containsInAnyOrder(
          "gbif:Multimedia",
          "dwc:Taxon", 
          "dwc:MeasurementOrFact",
          "skos:Concept", 
          "gbif:Distribution", 
          "gbif:Description", 
          "gbif:Reference", 
          "gbif:VernacularName", 
          "sdd:Node", 
          "sdd:Dataset", 
          "gbif:TypesAndSpecimen"
          ));
    }
}
