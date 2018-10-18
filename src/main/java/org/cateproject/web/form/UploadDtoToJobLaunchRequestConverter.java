package org.cateproject.web.form;

import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.file.FileTransferService;
import org.cateproject.domain.batch.JobLaunchRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

public class UploadDtoToJobLaunchRequestConverter implements Converter<UploadDto, JobLaunchRequest>
{

	Logger logger = LoggerFactory.getLogger(UploadDtoToJobLaunchRequestConverter.class);
	
	Tika tika = new Tika();
	
	@Value("${temporary.file.directory:#{systemProperties['java.io.tmpdir']}}")
        private FileSystemResource temporaryFileDirectory;
	
	@Autowired
	private FileTransferService fileTransferService;

        @Autowired
        private JobRegistry jobRegistry;

        @Autowired
        private ConversionService conversionService;

	private Set<String> priorityMimetypes = new HashSet<String>();

        public UploadDtoToJobLaunchRequestConverter() {
	    priorityMimetypes.add("application/zip");
	    priorityMimetypes.add("text/csv");
	    priorityMimetypes.add("text/tsv");
	    priorityMimetypes.add("application/sdd+xml");
	    priorityMimetypes.add("application/delta-specs-file");
        }

	public JobLaunchRequest convert(UploadDto uploadDto) throws IllegalArgumentException
	{
            try {
	        File temporaryDirectory = new File(temporaryFileDirectory.getFile(),UUID.randomUUID().toString());
	        temporaryDirectory.mkdir();
		
	        File temporaryFile = null;
	        String mimeType = null;
		
                for(MultipartFile multipartFile : uploadDto.getFiles()) {
        	    File file = new File(temporaryDirectory, multipartFile.getOriginalFilename());
            
                    multipartFile.transferTo(file);
                    Metadata metadata = new Metadata();
                    metadata.set(TikaMetadataKeys.RESOURCE_NAME_KEY, multipartFile.getOriginalFilename());
                    String mime = tika.detect(new FileInputStream(file), metadata);
                    logger.info("MimeType " + mime + " " + file.toString());
                    if(mimeType == null) {
            	        mimeType = mime;
            	        temporaryFile = file;
                    } else if(!priorityMimetypes.contains(mimeType)) {
            	        mimeType = mime;
            	        temporaryFile = file;
                    }  
                }

                logger.info("MimeType " + mimeType + " " + temporaryFile.toString());

                switch(mimeType) {
                    case "application/zip":
                    return convertToProcessDarwinCoreArchiveJob(temporaryFile);
                    default:
                        throw new IllegalArgumentException("Could not process uploaded files(s), unsupported format");
                }
            } catch (IOException ioe) {
                throw new IllegalArgumentException("Could not process uploaded file(s)", ioe);
            } catch (NoSuchJobException nsje) {
                throw new IllegalArgumentException("Could not process uploaded file(s)", nsje);
            }
	}
  
        public JobLaunchRequest convertToProcessDarwinCoreArchiveJob(File file) throws IOException, NoSuchJobException {
                Job job = jobRegistry.getJob("processDarwinCoreArchive");
		String uri = "upload://" + UUID.randomUUID().toString() + ".zip";
                String jobLaunchRequestIdentifier = UUID.randomUUID().toString();
		fileTransferService.copyFileOut(file, uri);

		Map<String, JobParameter> jobParametersMap = new HashMap<String, JobParameter>();
		jobParametersMap.put("input.file", new JobParameter(uri));
		jobParametersMap.put("tenant.id", new JobParameter(MultitenantContextHolder.getContext().getTenantId()));
		jobParametersMap.put("user.id", new JobParameter(SecurityContextHolder.getContext().getAuthentication().getName()));
		jobParametersMap.put("working.dir", new JobParameter(UUID.randomUUID().toString()));
		jobParametersMap.put("launch.request.identifier", new JobParameter(jobLaunchRequestIdentifier));
		Map<String, Object> tenantProperties = new HashMap<String, Object>();
		jobParametersMap.put("tenant.properties", new JobParameter(conversionService.convert(tenantProperties, String.class)));
		JobParameters jobParameters = new JobParameters(jobParametersMap);
                JobLaunchRequest jobLaunchRequest = new JobLaunchRequest(job, jobParameters);
                jobLaunchRequest.setIdentifier(jobLaunchRequestIdentifier);
                return jobLaunchRequest;
        }
}
