package org.cateproject.web.multitenant.theme;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.junit.Before;

public class MapBackedMessageSourceTest {
	
	private MapBackedMessageSource mapBackedMessageSource;
	
	@Before
	public void setUp() {
		Map<String, String> messages = new HashMap<String, String>();
		messages.put("foo", "value");
		mapBackedMessageSource = new MapBackedMessageSource(messages);
	}

	@Test
	public void testResolveCodeStringLocale() {
		assertNotNull("mapBackedMessageSource should return a messageFormat for a valid code", mapBackedMessageSource.resolveCode("foo", Locale.getDefault()));
	}

	@Test
	public void testReturnNullForInvalidCode() {
		assertNull("mapBackedMessageSource should return a messageFormat for an invalid code", mapBackedMessageSource.resolveCode("bar", Locale.getDefault()));
	}
}
