package org.cateproject.batch.multimedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.cateproject.batch.JobLaunchRequestHandler;
import org.cateproject.domain.Multimedia;
import org.cateproject.domain.constants.DCMIType;
import org.cateproject.file.FileTransferService;
import org.cateproject.file.GetResourceClient;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.batch.integration.launch.JobLaunchRequest;

public class MultimediaFileService {

    private static Logger logger = LoggerFactory.getLogger(MultimediaFileService.class);

    @Value("${temporary.file.directory}")
    private FileSystemResource temporaryFileDirectory;

    @Autowired
    private FileTransferService fileTransferService;

    @Autowired
    private GetResourceClient getResourceClient;

    @Autowired
    @Qualifier("processMultimedia")
    private Job processMultimediaJob;

    @Autowired
    @Qualifier("remoteJobLaunchRequests")
    private JobLaunchRequestHandler jobLaunchRequestHandler;

    Tika tika = new Tika();

    private static Map<String,String> MIMETYPE_EXTENSION_MAP = new HashMap<String,String>();
	
    private static Map<String,DCMIType> MIMETYPE_DCMITYPE_MAP = new HashMap<String,DCMIType>();

    static {
	MIMETYPE_EXTENSION_MAP.put("image/jpeg", "jpg");
	MIMETYPE_EXTENSION_MAP.put("image/png", "png");
	MIMETYPE_EXTENSION_MAP.put("image/gif", "gif");
	MIMETYPE_EXTENSION_MAP.put("image/bmp", "bmp");
    }

    public static String getExtension(String mimeType) {
	return MIMETYPE_EXTENSION_MAP.get(mimeType);
    }
	
    public DCMIType getType(String mimeType) {
       return MIMETYPE_DCMITYPE_MAP.get(mimeType); 
    }

    public Multimedia extractFileInfo(File file, Multimedia multimedia) {
        if(file != null) {
	    logger.info("Getting image info from {}", new Object[] {file});
	    try {
	        Metadata metadata = new Metadata();
	        metadata.set(TikaMetadataKeys.RESOURCE_NAME_KEY, file.getName());	        
	        String mime = tika.detect(new FileInputStream(file), metadata);
	        multimedia.setFormat(mime);
	        multimedia.setSize(file.length());
                multimedia.setType(getType(mime));
         
                switch (multimedia.getType()) {
                    case StillImage: 
	                ImageInfo imageInfo = Sanselan.getImageInfo(file);
	                multimedia.setWidth(imageInfo.getWidth());
	                multimedia.setHeight(imageInfo.getHeight());
                        break;
                    case MovingImage:
                    case Sound:
                        break;
                    default:
                        break;
                }
                 
	        FileInputStream fileInputStream = new FileInputStream(file);
	        byte[] buffer = new byte[255 * 3];
	        int read = fileInputStream.read(buffer);
	        multimedia.setHash(new String(buffer,Charset.forName("UTF-8")));
	        multimedia.setLocalFileName(UUID.randomUUID().toString() + "." + getExtension(mime));
	        fileInputStream.close();
	    } catch (FileNotFoundException fnfe) {
	        logger.error(fnfe.getMessage());
	    } catch (ImageReadException ire) {
	        logger.error(ire.getMessage());
	    } catch (IOException ioe) {
	        logger.error(ioe.getMessage());
	    }
	} else {
	    if(multimedia.getIdentifier() != null && multimedia.getIdentifier().startsWith("http://")) {
	        logger.info("Trying to get last modified for {}", new Object[] {multimedia.getIdentifier()});
	        DateTime lastModified = getResourceClient.getLastModified(multimedia.getIdentifier());
                logger.info("Last modified is {}", new Object[] {lastModified});
	        multimedia.setFileLastModified(lastModified);
	     }
	}
	return multimedia;
    }

    public boolean imageUnchanged(Multimedia m1, Multimedia m2) {
	    if(m1.getHash() != null && m2.getHash() != null && !m1.getHash().isEmpty() && !m2.getHash().isEmpty()) {
	        logger.info("Comparing image file data");
	        return 
	            m2.getSize().equals(m1.getSize()) 
                    && m2.getWidth().equals(m1.getWidth())
	            && m2.getHeight().equals(m1.getHeight())
	            && m2.getHash().equals(m1.getHash());
	        } else {
	            if(m1.getFileLastModified() != null && m2.getFileLastModified() != null) {
	                logger.info("Comparing last modified dates");
	                return m1.getFileLastModified().equals(m2.getFileLastModified());
	     } else {
	        logger.info("Images are different");
	        return false;
	    }
	}
    }

    public boolean videoUnchanged(Multimedia m1, Multimedia m2) {
	    if(m1.getHash() != null && m2.getHash() != null && !m1.getHash().isEmpty() && !m2.getHash().isEmpty()) {
	        logger.info("Comparing image file data");
	        return 
	            m2.getSize().equals(m1.getSize()) 
                    && m2.getWidth().equals(m1.getWidth())
	            && m2.getHeight().equals(m1.getHeight())
                    && m2.getDuration().equals(m1.getDuration())
	            && m2.getHash().equals(m1.getHash());
	        } else {
	            if(m1.getFileLastModified() != null && m2.getFileLastModified() != null) {
	                logger.info("Comparing last modified dates");
	                return m1.getFileLastModified().equals(m2.getFileLastModified());
	     } else {
	        logger.info("Images are different");
	        return false;
	    }
	}
    }

    public boolean audioUnchanged(Multimedia m1, Multimedia m2) {
	    if(m1.getHash() != null && m2.getHash() != null && !m1.getHash().isEmpty() && !m2.getHash().isEmpty()) {
	        logger.info("Comparing image file data");
	        return 
	            m2.getSize().equals(m1.getSize()) 
                    && m2.getDuration().equals(m1.getDuration())
	            && m2.getHash().equals(m1.getHash());
	        } else {
	            if(m1.getFileLastModified() != null && m2.getFileLastModified() != null) {
	                logger.info("Comparing last modified dates");
	                return m1.getFileLastModified().equals(m2.getFileLastModified());
	     } else {
	        logger.info("Images are different");
	        return false;
	    }
	}
    }
    public boolean filesUnchanged(Multimedia m1, Multimedia m2) {
        if(m1.getType() == m2.getType()) {
            switch (m1.getType()) {
                case StillImage:
                    return imageUnchanged(m1,m2);
                case MovingImage:
                    return videoUnchanged(m1,m2);
                case Sound:
                    return audioUnchanged(m1,m2);
                default:
                    return false;
            }
        } else {
            logger.info("Multimedia are different types ({}, {})", new Object[]{m1.getType(), m2.getType()});
            return false;
        }
    }

    public void copyFileInfo(Multimedia from, Multimedia to) {
        to.setSize(from.getSize());
        to.setWidth(from.getWidth());
        to.setHeight(from.getHeight());
        to.setDuration(from.getDuration());
        to.setFileLastModified(from.getFileLastModified());
        to.setHash(from.getHash());
    }

    public void postRemove(Multimedia multimedia) {

    }

    public void handleFile(Multimedia multimedia) {

    	MultipartFile multipartFile = multimedia.getMultipartFile();
        if (multipartFile != null && !multipartFile.isEmpty()) {
            logger.info("Processing multipart file");
            String extension = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
            File temporaryFile = new File(temporaryFileDirectory.getFile().getAbsolutePath() + File.separatorChar + UUID.randomUUID().toString() + "." + extension);
            
            try {
                multipartFile.transferTo(temporaryFile);
                multimedia.setOriginalFile(temporaryFile);
            } catch (Exception e) {
            	logger.error("Error transfering multipart file to {}", new Object[]{temporaryFile}, e);
                throw new RuntimeException(e);
            }
            logger.info("Successfully transferred {} to {}", new Object[]{multipartFile.getOriginalFilename(),temporaryFile});
        } else {
            logger.info("Trying to resolve non-multipart image");
            String dwcArchiveDir = (String)MultitenantContextHolder.getContext().getContextProperty("DwcArchiveDir");
            if(dwcArchiveDir != null) { // try to resolve image locally
            	logger.info("Detecting multimedia in exploded DwC Archive");
            	File dwcArchive = new File(dwcArchiveDir);
            	File localFile = new File(dwcArchive, multimedia.getIdentifier());
            	if(localFile.exists()) {
            	    multimedia.setOriginalFile(localFile);
                    logger.info("Successfully located {} in {}", new Object[]{multimedia.getIdentifier(),localFile.getAbsolutePath()});
            	}
            }
            if(multimedia.getOriginalFile() == null && multimedia.getIdentifier() != null && multimedia.getIdentifier().startsWith("http://")) {
            	logger.info("Multimedia identifier starts with http://");
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Multimedia process(Multimedia multimedia) {
	logger.info("Processing {}", new Object[]{multimedia.getIdentifier()});
	Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
	if(multimedia.getOriginalFile() != null) {
		String inputFile = "upload://" + multimedia.getLocalFileName();
                try {
		    fileTransferService.copyFileOut(multimedia.getOriginalFile(), inputFile);
                } catch (IOException ioe) {
                    logger.error("Could not copy file {} out : {}", multimedia.getOriginalFile(), ioe.getMessage());
                    throw new RuntimeException(ioe);
                }
		jobParametersMap.put("input.file",new JobParameter(inputFile));
	} else if(multimedia.getIdentifier() != null && multimedia.getIdentifier().startsWith("http://")) {
		jobParametersMap.put("input.file", new JobParameter(multimedia.getIdentifier()));
		jobParametersMap.put("random.string", new JobParameter(UUID.randomUUID().toString()));
	} else {
		return multimedia;
	}
	jobParametersMap.put("query.string", new JobParameter("select m from Multimedia m where m.identifier = :identifier"));
    	jobParametersMap.put("query.parameters_map", new JobParameter("identifier=" + multimedia.getIdentifier()));
        jobParametersMap.put("tenant.id", new JobParameter(MultitenantContextHolder.getContext().getTenantId()));
        jobParametersMap.put("user.id", new JobParameter(SecurityContextHolder.getContext().getAuthentication().getName()));
        JobParameters jobParameters = new JobParameters(jobParametersMap);
        JobLaunchRequest jobLaunchRequest = new JobLaunchRequest(processMultimediaJob, jobParameters);
        jobLaunchRequestHandler.launch(jobLaunchRequest);
	return multimedia;
		
	}

    public void prePersist(Multimedia multimedia) {
    	if(!MultitenantContextHolder.getContext().getContextBoolean("ProcessingMultimediaFile")) {
    		logger.debug("prePersist handling file");
    		handleFile(multimedia);
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
                Multimedia newMultimedia = extractFileInfo(multimedia.getOriginalFile(), multimedia);
		if(filesUnchanged(multimedia, newMultimedia)) {
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
}
