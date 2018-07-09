package org.cateproject.batch.multimedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

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
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.batch.integration.launch.JobLaunchRequest;

public class MultimediaFileService {

    private static Logger logger = LoggerFactory.getLogger(MultimediaFileService.class);

    @Value("${temporary.file.directory:#{systemProperties['java.io.tmpdir']}}")
    private FileSystemResource temporaryFileDirectory;

    @Autowired
    private GetResourceClient getResourceClient;

    private Tika tika = new Tika();

    private FFprobe ffprobe = new FFprobe();

    public void setGetResourceClient(GetResourceClient getResourceClient) {
        this.getResourceClient = getResourceClient;
    }

    public void setFFprobe(FFprobe ffprobe) {
        this.ffprobe = ffprobe;
    }

    private static Map<String,String> MIMETYPE_EXTENSION_MAP = new HashMap<String,String>();
	
    private static Map<String,DCMIType> MIMETYPE_DCMITYPE_MAP = new HashMap<String,DCMIType>();

    static {
	MIMETYPE_EXTENSION_MAP.put("image/jpeg", "jpg");
	MIMETYPE_EXTENSION_MAP.put("image/png", "png");
	MIMETYPE_EXTENSION_MAP.put("image/gif", "gif");
	MIMETYPE_EXTENSION_MAP.put("image/bmp", "bmp");
	MIMETYPE_EXTENSION_MAP.put("video/mp4", "mp4");
	MIMETYPE_EXTENSION_MAP.put("audio/mpeg", "mp3");
    }

    static {
	MIMETYPE_DCMITYPE_MAP.put("image/jpeg", DCMIType.StillImage);
	MIMETYPE_DCMITYPE_MAP.put("image/png", DCMIType.StillImage);
	MIMETYPE_DCMITYPE_MAP.put("image/gif", DCMIType.StillImage);
	MIMETYPE_DCMITYPE_MAP.put("image/bmp", DCMIType.StillImage);
	MIMETYPE_DCMITYPE_MAP.put("video/mp4", DCMIType.MovingImage);
	MIMETYPE_DCMITYPE_MAP.put("audio/mpeg", DCMIType.Sound);
    }

    public static String getExtension(String mimeType) {
	return MIMETYPE_EXTENSION_MAP.get(mimeType);
    }

    public static DCMIType getType(String mimeType) {
       return MIMETYPE_DCMITYPE_MAP.get(mimeType); 
    }

    public void setTemporaryFileDirectory(FileSystemResource temporaryFileDirectory) {
        this.temporaryFileDirectory = temporaryFileDirectory;
    }

    public Multimedia remoteFileInfo(String resource) {
        Multimedia multimedia = new Multimedia();
	if(resource.startsWith("http://")) {
	    logger.info("Trying to get last modified for {}", new Object[] {resource});
	    DateTime lastModified = getResourceClient.getLastModified(resource);
            logger.info("Last modified is {}", new Object[] {lastModified});
	    multimedia.setFileLastModified(lastModified);
	}
        return multimedia;
    }

    public Multimedia localFileInfo(File file) {
        Multimedia multimedia = new Multimedia();
	logger.info("Getting image info from {}", new Object[] {file});
	try {
	    Metadata metadata = new Metadata();
	    metadata.set(TikaMetadataKeys.RESOURCE_NAME_KEY, file.getName());	        
	    String mime = tika.detect(new FileInputStream(file), metadata);
	    multimedia.setFormat(mime);
	    multimedia.setSize(file.length());
            multimedia.setType(getType(mime));
	    logger.info("Detected format is {}, type is {}", new Object[] {mime, getType(mime)});
            switch (multimedia.getType()) {
                case StillImage: 
                    try {
	                ImageInfo imageInfo = Sanselan.getImageInfo(file);
	                multimedia.setWidth(imageInfo.getWidth());
	                multimedia.setHeight(imageInfo.getHeight());
	            } catch (ImageReadException ire) {
                        // TODO report exception
	                logger.error(ire.getMessage());
                    }
                    break;
                case MovingImage:
                    FFmpegProbeResult videoResult = ffprobe.probe(file.getAbsolutePath());
                    for(FFmpegStream ffmpegStream : videoResult.getStreams()) {
                        logger.info("Found stream index {} codec_long_name {} codec_name {}", new Object[]{ffmpegStream.index,ffmpegStream.codec_long_name, ffmpegStream.codec_name});
                        if(isVideoCodec(ffmpegStream.codec_name)) {
                            multimedia.setWidth(ffmpegStream.width);
                            multimedia.setHeight(ffmpegStream.height);
                            multimedia.setDuration(ffmpegStream.duration);
                            break;
                        }
                    }
                    break;
                case Sound:
                    FFmpegProbeResult audioResult = ffprobe.probe(file.getAbsolutePath());
                    for(FFmpegStream ffmpegStream : audioResult.getStreams()) {
                        if(isAudioCodec(ffmpegStream.codec_name)) {
                            multimedia.setDuration(ffmpegStream.duration);
                            break;
                        }
                    }
                    break;
                default:
                    // TODO Error condition
                    break;
            }
             
	    FileInputStream fileInputStream = new FileInputStream(file);
	    byte[] buffer = new byte[255 * 3];
	    int read = fileInputStream.read(buffer);
	    multimedia.setHash(new String(buffer,Charset.forName("UTF-8")));
	    multimedia.setLocalFileName(UUID.randomUUID().toString() + "." + getExtension(mime));
	    fileInputStream.close();
	} catch (FileNotFoundException fnfe) {
            // TODO report exception
	    logger.error(fnfe.getMessage());
	} catch (IOException ioe) {
            // TODO report exception 
	    logger.error(ioe.getMessage());
	}
	return multimedia;
    }

    public boolean filesUnchanged(Multimedia m1, Multimedia m2) {
	    if(m1.getHash() != null && m2.getHash() != null && !m1.getHash().isEmpty() && !m2.getHash().isEmpty()) {
	        logger.info("Comparing image file data");
	        return 
	            Objects.equals(m2.getSize(), m1.getSize()) 
                    && Objects.equals(m2.getWidth(), m1.getWidth())
	            && Objects.equals(m2.getHeight(), m1.getHeight())
                    && Objects.equals(m2.getDuration(), m1.getDuration())
	            && Objects.equals(m2.getHash(), m1.getHash());
	        } else {
	            if(m1.getFileLastModified() != null && m2.getFileLastModified() != null) {
	                logger.info("Comparing last modified dates");
	                return m1.getFileLastModified().equals(m2.getFileLastModified());
	     } else {
	        logger.info("Multimedia are different");
	        return false;
	    }
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

    public boolean isVideoCodec(String codec) {
        switch(codec) {
            case "h264":
                return true;
            default:
                return false;
        }
    }

    public boolean isAudioCodec(String codec) {
        switch(codec) {
            case "mp3":
                return true;
            default:
                return false;
        }
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
            } catch (IOException e) {
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

}
