package org.cateproject.domain.convert;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.cateproject.domain.constants.SddTerm;
import org.cateproject.domain.constants.SkosTerm;
import org.cateproject.domain.constants.Wgs84Term;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.IucnTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.UnknownTerm;

import org.junit.Before;
import org.junit.Test;

public class StringToTermConverterTest {

    StringToTermConverter stringToTermConverter;

    @Before
    public void setUp() throws Exception {
        stringToTermConverter = new StringToTermConverter();
    }

    @Test
    public void testNormaliseTerm() {
        assertEquals("normaliseTerm should remove quotes and lowercase the term","normalisedterm", stringToTermConverter.normaliseTerm("norm\"alised\"TERM"));
    }

    @Test
    public void testFindTermInEnum() {
        Set<Term> vocab = new HashSet<Term>();
        vocab.add(DcTerm.creator);
        assertEquals("findTermInEnum should return the expected term", DcTerm.creator, stringToTermConverter.findTermInEnum("creator",vocab));
        assertEquals("findTermInEnum should return the expected term", DcTerm.creator, stringToTermConverter.findTermInEnum("http://purl.org/dc/terms/creator",vocab));
        assertNull("findTermInEnum should return null", stringToTermConverter.findTermInEnum("creatr",vocab));
    }

    @Test
    public void testFindTermInEnumWithPrefixes() {
        Term[] vocab = new Term[] { DcTerm.creator };
        assertEquals("findTermInEnum should return the expected term", DcTerm.creator, stringToTermConverter.findTermInEnum("dc:creator", vocab, new String[] {"dwc:", "dc:"}));
        assertNull("findTermInEnum should return null", stringToTermConverter.findTermInEnum("dc:creator", vocab, new String[] { }));
    }

    @Test
    public void testConvert() {
        assertNull("convert should return null", stringToTermConverter.convert(null));
        assertEquals("convert should return the expected term", DcTerm.creator, stringToTermConverter.convert("creator"));
        assertEquals("convert should return the expected term", DwcTerm.kingdom, stringToTermConverter.convert("kingdom"));
        assertEquals("convert should return the expected term", GbifTerm.depth, stringToTermConverter.convert("depth"));
        assertEquals("convert should return the expected term", SddTerm.termID, stringToTermConverter.convert("termID"));
        assertEquals("convert should return the expected term", SkosTerm.Concept, stringToTermConverter.convert("Concept"));
        assertEquals("convert should return the expected term", Wgs84Term.longitude, stringToTermConverter.convert("longitude"));
        assertEquals("convert should return the expected term", IucnTerm.threatStatus, stringToTermConverter.convert("threatStatus"));
        assertEquals("convert should return the expected term", UnknownTerm.build("http://rs.tdwg.org/ac/terms/commentor"), stringToTermConverter.convert("http://rs.tdwg.org/ac/terms/commentor"));

        boolean thrown = false;
        try {
            stringToTermConverter.convert("http://wibble[@not='allowed']");
        } catch (IllegalArgumentException iae) {
            thrown = true;
        }
        assertTrue("convert should throw an illegal argument exception", thrown);
    }
}
