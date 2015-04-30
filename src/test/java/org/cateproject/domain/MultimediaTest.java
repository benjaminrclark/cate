package org.cateproject.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.cateproject.domain.constants.DCMIType;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class MultimediaTest {

	private Multimedia multimedia;
	
	private List<Taxon> taxa;
	
	private Term term;
	
	@Before
	public void setUp() throws Exception {
		taxa = new ArrayList<Taxon>();
		term = new Term();
		multimedia = new Multimedia();
		multimedia.setTaxa(taxa);
		multimedia.setTerm(term);
		multimedia.setIdentifier("IDENTIFIER");
		multimedia.setTitle("TITLE");
		multimedia.setReferences("REFERENCES");
		multimedia.setDescription("DESCRIPTION");
		multimedia.setSpatial("SPATIAL");
		multimedia.setLatitude(1.0);
		multimedia.setLongitude(1.0);
		multimedia.setFormat("FORMAT");
		multimedia.setPublisher("PUBLISHER");
		multimedia.setAudience("AUDIENCE");
		multimedia.setWidth(1);
		multimedia.setHeight(1);
		multimedia.setSize(1L);
		multimedia.setHash("HASH");
		multimedia.setLocalFileName("LOCAL_FILE_NAME");
		multimedia.setFileLastModified(new DateTime(2000, 1, 1, 1, 1, 1));
		multimedia.setType(DCMIType.Sound);
	}

	@Test
	public void testGetIdentifier() {
		assertEquals("identifier should equal 'IDENTIFIER'","IDENTIFIER", multimedia.getIdentifier());
	}

	@Test
	public void testGetTitle() {
		assertEquals("title should equal 'TITLE'","TITLE", multimedia.getTitle());
	}

	@Test
	public void testGetReferences() {
		assertEquals("references should equal 'REFERENCES'","REFERENCES", multimedia.getReferences());
	}

	@Test
	public void testGetDescription() {
		assertEquals("description should equal 'DESCRIPTION'","DESCRIPTION", multimedia.getDescription());
	}

	@Test
	public void testGetSpatial() {
		assertEquals("spatial should equal 'SPATIAL'","SPATIAL", multimedia.getSpatial());
	}

	@Test
	public void testGetLatitude() {
		assertEquals("latitude should equal 1.0",new Double(1.0), multimedia.getLatitude());
	}

	@Test
	public void testGetLongitude() {
		assertEquals("longitude should equal 1.0",new Double(1.0), multimedia.getLongitude());
	}

	@Test
	public void testGetFormat() {
		assertEquals("format should equal 'FORMAT'","FORMAT", multimedia.getFormat());
	}

	@Test
	public void testGetPublisher() {
		assertEquals("publisher should equal 'PUBLISHER'","PUBLISHER", multimedia.getPublisher());
	}

	@Test
	public void testGetAudience() {
		assertEquals("audience should equal 'AUDIENCE'","AUDIENCE", multimedia.getAudience());
	}

	@Test
	public void testGetWidth() {
		assertEquals("width should equal 1",new Integer(1), multimedia.getWidth());
	}

	@Test
	public void testGetHeight() {
		assertEquals("height should equal 1",new Integer(1), multimedia.getHeight());
	}

	@Test
	public void testGetSize() {
		assertEquals("size should equal 1L",new Long(1L), multimedia.getSize());
	}

	@Test
	public void testGetHash() {
		assertEquals("hash should equal 'HASH'","HASH", multimedia.getHash());
	}

	@Test
	public void testGetLocalFileName() {
		assertEquals("localFileName should equal 'LOCAL_FILE_NAME'","LOCAL_FILE_NAME", multimedia.getLocalFileName());
	}

	@Test
	public void testGetTaxa() {
		assertEquals("taxa should equal taxa",taxa, multimedia.getTaxa());
	}

	@Test
	public void testGetTerm() {
		assertEquals("term should equal term",term, multimedia.getTerm());
	}

	@Test
	public void testGetFileLastModified() {
		assertEquals("fileLastModified should equal 2000-01-01T01:01:01",new DateTime(2000, 1, 1, 1, 1, 1), multimedia.getFileLastModified());
	}

	@Test
	public void testGetType() {
		assertEquals("type should equal DCMIType.Sound",DCMIType.Sound, multimedia.getType());
	}

}
