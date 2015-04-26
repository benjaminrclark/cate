package org.cateproject.domain.convert.jpa;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class LanguageConverterTest {
	
	private LanguageConverter languageConverter;

	@Before
	public void setUp() throws Exception {
		languageConverter = new LanguageConverter();
	}

	@Test
	public void testConvertToDatabaseColumn() {
		assertEquals("languageConverter should store the locale as the ISO code",languageConverter.convertToDatabaseColumn(Locale.ENGLISH), "en");
	}

	@Test
	public void testConvertToEntityAttribute() {
		assertEquals("languageConverter should convert the ISO code to a Locale",languageConverter.convertToEntityAttribute("en"), Locale.ENGLISH);
	}

}
