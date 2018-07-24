package org.cateproject.batch.job.darwincore;

import org.apache.commons.lang3.ObjectUtils;
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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other.getClass().equals(this.getClass())) {
            EntityRelationship entityRelationship = (EntityRelationship) other;
            return ObjectUtils.equals(this.getToIdentifier(), entityRelationship.getToIdentifier())
                   && ObjectUtils.equals(this.getType(), entityRelationship.getType())
                   && ObjectUtils.equals(this.getFrom(), entityRelationship.getFrom());
        } else{
            return false;
        }
    }
}
