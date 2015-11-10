package org.cateproject.batch.multimedia;

import java.io.File;
import java.util.UUID;

import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.file.FileTransferService;
import org.cateproject.domain.Multimedia;
import org.cateproject.domain.constants.MultimediaFileType;
import org.cateproject.domain.util.MultimediaFile;
import org.cateproject.file.GetResourceClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

public class MultimediaFetchingProcessor implements ItemProcessor<Multimedia, Multimedia> {
	
	private static final Logger logger = LoggerFactory.getLogger(MultimediaFetchingProcessor.class);
	
	@Autowired
	private GetResourceClient getResourceClient;
	
	@Autowired
	private FileTransferService fileTransferService;

        @Autowired
        private MultimediaFileService multimediaFileService;
	
        @Value("${temporary.file.directory:#{systemProperties['java.io.tmpdir']}}")
	private FileSystemResource temporaryFileDirectory;
	
	private String uploadedFile;

	public void setUploadedFile(String uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
	public void setFileTransferService(FileTransferService fileTransferService) {
		this.fileTransferService = fileTransferService;
	}
	
	public void setTemporaryFieDirectory(FileSystemResource temporaryFileDirectory) {
	    this.temporaryFileDirectory = temporaryFileDirectory;
	}

	public void setGetResourceClient(GetResourceClient getResourceClient) {
		this.getResourceClient = getResourceClient;
	}

	@Override
	public Multimedia process(Multimedia multimedia) throws Exception {	
		if(multimedia.getLocalFileName() == null) {		
			if(uploadedFile.lastIndexOf(".") > -1) {
				multimedia.setLocalFileName(UUID.randomUUID() + uploadedFile.substring(uploadedFile.lastIndexOf(".")));
			} else {
				multimedia.setLocalFileName(UUID.randomUUID().toString());
			}
		}
		
		File to = new File(temporaryFileDirectory.getFile(), multimedia.getLocalFileName());
		logger.debug("Fetching multimedia {} to {}", new Object[] {uploadedFile, to.getAbsolutePath()});
		if(multimedia.getIdentifier().startsWith("http://")) {
			DateTime lastModified = getResourceClient.getResource(multimedia.getIdentifier(), null, to);
			multimedia.setFileLastModified(lastModified);
			logger.debug("Fetching {} last modified {}", new Object[] {multimedia.getIdentifier(),lastModified});
		} else {
			logger.debug("Fetching {}",new Object []{uploadedFile});
			fileTransferService.copyFileIn(uploadedFile, to);	
		}
	
                MultimediaFile originalFile = new MultimediaFile(multimedia, to, MultimediaFileType.original);
		Multimedia newMultimedia = multimediaFileService.extractFileInfo(to, new Multimedia());
		if(multimediaFileService.filesUnchanged(multimedia, newMultimedia) && fileTransferService.exists(originalFile.toString())) {
                    logger.info("{} has not changed, skipping",new Object[]{multimedia.getLocalFileName()});
		    return null; // skip
		} else {
                        logger.info("Multimedia has changed, processing further {}", new Object[] {MultitenantContextHolder.getContext().getContextProperty("ProcessingMultimediaFile")});
		        multimedia.getMultimediaFiles().add(originalFile);
                        multimediaFileService.copyFileInfo(newMultimedia, multimedia);
			return multimedia;
		}
	}
}
