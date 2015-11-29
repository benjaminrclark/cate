package org.cateproject.batch.multimedia;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.cateproject.batch.JobLaunchRequestHandler;
import org.cateproject.domain.Multimedia;
import org.cateproject.file.FileTransferService;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class MultimediaHandler {

    private static final Logger logger = LoggerFactory.getLogger(MultimediaHandler.class);

    @Autowired
    private FileTransferService fileTransferService;

    @Autowired
    MultimediaFileService multimediaFileService;

    @Autowired
    @Qualifier("processMultimedia")
    private Job processMultimediaJob;

    @Autowired
    @Qualifier("remoteJobLaunchRequests")
    private JobLaunchRequestHandler jobLaunchRequestHandler;

    @Autowired
    @Qualifier("conversionService")
    private ConversionService conversionService;

    public void setFileTransferService(FileTransferService fileTransferService) {
        this.fileTransferService = fileTransferService;
    }

    public void setMultimediaFileService(MultimediaFileService multimediaFileService) {
        this.multimediaFileService = multimediaFileService;
    }

    public void setProcessMultimediaJob(Job processMultimediaJob) {
        this.processMultimediaJob = processMultimediaJob;
    }

    public void setJobLaunchRequestHandler(JobLaunchRequestHandler jobLaunchRequestHandler) {
        this.jobLaunchRequestHandler = jobLaunchRequestHandler;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Multimedia process(Multimedia multimedia) {
	logger.info("Processing {}", new Object[]{multimedia.getIdentifier()});
	Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
	if(multimedia.getOriginalFile() != null) {
                String extension = multimedia.getOriginalFile().getName().substring(multimedia.getOriginalFile().getName().lastIndexOf(".") + 1);
		String inputFile = "upload://" + UUID.randomUUID().toString() + "." + extension;
                try {
		    fileTransferService.moveFileOut(multimedia.getOriginalFile(), inputFile);
                } catch (IOException ioe) {
                    logger.error("Could not move file {} out : {}", multimedia.getOriginalFile(), ioe.getMessage());
                    throw new RuntimeException(ioe);
                }
		jobParametersMap.put("input.file",new JobParameter(inputFile));
	} else if(multimedia.getIdentifier() != null && multimedia.getIdentifier().startsWith("http://")) {
		jobParametersMap.put("random.string", new JobParameter(UUID.randomUUID().toString()));
	} else {
		return multimedia;
	}
	jobParametersMap.put("query.string", new JobParameter("select m from Multimedia m where m.identifier = :identifier"));
    	jobParametersMap.put("query.parameters_map", new JobParameter("identifier=" + multimedia.getIdentifier()));
        jobParametersMap.put("tenant.id", new JobParameter(MultitenantContextHolder.getContext().getTenantId()));
        jobParametersMap.put("user.id", new JobParameter(SecurityContextHolder.getContext().getAuthentication().getName()));
        Map<String,Object> tenantProperties = new HashMap<String, Object>();
        tenantProperties.put("ProcessingMultimediaFile", Boolean.TRUE);
        jobParametersMap.put("tenant.properties", new JobParameter(conversionService.convert(tenantProperties,String.class)));
        JobParameters jobParameters = new JobParameters(jobParametersMap);
        JobLaunchRequest jobLaunchRequest = new JobLaunchRequest(processMultimediaJob, jobParameters);
        jobLaunchRequestHandler.launch(jobLaunchRequest);
	return multimedia;
		
	}

    public void prePersist(Multimedia multimedia) {
    	if(!MultitenantContextHolder.getContext().getContextBoolean("ProcessingMultimediaFile")) {
    		logger.debug("prePersist handling file");
    		multimediaFileService.handleFile(multimedia);
    	} else {
    		logger.debug("prePersist doing nothing");
    	}
    }

    public void postPersist(Multimedia multimedia) {
    	if(!MultitenantContextHolder.getContext().getContextBoolean("ProcessingMultimediaFile")) {
	    	logger.info("postPersist processing file");
	        process(multimedia);
    	} else {
    		logger.debug("postPersist doing nothing");
    	}
    }

    public void postUpdate(Multimedia multimedia) {
    	if(!MultitenantContextHolder.getContext().getContextBoolean("ProcessingMultimediaFile")) {
	    	logger.info("postPersist processing file");
                Multimedia newMultimedia = null;
                if(multimedia.getOriginalFile() != null) {
                    newMultimedia = multimediaFileService.localFileInfo(multimedia.getOriginalFile());
                } else {
                    newMultimedia = multimediaFileService.remoteFileInfo(multimedia.getIdentifier());
                }
		if(multimediaFileService.filesUnchanged(multimedia, newMultimedia)) {
		    logger.info("Files are the same, not processing");
		    // Do nothing as we assume that the file is the same
		} else {
                    logger.info("Files have changed, processing");
	            process(multimedia);
                }
    	} else {
    		logger.debug("postUpdate doing nothing");
    	}
    }

    public void postRemove(Multimedia multimedia) {

    }
}
