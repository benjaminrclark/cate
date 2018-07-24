package org.cateproject.batch;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.cateproject.file.FileTransferService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

public class ResourceStoringTasklet implements Tasklet {

    private String resource;

    @Autowired
    private FileTransferService fileTransferService; 

    @Value("${temporary.file.directory:#{systemProperties['java.io.tmpdir']}}")
    private FileSystemResource temporaryFileDirectory;

    public ResourceStoringTasklet(String resource) {
        this.resource = resource;
    }

    public void setFileTransferService(FileTransferService fileTransferService) {
        this.fileTransferService = fileTransferService;
    }

    public void setTemporaryFileDirectory(FileSystemResource temporaryFileDirectory) {
        this.temporaryFileDirectory = temporaryFileDirectory;
    }

    /**
     *  Copies this file from its location on the instance which is assumed to be relative to
     *  the temporary directory. Creates a new random filename for the uploaded resource with the same file extension
     *  as the input file. Puts the uri of the resource in 'input.file'
     *  in the job execution context then removes it. 
     */  
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        String extension = FilenameUtils.getExtension(resource);
        String remoteFileName = "upload://" + UUID.randomUUID().toString() + "." + extension;
   
        String localFileName  = temporaryFileDirectory.getFile().getAbsolutePath() + File.separator + resource;
        File localFile = new File(localFileName);
        fileTransferService.copyFileOut(localFile, remoteFileName);
        
        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("input.file",remoteFileName);
        localFile.delete();
        return RepeatStatus.FINISHED;
    }  
}
