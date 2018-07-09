package org.cateproject.batch.job.darwincore.reference;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.cateproject.domain.Reference;
import org.cateproject.batch.job.darwincore.NonOwnedProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Processor extends NonOwnedProcessor<Reference>  {

	private Logger logger = LoggerFactory.getLogger(Processor.class);
	
	public Processor() {
   	    super(Reference.class);
    }

	@Override
	protected void doUpdate(Reference persisted, Reference t) {
		persisted.setBibliographicCitation(t.getBibliographicCitation());
		persisted.setDate(t.getDate());
		persisted.setDescription(t.getDescription());
		persisted.setSource(t.getSource());
		persisted.setSubject(t.getSubject());
		persisted.setTaxonRemarks(t.getTaxonRemarks());
		persisted.setTitle(t.getTitle());
		persisted.setType(t.getType());		
	}

	@Override
	protected void doPersist(Reference t) {
		
	}

	@Override
	protected void doResolve(Reference t, Set<ConstraintViolation<Reference>> constraintViolations) {
		
	}

}
