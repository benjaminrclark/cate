package org.cateproject.domain.convert;

import org.gbif.ecat.voc.NomenclaturalCode;
import org.springframework.core.convert.converter.Converter;


public class StringToNomenclaturalCodeConverter implements Converter<String, NomenclaturalCode> {

	@Override
	public NomenclaturalCode convert(String source) {
		return NomenclaturalCode.fromString(source);
	}

}
