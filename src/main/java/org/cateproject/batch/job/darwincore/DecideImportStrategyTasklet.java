package org.cateproject.batch.job.darwincore;

import org.cateproject.domain.batch.BatchDataset;
import org.cateproject.repository.jpa.batch.BatchDatasetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

public class DecideImportStrategyTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(DecideImportStrategyTasklet.class);

    private String datasetIdentifier;

    @Autowired
    private BatchDatasetRepository batchDatasetRepository;

    public DecideImportStrategyTasklet(String datasetIdentifier) {
        this.datasetIdentifier = datasetIdentifier;
    }

    public void setBatchDatasetRepository(BatchDatasetRepository batchDatasetRepository) {
        this.batchDatasetRepository = batchDatasetRepository;
    }

    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        BatchDataset batchDataset = batchDatasetRepository.findByIdentifier(datasetIdentifier); 

        if (batchDataset == null) { // Should not happen
            stepContribution.setExitStatus(new ExitStatus("DATASET NOT FOUND")); 
        } else {
            stepContribution.setExitStatus(new ExitStatus("VALID CHANGE DUMP MANIFEST"));
        }
        return RepeatStatus.FINISHED; 
    }
}
