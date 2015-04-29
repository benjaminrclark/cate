package org.cateproject.domain.constants;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

public class LocalityTest {
	
	

	@Test(expected = IllegalArgumentException.class)
	public void testFromStringWithoutTDWGPrefix() {
		Location.fromString("EUROPE");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFromStringWithIllegalCode() {
		Location.fromString("TDWG:SOUTH_AMERICA");
	}
	
	@Test
	public void testFromString() {
		assertEquals("'TDWG:EUROPE' should be converted into Locality.EUROPE", Location.EUROPE, Location.fromString("TDWG:EUROPE"));
	}

	@Test
	public void testToString() {
		assertEquals("Locality.EUROPE should return 'EUROPE'", "EUROPE", Location.EUROPE.toString());
		assertEquals("Locality.GRB should return 'EUROPE_NORTHERN_EUROPE_GRB'", "EUROPE_NORTHERN_EUROPE_GRB", Location.GRB.toString());
		
	}

	@Test
	public void testGetIdentifier() {
		assertEquals("Locality.EUROPE should return '1'", "1", Location.EUROPE.getIdentifier());
	}

	@Test
	public void testGetPrefix() {
		assertEquals("Locality.EUROPE should return 'TDWG'", "TDWG", Location.EUROPE.getPrefix());
	}

	@Test
	public void testGetParent() {
		assertEquals("Locality.NORTHERN_EUROPE should return Locality.EUROPE", Location.EUROPE, Location.NORTHERN_EUROPE.getParent());
	}

	@Test
	public void testGetLevel() {
		assertEquals("Locality.EUROPE should return 0", new Integer(0), Location.EUROPE.getLevel());
	}

	@Test
	public void testGetName() {
		assertEquals("Locality.EUROPE should return 'Europe'", "Europe", Location.EUROPE.getName());
	}

	@Test
	public void testGetFeatureId() {
		assertEquals("Locality.EUROPE should return 1", new Integer(1), Location.EUROPE.getFeatureId());
	}

	@Test
	public void testGetEnvelope() throws Exception {
		WKTReader wktReader = new WKTReader();
		Polygon envelope = (Polygon) wktReader.read("POLYGON((-2732035.7132802345 4137942.3699627547,7370519.770783563 4137942.3699627547,7370519.770783563 16048150.912687898,-2732035.7132802345 16048150.912687898,-2732035.7132802345 4137942.3699627547))");
		assertEquals("Locality.EUROPE should return expected envelope", 0, Location.EUROPE.getEnvelope().compareTo(envelope));
	}

	@Test
	public void testGetChildren() {
		Set<Location> children = new HashSet<Location>();
		children.add(Location.NORTHERN_EUROPE);
		children.add(Location.MIDDLE_EUROPE);	
		children.add(Location.SOUTHWESTERN_EUROPE);
		children.add(Location.SOUTHEASTERN_EUROPE);
		children.add(Location.EASTERN_EUROPE);
		assertEquals("Locality.EUROPE should return expected children", children, Location.EUROPE.getChildren());
	}

}
