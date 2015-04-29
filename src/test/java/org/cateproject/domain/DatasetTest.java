package org.cateproject.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class DatasetTest {
	
	private Dataset dataset;
	
	private List<Node> nodes;
	
	private Map<String, String> parameters;
	
	private List<Taxon> taxa;

	@Before
	public void setUp() throws Exception {
		nodes = new ArrayList<Node>();
		parameters = new HashMap<String, String>();
		taxa = new ArrayList<Taxon>();
		
		dataset = new Dataset();
		dataset.setIdentifier("IDENTIFIER");
		dataset.setNodes(nodes);
		dataset.setParameters(parameters);
		dataset.setTaxa(taxa);
		dataset.setTitle("TITLE");
		dataset.setDescription("DESCRIPTION");
	}

	@Test
	public void testGetIdentifier() {
		assertEquals("identifier should equal 'IDENTIFIER'","IDENTIFIER", dataset.getIdentifier());
	}

	@Test
	public void testGetNodes() {
		assertEquals("nodes should equal nodes", nodes, dataset.getNodes());
	}

	@Test
	public void testGetParameters() {
		assertEquals("parameters should equal parameters", parameters, dataset.getParameters());
	}

	@Test
	public void testGetDescription() {
		assertEquals("description should equal 'DESCRIPTION'", "DESCRIPTION", dataset.getDescription());
	}

	@Test
	public void testGetTitle() {
		assertEquals("title should equal 'TITLE'", "TITLE", dataset.getTitle());
	}

	@Test
	public void testGetTaxa() {
		assertEquals("taxa should equal taxa", taxa, dataset.getTaxa());
	}

}
