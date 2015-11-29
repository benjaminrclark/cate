package org.cateproject.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.cateproject.domain.constants.DCMIType;
import org.cateproject.domain.constants.MultimediaFileType;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.multitenant.event.MultitenantEvent;
import org.cateproject.multitenant.event.MultitenantEventAwareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

public class DefaultFileTransferServiceImpl implements FileTransferService, MultitenantEventAwareService, InitializingBean {

        private static Logger logger = LoggerFactory.getLogger(DefaultFileTransferServiceImpl.class);

        @Value("${upload.file.directory:#{'./upload/'}}")
	private FileSystemResource uploadFileDirectory;
	
        @Value("${static.file.directory:#{'./static/'}}")
	private FileSystemResource staticFileDirectory;
	
        public void setUploadFileDirectory(FileSystemResource uploadFileDirectory) {
            this.uploadFileDirectory = uploadFileDirectory;
        }

        public void setStaticFileDirectory(FileSystemResource staticFileDirectory) {
            this.staticFileDirectory = staticFileDirectory;
        }

	public File stringToFile(String fileName) {
		
		File file = null;
		
		if(fileName.startsWith("static://")) {
			File staticDirectory = new File(staticFileDirectory.getFile(),MultitenantContextHolder.getContext().getTenantId());
			file = new File(staticDirectory,fileName.substring(9));
		} else if(fileName.startsWith("upload://")) {
			file = new File(uploadFileDirectory.getFile(),fileName.substring(9));
		} else {
			file = new File(fileName);
		}
		
		return file;
	}

        public void afterPropertiesSet() {
             if(!uploadFileDirectory.getFile().exists()) {
                boolean successful = uploadFileDirectory.getFile().mkdirs();
                if(successful) {
		    logger.info("Upload directory ({}) has been created successfully", new Object[]{ uploadFileDirectory.getFile().getAbsolutePath()});
                } else {
		    logger.error("Upload directory ({}) was not created successfully ", new Object[]{ uploadFileDirectory.getFile().getAbsolutePath()});
                }
             }
        }

        public void handle(MultitenantEvent multitenantEvent) {
		logger.info("TenantEvent recieved");
		switch(multitenantEvent.getType()) {
		case CREATE:
			File staticTenantDirectory = new File(staticFileDirectory.getFile(),multitenantEvent.getIdentifier());
                        boolean successful = staticTenantDirectory.mkdirs();
                        if(successful) {
			    logger.info("Static directory for tenant {} ({}) has been created successfully", new Object[]{ multitenantEvent.getIdentifier(), staticTenantDirectory.getAbsolutePath()});
                        } else {
			    logger.error("Static directory for tenant {} ({}) was not created successfully ", new Object[]{ multitenantEvent.getIdentifier(), staticTenantDirectory.getAbsolutePath()});
                        }
                        if(staticTenantDirectory.exists()) {
                            for(DCMIType type : DCMIType.values()) {
                               File multimediaTypeDirectory  = new File(staticTenantDirectory, type.toString());
                               for(MultimediaFileType multimediaType : type.getMultimediaFileTypes()) {
                                   File multimediaFileTypeDirectory = new File(multimediaTypeDirectory, multimediaType.toString());
                                   multimediaFileTypeDirectory.mkdirs();
                               }
                            }
                        }
			break;
		case DELETE:	
			break;
		default:
			 break;
		}
        }
 
        public void notify(MultitenantEvent multitenantEvent) {
        }

	public String moveFileOut(File from, String toName) throws IOException {
		File to = stringToFile(toName);
		if(!from.equals(to)) {
                    logger.debug("Copying {} to {}", new Object[]{from.getAbsolutePath(), to.getAbsolutePath()});
	            Files.move(from.toPath(),to.toPath());
		}
		return to.getAbsolutePath();
        }
	
	public void moveFileIn(String fromName, File to) throws IOException {
		File from = stringToFile(fromName);
                logger.debug("Move {} to {} to exists {}", new Object[]{from.getAbsolutePath(), to.getAbsolutePath(), to.exists()});
		if(!to.exists()) {
		    Files.move(from.toPath(), to.toPath());
		}		
        }

	public String copyFileOut(File from, String toName) throws IOException {
		File to = stringToFile(toName);
		if(!from.equals(to)) {
                    logger.debug("Copying {} to {}", new Object[]{from.getAbsolutePath(), to.getAbsolutePath()});
	            Files.copy(from.toPath(),to.toPath());
		}
		return to.getAbsolutePath();
        }
	
	public void copyFileIn(String fromName, File to) throws IOException {
		File from = stringToFile(fromName);
                logger.debug("Copying {} to {} to exists {}", new Object[]{from.getAbsolutePath(), to.getAbsolutePath(), to.exists()});
		if(!to.exists()) {
		    Files.copy(from.toPath(), to.toPath());
		}		
        }

	public void copyDirectoryIn(String from, File to) throws IOException {

        }
	
	public void copyDirectoryOut(File from, String to) throws IOException {

        }
	
	public boolean delete(String file) throws IOException {
	    File fileToDelete = stringToFile(file);
            return fileToDelete.delete();
        }

	public boolean exists(String string) throws IOException {
            return false;
        }
}
