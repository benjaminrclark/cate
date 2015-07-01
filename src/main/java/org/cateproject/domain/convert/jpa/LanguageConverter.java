package org.cateproject.domain.convert.jpa;

import java.util.Locale;

import javax.persistence.AttributeConverter;

public class LanguageConverter implements AttributeConverter<Locale, String> {

	@Override
	public String convertToDatabaseColumn(Locale locale) {
		return locale.getLanguage();
	}

	@Override
	public Locale convertToEntityAttribute(String string) {
		return new Locale(string);
	}

}
