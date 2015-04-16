package org.cateproject.multitenant;

import org.springframework.util.Assert;

public class MultitenantContextHolder {

    private static ThreadLocal<MultitenantContext> contextHolder = new ThreadLocal<MultitenantContext>();

    public static void setContext(MultitenantContext context) {
        Assert.notNull(context, "Only non-null TenantContext instances are permitted");
        contextHolder.set(context);
    }
    
    public static MultitenantContext getContext() {
        if (contextHolder.get() == null) {
            contextHolder.set(new MultitenantContextImpl());
        }

        return (MultitenantContext) contextHolder.get();
    }
}
