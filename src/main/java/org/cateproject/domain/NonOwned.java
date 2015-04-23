package org.cateproject.domain;

import java.util.List;

public interface NonOwned {
	
	public void setTaxa(List<Taxon> taxa);
	
	public List<Taxon> getTaxa();

}
