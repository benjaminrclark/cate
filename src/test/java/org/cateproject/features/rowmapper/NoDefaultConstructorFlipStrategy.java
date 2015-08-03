package org.cateproject.features.rowmapper;

import org.ff4j.core.FeatureStore;
import org.ff4j.core.FlippingExecutionContext;
import org.ff4j.strategy.AbstractFlipStrategy;

public class NoDefaultConstructorFlipStrategy extends AbstractFlipStrategy {

     public NoDefaultConstructorFlipStrategy(String arg) {

     }

     public boolean evaluate(String featureName, FeatureStore store, FlippingExecutionContext executionContext) {
         return false;
     }

}
