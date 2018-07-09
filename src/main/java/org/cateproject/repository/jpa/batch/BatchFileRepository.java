package org.cateproject.repository.jpa.batch;

import org.cateproject.domain.batch.BatchFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchFileRepository extends JpaRepository<BatchFile, Long>, JpaSpecificationExecutor<BatchFile> {
}
