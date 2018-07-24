package org.cateproject.domain.convert;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cateproject.domain.constants.SddTerm;
import org.cateproject.domain.constants.SkosTerm;
import org.cateproject.domain.constants.Wgs84Term;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.IucnTerm;
import org.gbif.dwc.terms.UnknownTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class StringToTermConverter implements Converter<String, Term>
{

	Logger logger = LoggerFactory.getLogger(StringToTermConverter.class);

	private static final Pattern QUOTE_PATTERN = Pattern.compile("\"");

	public static final String normaliseTerm(String term)
	{
		// no quotes or whitespace in term names
		term = StringUtils.strip(StringUtils.trim(QUOTE_PATTERN.matcher(term)
		                         .replaceAll("")));
		return term.toLowerCase();
	}

	public Term convert(String termName) throws IllegalArgumentException
	{
		if (termName == null)
		{
			return null;
		}
		// try known term enums first
		Term term = findTermInEnum(termName, DwcTerm.values(),
		                                  new String[] {DwcTerm.PREFIX + ":", DwcTerm.NS});
		if (term == null)
		{
			term = findTermInEnum(termName, DcTerm.values(),
			                      new String[] {DcTerm.PREFIX + ":", DcTerm.NS});
		}
		if (term == null)
		{
			term = findTermInEnum(termName, GbifTerm.values(),
			                      new String[] {GbifTerm.PREFIX + ":", GbifTerm.NS});
		}
		if (term == null)
		{
			term = findTermInEnum(termName, IucnTerm.values(),
			                      new String[] {IucnTerm.PREFIX + ":", IucnTerm.NS});
		}
		if (term == null)
		{
			term = findTermInEnum(termName, Wgs84Term.values(),
			                      new String[] {Wgs84Term.PREFIX + ":", Wgs84Term.NS});
		}
		if (term == null)
		{
			term = findTermInEnum(termName, SddTerm.values(),
			                      new String[] {SddTerm.PREFIX + ":", SddTerm.NS});
		}
		if (term == null)
		{
			term = findTermInEnum(termName, SkosTerm.values(),
			                      new String[] {SkosTerm.PREFIX + ":", SkosTerm.NS});
		}
		if (term == null)
		{
			try {
                            term = new UnknownTerm(new URI(termName));
                        } catch (URISyntaxException urise) {
                            throw new IllegalArgumentException(urise);
                        }
		}

		return term;
	}


	protected Term findTermInEnum(String termName,
	                                   Collection<Term> vocab)
	{
		for (Term term : vocab)
		{
                    if(term.qualifiedName().equals(termName)) {
                        return term;
                    }
                    if(term.simpleName().equals(termName)) {
                        return term;
                    }
		}
		return null;
	}

	protected Term findTermInEnum(String termName, Term[] vocab,
	                                   String[] prefixes)
	{
		for (String prefix : prefixes)
		{
			if (termName.startsWith(prefix))
			{
				termName = termName.substring(prefix.length());
				break;
			}
		}
		return findTermInEnum(termName, Arrays.asList(vocab));
	}

}
