package org.cateproject.batch.job.darwincore;

import org.cateproject.domain.Base;

public class EntityRelationship<T extends Base> {
    private T from;

    private String toIdentifier;

    private EntityRelationshipType type;

    public EntityRelationship(T from, EntityRelationshipType type,
            String toIdentifier) {
        this.from = from;
        this.toIdentifier = toIdentifier;
        this.type = type;
    }

    public final EntityRelationshipType getType() {
        return type;
    }

    public final T getFrom() {
        return from;
    }

    public final String getToIdentifier() {
        return toIdentifier;
    }

}
