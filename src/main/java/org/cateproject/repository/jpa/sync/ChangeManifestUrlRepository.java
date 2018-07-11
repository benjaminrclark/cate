package org.cateproject.repository.jpa.sync;

import org.cateproject.domain.sync.ChangeManifestUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ChangeManifestUrlRepository extends JpaRepository<ChangeManifestUrl, Long>, JpaSpecificationExecutor<ChangeManifestUrl> {

}
