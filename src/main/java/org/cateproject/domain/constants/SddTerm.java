package org.cateproject.domain.constants;

import org.gbif.dwc.terms.Term;


public enum SddTerm implements Term
{
	Node,
	applicableIfID,
	inapplicableIfID,
	parentID,
	termID,
	parameters,
	Dataset;

	public static final String NS = "http://rs.tdwg.org/UBIF/2006/";
	public static final String PREFIX = "sdd";

	@Override
	public String qualifiedName()
	{
		return NS + name();
	}

	@Override
	public String simpleName()
	{
		return name();
	}

	@Override
	public String toString()
	{
		return PREFIX + ":" + name();
	}
}
