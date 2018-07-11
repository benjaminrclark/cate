package org.cateproject.domain.convert;

import org.gbif.ecat.voc.TaxonomicStatus;
import org.springframework.core.convert.converter.Converter;


public class StringToTaxonomicStatusConverter implements Converter<String, TaxonomicStatus> {

	@Override
	public TaxonomicStatus convert(String source) {
		return TaxonomicStatus.fromString(source);
	}

}
