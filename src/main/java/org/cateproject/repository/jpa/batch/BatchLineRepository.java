package org.cateproject.repository.jpa.batch;

import org.cateproject.domain.batch.BatchLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.SortedSet;

@Repository
public interface BatchLineRepository extends JpaRepository<BatchLine, Long>, JpaSpecificationExecutor<BatchLine> {

    @Query("select l from BatchLine l left join l.file as f left join f.dataset as d where d.identifier = :datasetIdentifier and f.location = :fileLocation and l.lineNumber = :lineNumber")
    public BatchLine findLineByDatasetIdentifierAndFileLocationAndLineNumber(@Param("datasetIdentifier") String datasetIdentifier, @Param("fileLocation") String fileLocation, @Param("lineNumber") Integer lineNumber);

    @Query("select max(l.numberOfColumns) from BatchLine l left join l.file as f where f.id = :fileId")
    public Integer findMaxNumberOfColumnsByFile(@Param("fileId") Long fileId);

    @Query("select l from BatchLine l left join l.file as f left join f.dataset as d where d.identifier = :datasetIdentifier and f.location = :fileLocation and l.lineNumber <= :lineFrom and l.lineNumber >= :lineTo order by l.lineNumber asc")
    public SortedSet<BatchLine> findLinesByDatasetIdentifierAndFileLocationAndLineNumbers(@Param("datasetIdentifier") String datasetIdentifier, @Param("fileLocation") String fileLocation, @Param("lineFrom") Integer lineFrom, @Param("lineTo") Integer lineTo);
}
