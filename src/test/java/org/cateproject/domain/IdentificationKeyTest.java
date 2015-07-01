package org.cateproject.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class IdentificationKeyTest {
	
	private List<Taxon> taxa;
	
	private IdentificationKey identificationKey;

	@Before
	public void setUp() throws Exception {
		taxa = new ArrayList<Taxon>();
		identificationKey = new IdentificationKey();
		identificationKey.setTaxa(taxa);
		identificationKey.setIdentifier("IDENTIFIER");
		identificationKey.setDescription("DESCRIPTION");
		identificationKey.setTitle("TITLE");
		identificationKey.setCompiledKey("COMPILED_KEY");
	}

	@Test
	public void testGetIdentifier() {
		assertEquals("identifier should equal 'IDENTIFIER'","IDENTIFIER", identificationKey.getIdentifier());
	}

	@Test
	public void testGetDescription() {
		assertEquals("description should equal 'DESCRIPTION'","DESCRIPTION", identificationKey.getDescription());
	}

	@Test
	public void testGetTitle() {
		assertEquals("title should equal 'TITLE'","TITLE", identificationKey.getTitle());
	}

	@Test
	public void testGetCompiledKey() {
		assertEquals("compiledKey should equal 'COMPILED_KEY'","COMPILED_KEY", identificationKey.getCompiledKey());
	}

	@Test
	public void testGetTaxa() {
		assertEquals("taxa should equal taxa",taxa, identificationKey.getTaxa());
	}

}
