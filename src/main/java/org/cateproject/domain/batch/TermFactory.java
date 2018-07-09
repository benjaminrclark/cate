package org.cateproject.domain.batch;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.IucnTerm;
import org.gbif.dwc.terms.UnknownTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TermFactory {
	
	Logger logger = LoggerFactory.getLogger(TermFactory.class);

	private static final Pattern QUOTE_PATTERN = Pattern.compile("\"");
	private final Set<Term> unkownTerms = new HashSet<Term>();

	public static String normaliseTerm(String term) {
		// no quotes or whitespace in term names
		term = StringUtils.strip(StringUtils.trim(QUOTE_PATTERN.matcher(term)
				.replaceAll("")));
		return term.toLowerCase();
	}

	public void clear() {
		unkownTerms.clear();
	}

	public Term findTerm(String termName) {
		if (termName == null) {
			return null;
		}
		// normalise terms
		String normTermName = normaliseTerm(termName);
		//logger.debug("Term " + termName + " normalized to " + normTermName);
		// try known term enums first
		Term term = findTermInEnum(normTermName, DwcTerm.values(),
				new String[] {DwcTerm.PREFIX + ":", DwcTerm.NS});
		if (term == null) {
			term = findTermInEnum(normTermName, DcTerm.values(),
					new String[] {DcTerm.PREFIX + ":", DcTerm.NS});
		}
		if (term == null) {
			term = findTermInEnum(normTermName, GbifTerm.values(),
					new String[] {GbifTerm.PREFIX + ":", GbifTerm.NS});
		}
		if (term == null) {
			term = findTermInEnum(normTermName, IucnTerm.values(),
					new String[] {IucnTerm.PREFIX + ":", IucnTerm.NS});
		}
		if (term == null) {
			term = findTermInEnum(normTermName, Wgs84Term.values(),
					new String[] {Wgs84Term.PREFIX + ":", Wgs84Term.NS});
		}
		if (term == null) {
			term = findTermInEnum(normTermName, SddTerm.values(),
					new String[] {SddTerm.PREFIX + ":", SddTerm.NS});
		}
		if (term == null) {
			term = findTermInEnum(normTermName, SkosTerm.values(),
					new String[] {SkosTerm.PREFIX + ":", SkosTerm.NS});
		}
		if (term == null) {
			term = findTermInEnum(normTermName, unkownTerms);
		}
		// DwcTerm.classs does not parse its own .toString
		if(termName.equals("dwc:classs")) {
			return DwcTerm.class_;
		}
		if (term == null) {
			term = UnknownTerm.build(termName, StringUtils.substringAfterLast(
					termName, "/"));
			unkownTerms.add(term);
		}
		
		//logger.debug("Returning " + term + " of class " + term.getClass().getSimpleName());
		
		return term;
	}

	private Term findTermInEnum(String termName,
			Collection<Term> vocab) {
		for (Term term : vocab) {
			if (term.qualifiedName().equalsIgnoreCase(termName)) {
				return term;
			}
			if (term.simpleName().equalsIgnoreCase(termName)) {
				return term;
			}
		}
		return null;
	}

	private Term findTermInEnum(String termName, Term[] vocab,
			String[] prefixes) {
		for (String prefix : prefixes) {
			if (termName.startsWith(prefix)) {
				termName = termName.substring(prefix.length());
				break;
			}
		}
		return findTermInEnum(termName, Arrays.asList(vocab));
	}

}
