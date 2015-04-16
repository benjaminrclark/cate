package org.cateproject.multitenant;



import java.util.List;

import org.cateproject.multitenant.domain.Multitenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MultitenantRepository {
	
	Page<Multitenant> findAll(Pageable page);

	Multitenant findByIdentifier(String identifier);
	
	Multitenant findOne(Long id);

	Multitenant save(Multitenant obj);

	void flush();

	long count();

	List<Multitenant> findAll();

	void delete(Multitenant obj);

}
