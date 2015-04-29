package org.cateproject.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class NodeTest {
	
	private Node node;
	
	private List<Node> children;
	
	private Node parent;
	
	private Term term;
	
	private Map<String, String> parameters;
	
	private Set<Term> inapplicableIf;
	
	private Set<Term> applicableIf;

	@Before
	public void setUp() throws Exception {
		children = new ArrayList<Node>();
		parent = new Node();
		node = new Node();
		term = new Term();
		parameters = new HashMap<String,String>();
		inapplicableIf = new HashSet<Term>();
		applicableIf = new HashSet<Term>();
		
		node.setIdentifier("IDENTIFIER");
		node.setChildren(children);
		node.setParent(parent);
		node.setTerm(term);
		node.setParameters(parameters);
		node.setInapplicableIf(inapplicableIf);
		node.setApplicableIf(applicableIf);
	}

	@Test
	public void testGetIdentifier() {
		assertEquals("identifier should equal 'IDENTIFIER'","IDENTIFIER", node.getIdentifier());
	}

	@Test
	public void testGetChildren() {
		assertEquals("children should equal children", children, node.getChildren());
	}

	@Test
	public void testGetParent() {
		assertEquals("parent should equal parent", parent, node.getParent());
	}

	@Test
	public void testGetTerm() {
		assertEquals("term should equal term", term, node.getTerm());
	}

	@Test
	public void testGetParameters() {
		assertEquals("parameters should equal parameters", parameters, node.getParameters());
	}

	@Test
	public void testGetInapplicableIf() {
		assertEquals("inapplicableIf should equal inapplicableIf", inapplicableIf, node.getInapplicableIf());
	}

	@Test
	public void testGetApplicableIf() {
		assertEquals("applicableIf should equal applicableIf", applicableIf, node.getApplicableIf());
	}

}
