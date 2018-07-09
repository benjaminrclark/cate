package org.cateproject.batch.job.darwincore.taxon;

import org.cateproject.domain.Reference;
import org.cateproject.domain.Taxon;
import org.cateproject.batch.job.darwincore.BaseFieldSetMapper;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.ecat.voc.NomenclaturalCode;
import org.gbif.ecat.voc.NomenclaturalStatus;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author ben
 * 
 */
public class FieldSetMapper extends BaseFieldSetMapper<Taxon> {

    private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

    public FieldSetMapper() {
        super(Taxon.class);
    }
    
    @Override
    public final void mapField(final Taxon object, final String fieldName, final String value) {
        super.mapField(object, fieldName, value);
        Term term = getTermFactory().findTerm(fieldName);
        if (term instanceof DcTerm) {
            DcTerm dcTerm = (DcTerm) term;
            switch (dcTerm) {
            case bibliographicCitation:
                object.setBibliographicCitation(value);
                break;
            default:
                break;
            }
        }

        // DwcTerms
        if (term instanceof DwcTerm) {
            DwcTerm dwcTerm = (DwcTerm) term;
            switch (dwcTerm) {
            case acceptedNameUsageID:
                if (value != null && value.trim().length() != 0) {
                    Taxon acceptedNameUsage = new Taxon();
                    acceptedNameUsage.setTaxonId(value);
                    object.setAcceptedNameUsage(acceptedNameUsage);
                }
                break;
            case class_:
                object.setClazz(value);
                break;
            case family:
                object.setFamily(value);
                break;
            case genus:
                object.setGenus(value);
                break;
            case higherClassification:
                object.setHigherClassification(value);
                break;
            case infraspecificEpithet:
                object.setInfraspecificEpithet(value);
                break;
            case kingdom:
                object.setKingdom(value);
                break;
            case nameAccordingToID:
                if (value != null && value.trim().length() > 0) {
                    Reference nameAccordingTo = new Reference();
                    nameAccordingTo.setIdentifier(value);
                    object.setNameAccordingTo(nameAccordingTo);
                }
                break;
            case namePublishedIn:
                object.setNamePublishedInString(value);
                break;
            case namePublishedInID:
                if (value != null && value.trim().length() > 0) {
                    Reference namePublishedIn = new Reference();
                    namePublishedIn.setIdentifier(value);
                    object.setNamePublishedIn(namePublishedIn);
                }
                break;
            case namePublishedInYear:
                object.setNamePublishedInYear(conversionService.convert(value, Integer.class));
                break;
            case nomenclaturalCode:
                object.setNomenclaturalCode(conversionService.convert(value, NomenclaturalCode.class));
                break;
            case nomenclaturalStatus:
                object.setNomenclaturalStatus(conversionService.convert(value, NomenclaturalStatus.class));
                break;
            case order:
                object.setOrder(value);
                break;
            case originalNameUsageID:
                if (value != null && value.trim().length() != 0) {
                    Taxon originalNameUsage = new Taxon();
                    originalNameUsage.setTaxonId(value);
                    object.setOriginalNameUsage(originalNameUsage);
                }
                break;
            case parentNameUsageID:
                if (value != null && value.trim().length() != 0) {
                    Taxon parentNameUsage = new Taxon();
                    parentNameUsage.setTaxonId(value);
                    object.setParentNameUsage(parentNameUsage);
                }
                break;
            case phylum:
                object.setPhylum(value);
                break;
            case scientificName:
                object.setScientificName(value);
                break;
            case scientificNameAuthorship:
                object.setScientificNameAuthorship(value);
                break;
            case scientificNameID:
                object.setScientificNameID(value);
                break;
            case specificEpithet:
                object.setSpecificEpithet(value);
                break;
            case subgenus:
                object.setSubgenus(value);
                break;
            case taxonID:
                object.setTaxonId(value);
                break;
            case taxonomicStatus:
                object.setTaxonomicStatus(conversionService.convert(value, TaxonomicStatus.class));
                break;
            case taxonRank:
                object.setTaxonRank(conversionService.convert(value, Rank.class));
                break;
            case taxonRemarks:
                object.setTaxonRemarks(value);
                break;
            case verbatimTaxonRank:
                object.setVerbatimTaxonRank(value);
                break;
            default:
                break;
            }
        }
    }
}
