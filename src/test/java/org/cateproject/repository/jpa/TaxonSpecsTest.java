package org.cateproject.repository.jpa;

import static org.junit.Assert.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.cateproject.domain.Taxon;
import org.easymock.EasyMock;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

public class TaxonSpecsTest {

	private TaxonSpecs taxonSpecs;
	private Root<Taxon> root;
	private CriteriaQuery<?> query;
	private CriteriaBuilder cb;

	@Before
	public void setUp() throws Exception {
		taxonSpecs = new TaxonSpecs();
		root = EasyMock.createMock(Root.class);
		query = EasyMock.createMock(CriteriaQuery.class);
		cb = EasyMock.createMock(CriteriaBuilder.class);
		
	}

	@Test
	public void testIsRootTaxon() {
		Specification<Taxon> specification = TaxonSpecs.isRootTaxon(Rank.FAMILY);
		Path<Object> parentNameUsagePath = EasyMock.createMock(Path.class);
		Path<Object> taxonRankPath = EasyMock.createMock(Path.class);
		Path<Object> taxonomicStatusPath = EasyMock.createMock(Path.class);
		
		Predicate taxonRankPredicate = EasyMock.createMock(Predicate.class);
		Predicate taxonomicStatusPredicate = EasyMock.createMock(Predicate.class);
		Predicate parentNameUsagePredicate = EasyMock.createMock(Predicate.class);
		Predicate andPredicate = EasyMock.createMock(Predicate.class);
		
		EasyMock.expect(root.get(EasyMock.eq("parentNameUsage"))).andReturn(parentNameUsagePath);
		EasyMock.expect(root.get(EasyMock.eq("taxonomicStatus"))).andReturn(taxonomicStatusPath);
		EasyMock.expect(root.get(EasyMock.eq("taxonRank"))).andReturn(taxonRankPath);
		
		EasyMock.expect(cb.isNull(EasyMock.eq(parentNameUsagePath))).andReturn(parentNameUsagePredicate);
		EasyMock.expect(cb.equal(EasyMock.eq(taxonomicStatusPath), EasyMock.eq(TaxonomicStatus.Accepted))).andReturn(taxonomicStatusPredicate);
		EasyMock.expect(cb.equal(EasyMock.eq(taxonRankPath), EasyMock.eq(Rank.FAMILY))).andReturn(taxonRankPredicate);
		
		
		EasyMock.expect(cb.and(parentNameUsagePredicate, taxonomicStatusPredicate, taxonRankPredicate)).andReturn(andPredicate);
		
		EasyMock.replay(root, query, cb, parentNameUsagePath, taxonRankPath, taxonomicStatusPath, taxonRankPredicate, taxonomicStatusPredicate, parentNameUsagePredicate, andPredicate);
		assertEquals("toPredicate should return expected value", andPredicate, specification.toPredicate(root, query, cb));
		EasyMock.verify(root, query, cb, parentNameUsagePath, taxonRankPath, taxonomicStatusPath, taxonRankPredicate, taxonomicStatusPredicate, parentNameUsagePredicate, andPredicate);
	}

	@Test
	public void testIsChildOfTaxon() {
		Specification<Taxon> specification = TaxonSpecs.isChildOfTaxon(1L);
		Path<Object> parentNameUsagePath = EasyMock.createMock(Path.class);
		Path<Object> parentNameUsageIdPath = EasyMock.createMock(Path.class);
		
		Predicate childOfTaxonPredicate = EasyMock.createMock(Predicate.class);
		
		EasyMock.expect(root.get(EasyMock.eq("parentNameUsage"))).andReturn(parentNameUsagePath);
		EasyMock.expect(parentNameUsagePath.get(EasyMock.eq("id"))).andReturn(parentNameUsageIdPath);
		
		EasyMock.expect(cb.equal(EasyMock.eq(parentNameUsageIdPath), EasyMock.eq(1L))).andReturn(childOfTaxonPredicate);
		
		EasyMock.replay(root, query, cb, parentNameUsagePath, parentNameUsageIdPath, childOfTaxonPredicate);
		assertEquals("toPredicate should return expected value", childOfTaxonPredicate, specification.toPredicate(root, query, cb));
		EasyMock.verify(root, query, cb, parentNameUsagePath, parentNameUsageIdPath, childOfTaxonPredicate);
	}

}
