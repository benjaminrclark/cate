package org.cateproject.domain;

import javax.persistence.MappedSuperclass;

import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Audited
@MappedSuperclass
public abstract class NonOwnedEntity extends Base implements NonOwned {	
	
	public String toString() {
		return "Searchable<" + this.getId() + ">";
    }	
}
