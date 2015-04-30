package org.cateproject.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cateproject.domain.constants.CharacterType;
import org.junit.Before;
import org.junit.Test;

public class TermTest {
	
	private Term term;
	
	private Term character;
	
	private List<Term> states;
	
	private List<Multimedia> multimedia;
	
	private List<Taxon> taxa;

	@Before
	public void setUp() throws Exception {
		taxa = new ArrayList<Taxon>();
		multimedia = new ArrayList<Multimedia>();
		states = new ArrayList<Term>();
		character = new Term();
		term = new Term();
		term.setTaxa(taxa);
		term.setMultimedia(multimedia);
		term.setCharacter(character);
		term.setStates(states);
		term.setIdentifier("IDENTIFIER");
		term.setDescription("DESCRIPTION");
		term.setUnit("UNIT");
		term.setType(CharacterType.Text);
		term.setOrder(1);
		term.setTitle("TITLE");
	}

	@Test
	public void testGetIdentifier() {
		assertEquals("identifier should equal 'IDENTIFIER'","IDENTIFIER", term.getIdentifier());
	}

	@Test
	public void testGetDescription() {
		assertEquals("description should equal 'DESCRIPTION'","DESCRIPTION", term.getDescription());
	}

	@Test
	public void testGetMultimedia() {
		assertEquals("multimedia should equal multimedia",multimedia, term.getMultimedia());
	}

	@Test
	public void testGetStates() {
		assertEquals("states should equal states",states, term.getStates());
	}

	@Test
	public void testGetCharacter() {
		assertEquals("character should equal character",character, term.getCharacter());
	}

	@Test
	public void testGetUnit() {
		assertEquals("unit should equal 'UNIT'","UNIT", term.getUnit());
	}

	@Test
	public void testGetType() {
		assertEquals("type should equal CharacterType.Text",CharacterType.Text, term.getType());
	}

	@Test
	public void testGetOrder() {
		assertEquals("order should equal 1",new Integer(1), term.getOrder());
	}

	@Test
	public void testGetTitle() {
		assertEquals("title should equal 'TITLE'","TITLE", term.getTitle());
	}

	@Test
	public void testGetTaxa() {
		assertEquals("taxa should equal taxa",taxa, term.getTaxa());
	}

}
