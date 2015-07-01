package org.cateproject.domain;

import javax.persistence.MappedSuperclass;

import org.hibernate.envers.Audited;

@Audited
@MappedSuperclass
public abstract class OwnedEntity extends Base implements Owned {

}
