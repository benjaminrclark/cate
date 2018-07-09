package org.cateproject.batch;

import org.cateproject.file.FileTransferService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

public class InputFileCleanupTasklet implements Tasklet {

    private String inputFile;

    @Autowired
    private FileTransferService fileTransferService;

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void setFileTransferService(FileTransferService fileTransferService) {
        this.fileTransferService = fileTransferService;
    }

    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        fileTransferService.delete(inputFile);
        return RepeatStatus.FINISHED;
    }
}
