package org.cateproject.domain.constants;

import org.gbif.dwc.terms.Term;

public enum SkosTerm implements Term
{
	Concept;

	public static final String NS = "http://www.w3.org/2004/02/skos/core#";
	public static final String PREFIX = "skos";

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
