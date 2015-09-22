package org.cateproject.domain.convert.solr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.solr.common.SolrInputDocument;
import org.cateproject.domain.Description;
import org.cateproject.domain.Distribution;
import org.cateproject.domain.Reference;
import org.cateproject.domain.Taxon;
import org.cateproject.domain.constants.Location;
import org.gbif.ecat.voc.NomenclaturalCode;
import org.gbif.ecat.voc.NomenclaturalStatus;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.junit.Before;
import org.junit.Test;

public class TaxonWritingConverterTest {

    private TaxonWritingConverter taxonWritingConverter;

    @Before
    public void setUp() {
        taxonWritingConverter = new TaxonWritingConverter();
    }

    @Test
    public void testConvert() {
        Taxon parentNameUsage = new Taxon();
        parentNameUsage.setScientificName("PARENT_NAME_USAGE"); 

        Taxon acceptedNameUsage = new Taxon();
        acceptedNameUsage.setScientificName("ACCEPTED_NAME_USAGE"); 
 
        Taxon originalNameUsage = new Taxon();
        originalNameUsage.setScientificName("ORIGINAL_NAME_USAGE"); 

        Reference nameAccordingTo = new Reference();
        nameAccordingTo.setBibliographicCitation("NAME_ACCORDING_TO");


        Reference namePublishedIn = new Reference();
        namePublishedIn.setBibliographicCitation("NAME_PUBLISHED_IN");

        Taxon taxon = new Taxon();
        taxon.setId(1L);
        taxon.setScientificName("SCIENTIFIC_NAME");
        taxon.setTaxonId("TAXON_ID");
        taxon.setScientificNameID("SCIENTIFIC_NAME_ID");
        taxon.setNamePublishedInYear(1234);
        taxon.setHigherClassification("HIGHER_CLASSIFICATION");
        taxon.setKingdom("KINGDOM");
        taxon.setPhylum("PHYLUM");
        taxon.setClazz("CLAZZ");
        taxon.setOrder("ORDER");
        taxon.setFamily("FAMILY");
        taxon.setGenus("GENUS");
        taxon.setSubgenus("SUBGENUS");
        taxon.setSpecificEpithet("SPECIFIC_EPITHET");
        taxon.setInfraspecificEpithet("INFRASPECIFIC_EPITHET");
        taxon.setTaxonRank(Rank.SPECIES);
        taxon.setNomenclaturalStatus(NomenclaturalStatus.Available);
        taxon.setVerbatimTaxonRank("VERBATIM_TAXON_RANK");
        taxon.setScientificNameAuthorship("SCIENTIFIC_NAME_AUTHORSHIP");
        taxon.setNomenclaturalCode(NomenclaturalCode.Zoological);
        taxon.setTaxonomicStatus(TaxonomicStatus.Accepted);
        taxon.setTaxonRemarks("TAXON_REMARKS");
        taxon.setParentNameUsage(parentNameUsage);
        taxon.setAcceptedNameUsage(acceptedNameUsage);
        taxon.setOriginalNameUsage(originalNameUsage); 
        taxon.setNameAccordingTo(nameAccordingTo);
        taxon.setNamePublishedIn(namePublishedIn);
        taxon.setScientificName("SCIENTIFIC_NAME");
        Distribution africa = new Distribution();
        africa.setIdentifier("AFRICA"); 
        africa.setLocation(Location.AFRICA);
        Distribution southern_africa = new Distribution();
        southern_africa.setIdentifier("SOUTHERN_AFRICA");
        southern_africa.setLocation(Location.SOUTHERN_AFRICA);
        Distribution botswana = new Distribution(); 
        botswana.setIdentifier("BOTSWANA");
        botswana.setLocation(Location.BOT);
        taxon.getDistribution().add(africa);
        taxon.getDistribution().add(southern_africa);
        taxon.getDistribution().add(botswana);
 
        Description description = new Description();
        description.setDescription("DESCRIPTION");
        taxon.getDescriptions().add(description);

        SolrInputDocument solrDocument = taxonWritingConverter.convert(taxon); 

        assertEquals("id should equal 'taxon_1'", solrDocument.getFieldValue("id"), "taxon_1");
        assertEquals("base.label_sort should equal 'SCIENTIFIC_NAME'", solrDocument.getFieldValue("base.label_sort"), "SCIENTIFIC_NAME");
        assertEquals("base.class_s should equal 'org.cateproject.domain.Taxon'", solrDocument.getFieldValue("base.class_s"), "org.cateproject.domain.Taxon");
        assertEquals("taxon.taxonid_s should equal 'TAXON_ID'", solrDocument.getFieldValue("taxon.taxonid_s"), "TAXON_ID");
        assertEquals("taxon.scientificnameid_s should equal 'SCIENTIFIC_NAME_ID'", solrDocument.getFieldValue("taxon.scientificnameid_s"), "SCIENTIFIC_NAME_ID");
        assertEquals("taxon.namepublishedinyear_i should equal 1234", solrDocument.getFieldValue("taxon.namepublishedinyear_i"), 1234);
        assertEquals("taxon.higherclassification_s should equal 'HIGHER_CLASSIFICATION'", solrDocument.getFieldValue("taxon.higherclassification_s"), "HIGHER_CLASSIFICATION");
        assertEquals("taxon.kingdom_s should equal 'KINGDOM'", solrDocument.getFieldValue("taxon.kingdom_s"), "KINGDOM");
        assertEquals("taxon.phylum_s should equal 'PHYLUM'", solrDocument.getFieldValue("taxon.phylum_s"), "PHYLUM");
        assertEquals("taxon.clazz_s should equal 'CLAZZ'", solrDocument.getFieldValue("taxon.clazz_s"), "CLAZZ");
        assertEquals("taxon.order_s should equal 'ORDER'", solrDocument.getFieldValue("taxon.order_s"), "ORDER");
        assertEquals("taxon.family_s should equal 'FAMILY'", solrDocument.getFieldValue("taxon.family_s"), "FAMILY");
        assertEquals("taxon.genus_s should equal 'GENUS'", solrDocument.getFieldValue("taxon.genus_s"), "GENUS");
        assertEquals("taxon.subgenus_s should equal 'SUBGENUS'", solrDocument.getFieldValue("taxon.subgenus_s"), "SUBGENUS");
        assertEquals("taxon.specificepithet_s should equal 'SPECIFIC_EPITHET'", solrDocument.getFieldValue("taxon.specificepithet_s"), "SPECIFIC_EPITHET");
        assertEquals("taxon.infraspecificepithet_s should equal 'INFRASPECIFIC_EPITHET'", solrDocument.getFieldValue("taxon.infraspecificepithet_s"), "INFRASPECIFIC_EPITHET");
        assertEquals("taxon.taxonrank_t should equal Rank.SPECIES", solrDocument.getFieldValue("taxon.taxonrank_t"), Rank.SPECIES);
        assertEquals("taxon.nomenclaturalstatus_t should equal NomenclaturalStatus.Available", solrDocument.getFieldValue("taxon.nomenclaturalstatus_t"), NomenclaturalStatus.Available);
        assertEquals("taxon.verbatimtaxonrank_s should equal 'VERBATIM_TAXON_RANK'", solrDocument.getFieldValue("taxon.verbatimtaxonrank_s"), "VERBATIM_TAXON_RANK");
        assertEquals("taxon.scientificnameauthorship_s should equal 'SCIENTIFIC_NAME_AUTHORSHIP'", solrDocument.getFieldValue("taxon.scientificnameauthorship_s"), "SCIENTIFIC_NAME_AUTHORSHIP");
        assertEquals("taxon.nomenclaturalcode_t should equal NomenclaturalCode.Zoological", solrDocument.getFieldValue("taxon.nomenclaturalcode_t"), NomenclaturalCode.Zoological);
        assertEquals("taxon.taxonomicstatus_t should equal TaxonomicStatus.Accepted", solrDocument.getFieldValue("taxon.taxonomicstatus_t"), TaxonomicStatus.Accepted);
        assertEquals("taxon.taxonremarks_s should equal 'TAXON_REMARKS'", solrDocument.getFieldValue("taxon.taxonremarks_s"), "TAXON_REMARKS");
        assertEquals("taxon.parentnameusage_t should equal parentNameUsage", solrDocument.getFieldValue("taxon.parentnameusage_t"), parentNameUsage);
        assertEquals("taxon.acceptednameusage_t should equal acceptedNameUsage", solrDocument.getFieldValue("taxon.acceptednameusage_t"), acceptedNameUsage);
        assertEquals("taxon.originalnameusage_t should equal originalNameUsage", solrDocument.getFieldValue("taxon.originalnameusage_t"), originalNameUsage);
        assertEquals("taxon.namepublishedin_t should equal namePublishedIn", solrDocument.getFieldValue("taxon.namepublishedin_t"), namePublishedIn);
        assertEquals("taxon.nameaccordingto_t should equal nameAccordingTo", solrDocument.getFieldValue("taxon.nameaccordingto_t"), nameAccordingTo);
        assertEquals("taxon.scientificname_s should equal 'SCIENTIFIC_NAME'", solrDocument.getFieldValue("taxon.scientificname_s"), "SCIENTIFIC_NAME");
        assertEquals("taxon.distribution_TDWG_0_ss should equal 'AFRICA'", solrDocument.getFieldValue("taxon.distribution_TDWG_0_ss"), "AFRICA");
        assertTrue("taxon.distribution_TDWG_1_ss should contain 'AFRICA_SOUTHERN_AFRICA'", solrDocument.getFieldValues("taxon.distribution_TDWG_1_ss").contains("AFRICA_SOUTHERN_AFRICA"));
        assertTrue("taxon.distribution_TDWG_2_ss should contain 'AFRICA_SOUTHERN_AFRICA_BOT'", solrDocument.getFieldValues("taxon.distribution_TDWG_2_ss").contains("AFRICA_SOUTHERN_AFRICA_BOT"));
        assertTrue("base_solrsummary_t should contain 'DESCRIPTION'", ((StringBuilder)solrDocument.getFieldValue("base_solrsummary_t")).toString().contains("DESCRIPTION"));
    }
}
