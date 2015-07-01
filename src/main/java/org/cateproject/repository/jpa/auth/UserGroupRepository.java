package org.cateproject.repository.jpa.auth;
import org.cateproject.domain.auth.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long>, JpaSpecificationExecutor<UserGroup>{
	UserGroup findByName(String name);
	
	Page<UserGroup> findByNameStartingWith(String name, Pageable pageable);
}
