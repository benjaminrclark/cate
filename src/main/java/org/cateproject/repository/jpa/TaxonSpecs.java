package org.cateproject.repository.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.cateproject.domain.Taxon;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.springframework.data.jpa.domain.Specification;

public class TaxonSpecs {
	
	public static Specification<Taxon> isRootTaxon(final Rank rank) {
		return new Specification<Taxon>() {

			@Override
			public Predicate toPredicate(Root<Taxon> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {        
		        	return criteriaBuilder.and(
		                criteriaBuilder.isNull(root.get("parentNameUsage")),
		                criteriaBuilder.equal(root.get("taxonomicStatus"), TaxonomicStatus.Accepted),
		                criteriaBuilder.equal(root.get("taxonRank"), rank)
		            );            
			}
			
		};
		
	}
	
	public static Specification<Taxon> isChildOfTaxon(final Long id) {
		return new Specification<Taxon>() {

			@Override
			public Predicate toPredicate(Root<Taxon> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {        
		            return criteriaBuilder.equal(root.get("parentNameUsage").get("id"), id);
			}
			
		};
		
	}

}
