package org.cateproject.batch.job.darwincore;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;

public class ProcessorException extends RuntimeException {
    
    Set constraintViolations = new HashSet<ConstraintViolation>();

    public Set getConstraintViolations() {
        return constraintViolations;
    }

    public  void setConstraintViolations(Set constraintViolations) {
        this.constraintViolations = constraintViolations;
    }

}
