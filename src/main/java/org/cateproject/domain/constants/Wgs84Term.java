package org.cateproject.domain.constants;

import org.gbif.dwc.terms.Term;

public enum Wgs84Term implements Term
{
	latitude,
	longitude;

	public static final String NS = "http://www.w3.org/2003/01/geo/wgs84_pos#";
	public static final String PREFIX = "wgs84";

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
