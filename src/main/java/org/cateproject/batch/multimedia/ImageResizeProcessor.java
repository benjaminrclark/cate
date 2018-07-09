package org.cateproject.batch.multimedia;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.cateproject.domain.Multimedia;
import org.cateproject.domain.constants.DCMIType;
import org.cateproject.domain.constants.MultimediaFileType;
import org.cateproject.domain.util.MultimediaFile;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

public class ImageResizeProcessor implements ItemProcessor<Multimedia,Multimedia> {

	private static final Logger logger = LoggerFactory.getLogger(ImageResizeProcessor.class);

        @Autowired
        private ConvertCmd convertCmd;
	
	private Integer resizeX = -1;
	
	private Integer resizeY = -1;
	
	private Character resizeChar = null;
	
	private MultimediaFileType type = MultimediaFileType.large;
	
	private String gravity = null;
	
	private Integer extentX = -1;
	
	private Integer extentY = -1;
	
        @Value("${temporary.file.directory:#{systemProperties['java.io.tmpdir']}}")
	private FileSystemResource temporaryFileDirectory;

        public void setConvertCmd(ConvertCmd convertCmd) {
            this.convertCmd = convertCmd;
        }
	
	public void setTemporaryFileDirectory(FileSystemResource temporaryFileDirectory) {
	    this.temporaryFileDirectory = temporaryFileDirectory;
	}

	public void setResizeX(Integer resizeX) {
	    this.resizeX = resizeX;
	}

	public void setResizeY(Integer resizeY) {
	    this.resizeY = resizeY;
	}
	
	public void setResizeChar(Character resizeChar) {
	    this.resizeChar = resizeChar;
	}

	public void setType(MultimediaFileType type) {
	    this.type = type;
	}

	public void setGravity(String gravity) {
	    this.gravity = gravity;
	}

	public void setExtentX(Integer extentX) {
	    this.extentX = extentX;
	}

	public void setExtentY(Integer extentY) {
	    this.extentY = extentY;
	}

	@Override
	public Multimedia process(Multimedia multimedia) throws Exception {
            if(multimedia.getType() != null && multimedia.getType().equals(DCMIType.StillImage)) {
                logger.debug("Image {} dimensions: {} x {}",new Object[]{multimedia.getLocalFileName(), multimedia.getWidth(), multimedia.getHeight()});

                String extension = multimedia.getLocalFileName().substring(multimedia.getLocalFileName().lastIndexOf(".") + 1);
        	File resizedImage = new File(temporaryFileDirectory.getFile(),UUID.randomUUID().toString() + "." + extension);
                // shrink to no larger than MAX_IMAGE_DIMENSION * MAX_IMAGE_DIMENSION
                IMOperation operation = new IMOperation();
                operation.addImage(multimedia.getOriginalFile().getAbsolutePath());
                if(resizeX > 0 && resizeY > 0) {
            	    if(resizeChar == null) {
	                logger.debug("Resizing to {} * {}", new Object[]{resizeX.intValue(), resizeY.intValue()});
	                operation.resize(resizeX.intValue(), resizeY.intValue());
            	    } else {
	                logger.debug("Resizing to {} * {} {}", new Object[]{resizeX.intValue(), resizeY.intValue(), resizeChar});
	                operation.resize(resizeX.intValue(), resizeY.intValue(), resizeChar);
                    } 
                }
                if(gravity != null) {
                    operation.gravity(gravity);
                }
                if(extentX > 0 && extentY > 0) {
                    logger.debug("Setting the extent to {} * {}", new Object[]{extentX.intValue(),extentY.intValue()});
                    operation.extent(extentX.intValue(), extentY.intValue());
                }
                operation.addImage(resizedImage.getAbsolutePath());
                try {
                    convertCmd.run(operation);
                } catch (IOException ioe) {
                    // TODO Handle exception
                    logger.error("Error resizing image {}", ioe);
                    throw ioe;
                } catch (InterruptedException ie) {
                    // TODO Handle exception
                    logger.error("Error resizing image {}", ie);
                    throw ie;
                } catch (IM4JavaException im4je) {
                    // TODO Handle exception
                    logger.error("Error resizing image {}", im4je);
                    throw im4je;
                }
            
                MultimediaFile multimediaFile = new MultimediaFile(multimedia, resizedImage, type); 
                multimedia.getMultimediaFiles().add(multimediaFile);
            } 
	    return multimedia;
	}
}
