package org.cateproject.repository.jpa.admin;

import org.cateproject.domain.admin.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TenantRepository extends JpaRepository<Tenant, Long>,  JpaSpecificationExecutor<Tenant> {

}
