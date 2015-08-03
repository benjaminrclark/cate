package org.cateproject.features.rowmapper;

import org.ff4j.core.FeatureStore;
import org.ff4j.core.FlippingExecutionContext;
import org.ff4j.strategy.AbstractFlipStrategy;

public class NoVisibleConstructorFlipStrategy extends AbstractFlipStrategy {

     private NoVisibleConstructorFlipStrategy() {

     }

     public boolean evaluate(String featureName, FeatureStore store, FlippingExecutionContext executionContext) {
         return false;
     }
}
