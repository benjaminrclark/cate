package org.cateproject.batch.job.darwincore;

import org.cateproject.domain.Base;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;

public class ProcessorException extends RuntimeException {
    
    Set<ConstraintViolation> constraintViolations = new HashSet<ConstraintViolation>();

    public Set<ConstraintViolation> getConstraintViolations() {
        return constraintViolations;
    }

    public  void setConstraintViolations(Set constraintViolations) {
        this.constraintViolations = (Set<ConstraintViolation>)constraintViolations;
    }

}
