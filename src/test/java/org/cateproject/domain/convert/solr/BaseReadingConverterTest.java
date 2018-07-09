package org.cateproject.domain.convert.solr;

import static org.junit.Assert.assertEquals;

import org.apache.solr.common.SolrDocument;
import org.cateproject.domain.Dataset;
import org.cateproject.domain.IdentificationKey;
import org.cateproject.domain.Multimedia;
import org.cateproject.domain.Reference;
import org.cateproject.domain.Taxon;
import org.cateproject.domain.Term;
import org.cateproject.repository.jpa.DatasetRepository;
import org.cateproject.repository.jpa.IdentificationKeyRepository;
import org.cateproject.repository.jpa.MultimediaRepository;
import org.cateproject.repository.jpa.ReferenceRepository;
import org.cateproject.repository.jpa.TaxonRepository;
import org.cateproject.repository.jpa.TermRepository;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class BaseReadingConverterTest {

  private BaseReadingConverter baseReadingConverter;

  private DatasetRepository datasetRepository;

  private TermRepository termRepository;

  private MultimediaRepository multimediaRepository;

  private ReferenceRepository referenceRepository;

  private TaxonRepository taxonRepository;

  private IdentificationKeyRepository identificationKeyRepository;

  @Before
  public void setUp() {
      baseReadingConverter = new BaseReadingConverter();
      datasetRepository = EasyMock.createMock(DatasetRepository.class);
      termRepository = EasyMock.createMock(TermRepository.class);
      multimediaRepository = EasyMock.createMock(MultimediaRepository.class);
      referenceRepository = EasyMock.createMock(ReferenceRepository.class);
      taxonRepository = EasyMock.createMock(TaxonRepository.class);
      identificationKeyRepository = EasyMock.createMock(IdentificationKeyRepository.class);
 
      baseReadingConverter.setDatasetRepository(datasetRepository);
      baseReadingConverter.setTermRepository(termRepository);
      baseReadingConverter.setMultimediaRepository(multimediaRepository);
      baseReadingConverter.setReferenceRepository(referenceRepository);
      baseReadingConverter.setTaxonRepository(taxonRepository);
      baseReadingConverter.setIdentificationKeyRepository(identificationKeyRepository);
  }

  @Test
  public void testConvertDataset() {
      Dataset dataset = new Dataset();
      SolrDocument solrDocument = new SolrDocument();
      solrDocument.addField("base.id_l", 1L);
      solrDocument.addField("base.class_s", "org.cateproject.domain.Dataset");

      EasyMock.expect(datasetRepository.findOne(EasyMock.eq(1L))).andReturn(dataset);

      EasyMock.replay(datasetRepository, termRepository, multimediaRepository, referenceRepository, taxonRepository, identificationKeyRepository);    
      assertEquals("convert should return a dataset", (Dataset)baseReadingConverter.convert(solrDocument), dataset);
      EasyMock.verify(datasetRepository, termRepository, multimediaRepository, referenceRepository, taxonRepository, identificationKeyRepository);    
  }

  @Test
  public void testConvertMultimedia() {
      Multimedia multimedia = new Multimedia();
      SolrDocument solrDocument = new SolrDocument();
      solrDocument.addField("base.id_l", 1L);
      solrDocument.addField("base.class_s", "org.cateproject.domain.Multimedia");

      EasyMock.expect(multimediaRepository.findOne(EasyMock.eq(1L))).andReturn(multimedia);

      EasyMock.replay(datasetRepository, termRepository, multimediaRepository, referenceRepository, taxonRepository, identificationKeyRepository);    
      assertEquals("convert should return a multimedia", (Multimedia)baseReadingConverter.convert(solrDocument), multimedia);
      EasyMock.verify(datasetRepository, termRepository, multimediaRepository, referenceRepository, taxonRepository, identificationKeyRepository);    
  }

  @Test
  public void testConvertReference() {
      Reference reference = new Reference();
      SolrDocument solrDocument = new SolrDocument();
      solrDocument.addField("base.id_l", 1L);
      solrDocument.addField("base.class_s", "org.cateproject.domain.Reference");

      EasyMock.expect(referenceRepository.findOne(EasyMock.eq(1L))).andReturn(reference);

      EasyMock.replay(datasetRepository, termRepository, multimediaRepository, referenceRepository, taxonRepository, identificationKeyRepository);    
      assertEquals("convert should return a reference", (Reference)baseReadingConverter.convert(solrDocument), reference);
      EasyMock.verify(datasetRepository, termRepository, multimediaRepository, referenceRepository, taxonRepository, identificationKeyRepository);    
  }

  @Test
  public void testConvertTaxon() {
      Taxon taxon = new Taxon();
      SolrDocument solrDocument = new SolrDocument();
      solrDocument.addField("base.id_l", 1L);
      solrDocument.addField("base.class_s", "org.cateproject.domain.Taxon");

      EasyMock.expect(taxonRepository.findOne(EasyMock.eq(1L))).andReturn(taxon);

      EasyMock.replay(datasetRepository, termRepository, multimediaRepository, referenceRepository, taxonRepository, identificationKeyRepository);    
      assertEquals("convert should return a taxon", (Taxon)baseReadingConverter.convert(solrDocument), taxon);
      EasyMock.verify(datasetRepository, termRepository, multimediaRepository, referenceRepository, taxonRepository, identificationKeyRepository);    
  }

  @Test
  public void testConvertTerm() {
      Term term = new Term();
      SolrDocument solrDocument = new SolrDocument();
      solrDocument.addField("base.id_l", 1L);
      solrDocument.addField("base.class_s", "org.cateproject.domain.Term");

      EasyMock.expect(termRepository.findOne(EasyMock.eq(1L))).andReturn(term);

      EasyMock.replay(datasetRepository, termRepository, multimediaRepository, referenceRepository, taxonRepository, identificationKeyRepository);    
      assertEquals("convert should return a term", (Term)baseReadingConverter.convert(solrDocument), term);
      EasyMock.verify(datasetRepository, termRepository, multimediaRepository, referenceRepository, taxonRepository, identificationKeyRepository);    
  }

  @Test
  public void testConvertIdentificationKey() {
      IdentificationKey identificationKey = new IdentificationKey();
      SolrDocument solrDocument = new SolrDocument();
      solrDocument.addField("base.id_l", 1L);
      solrDocument.addField("base.class_s", "org.cateproject.domain.IdentificationKey");

      EasyMock.expect(identificationKeyRepository.findOne(EasyMock.eq(1L))).andReturn(identificationKey);

      EasyMock.replay(datasetRepository, termRepository, multimediaRepository, referenceRepository, taxonRepository, identificationKeyRepository);    
      assertEquals("convert should return an identificationKey", (IdentificationKey)baseReadingConverter.convert(solrDocument), identificationKey);
      EasyMock.verify(datasetRepository, termRepository, multimediaRepository, referenceRepository, taxonRepository, identificationKeyRepository);    
  }

  @Test(expected = RuntimeException.class)
  public void testConvertUnknownClass() {
      SolrDocument solrDocument = new SolrDocument();
      solrDocument.addField("base.id_l", 1L);
      solrDocument.addField("base.class_s", "org.cateproject.domain.UnknownClass");

      baseReadingConverter.convert(solrDocument);
  }
}
