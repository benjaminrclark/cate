package org.cateproject.repository.search;

import org.cateproject.domain.Base;

public class BaseRepositoryImpl extends FacetableRepositoryImpl<Base> implements BaseRepository {

    public BaseRepositoryImpl() {
        this.setEntityClass(Base.class);
        this.setIdFieldName("id");
    }

}
