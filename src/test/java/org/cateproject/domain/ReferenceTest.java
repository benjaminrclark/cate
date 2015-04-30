package org.cateproject.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.cateproject.domain.constants.ReferenceType;
import org.junit.Before;
import org.junit.Test;

public class ReferenceTest {
	
	private Reference reference;
	
	private List<Taxon> taxa;

	@Before
	public void setUp() throws Exception {
		taxa = new ArrayList<Taxon>();
		reference = new Reference();
		reference.setIdentifier("IDENTIFIER");
		reference.setBibliographicCitation("BIBLIOGRAPHIC_CITATION");
		reference.setDate("DATE");
		reference.setSource("SOURCE");
		reference.setDescription("DESCRIPTION");
		reference.setSubject("SUBJECT");
		reference.setLanguage(Locale.ENGLISH);
		reference.setTaxonRemarks("TAXON_REMARKS");
		reference.setTitle("TITLE");
		reference.setType(ReferenceType.checklist);
		reference.setTaxa(taxa);
	}

	@Test
	public void testGetIdentifier() {
		assertEquals("identifier should equal 'IDENTIFIER'","IDENTIFIER", reference.getIdentifier());
	}

	@Test
	public void testGetBibliographicCitation() {
		assertEquals("bibliographicCitation should equal 'BIBLIOGRAPHIC_CITATION'","BIBLIOGRAPHIC_CITATION", reference.getBibliographicCitation());
	}

	@Test
	public void testGetDate() {
		assertEquals("date should equal 'DATE'","DATE", reference.getDate());
	}

	@Test
	public void testGetSource() {
		assertEquals("source should equal 'SOURCE'","SOURCE", reference.getSource());
	}

	@Test
	public void testGetDescription() {
		assertEquals("description should equal 'DESCRIPTION'","DESCRIPTION", reference.getDescription());
	}

	@Test
	public void testGetSubject() {
		assertEquals("subject should equal 'SUBJECT'","SUBJECT", reference.getSubject());
	}

	@Test
	public void testGetLanguage() {
		assertEquals("language should equal Locale.ENGLISH",Locale.ENGLISH, reference.getLanguage());
	}

	@Test
	public void testGetTaxonRemarks() {
		assertEquals("taxonRemarks should equal 'TAXON_REMARKS'","TAXON_REMARKS", reference.getTaxonRemarks());
	}

	@Test
	public void testGetTitle() {
		assertEquals("title should equal 'TITLE'","TITLE", reference.getTitle());
	}

	@Test
	public void testGetType() {
		assertEquals("type should equal ReferenceType.checklist",ReferenceType.checklist, reference.getType());
	}

	@Test
	public void testGetTaxa() {
		assertEquals("taxa should equal taxa",taxa, reference.getTaxa());
	}

}
