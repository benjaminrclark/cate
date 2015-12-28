package org.cateproject.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.cateproject.domain.constants.DCMIType;
import org.cateproject.domain.constants.MultimediaFileType;
import org.cateproject.multitenant.MultitenantContextHolder;
import org.cateproject.multitenant.event.MultitenantEvent;
import org.cateproject.multitenant.event.MultitenantEventAwareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.DigestUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.BucketPolicy;
import com.amazonaws.services.s3.model.BucketWebsiteConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.SetBucketPolicyRequest;
import com.amazonaws.util.IOUtils;

public class S3FileTransferService implements FileTransferService, MultitenantEventAwareService {

        private static Logger logger = LoggerFactory.getLogger(S3FileTransferService.class);

        @Autowired
        private AmazonS3 amazonS3;

        private String uploadBucketArn;

        private String region;

        private ClassPathResource indexResource = new ClassPathResource("org/cateproject/file/index.html");

        private ClassPathResource errorResource = new ClassPathResource("org/cateproject/file/error.html");

        public void setAmazonS3(AmazonS3 amazonS3) {
            this.amazonS3 = amazonS3;
        }

        public void setUploadBucketArn(String uploadBucketArn) {
          this.uploadBucketArn = uploadBucketArn;
        }

        public void setRegion(String region) {
            this.region = region;
        }


        protected void stringToFile(String fileName, File file) throws IOException {
                GetObjectRequest getObjectRequest = null;
		if(fileName.startsWith("static://")) {
			getObjectRequest = new GetObjectRequest("static-" + MultitenantContextHolder.getContext().getTenantId(),fileName.substring(9));
		} else if(fileName.startsWith("upload://")) {
			getObjectRequest = new GetObjectRequest(uploadBucketArn,fileName.substring(9));
		} else {
                        throw new IllegalArgumentException("key must start with static:// or upload://");
		}
                logger.debug("Copy {} to {}", new Object[]{getObjectRequest.getKey(), file.getAbsolutePath()});
                amazonS3.getObject(getObjectRequest,file);
        }

        protected String fileToString(File file, String fileName) {
            PutObjectRequest putObjectRequest = null;
            if(fileName.startsWith("static://")) {
	        putObjectRequest = new PutObjectRequest("static-" + MultitenantContextHolder.getContext().getTenantId(),fileName.substring(9),file);
	    } else if(fileName.startsWith("upload://")) {
	        putObjectRequest = new PutObjectRequest(uploadBucketArn,fileName.substring(9), file);
	    } else {
                throw new IllegalArgumentException("key must start with static:// or upload://");
	    }

            logger.debug("Copying {} to {}", new Object[]{file.getAbsolutePath(), putObjectRequest.getBucketName()});
	    PutObjectResult result = amazonS3.putObject(putObjectRequest);
            return result.getETag();
        }

        protected ObjectMetadata getObjectMetadata(InputStream inputStream) throws IOException {
            byte[] content = IOUtils.toByteArray(inputStream);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("text/html");
            objectMetadata.setContentEncoding("UTF-8");
            objectMetadata.setContentLength(content.length);
            byte[] digest = DigestUtils.md5Digest(content);
            String md5 = new String(Base64.getEncoder().encode(digest));
            objectMetadata.setContentMD5(md5);
            return objectMetadata;
        }

        public void handle(MultitenantEvent multitenantEvent) {
		logger.info("TenantEvent recieved");
		switch(multitenantEvent.getType()) {
		case CREATE:
			String bucketName = "static-" + multitenantEvent.getIdentifier();
                        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName,region);
                        amazonS3.createBucket(createBucketRequest);
                        try {
                            ObjectMetadata indexMetadata = getObjectMetadata(indexResource.getInputStream());
                            PutObjectRequest rootRequest = new PutObjectRequest(bucketName, "index.html", indexResource.getInputStream(), indexMetadata);
                            amazonS3.putObject(rootRequest);
                            ObjectMetadata errorMetadata = getObjectMetadata(errorResource.getInputStream());
                            PutObjectRequest errorRequest = new PutObjectRequest(bucketName, "error.html", errorResource.getInputStream(), errorMetadata);
                            amazonS3.putObject(errorRequest);

                            for(DCMIType type : DCMIType.values()) {
                                String multimediaTypeDirectory  = bucketName + "/" + type.toString();
                                PutObjectRequest multimediaTypeRequest = new PutObjectRequest(bucketName, multimediaTypeDirectory + "/index.html", indexResource.getInputStream(), indexMetadata);
                                amazonS3.putObject(multimediaTypeRequest);
                                for(MultimediaFileType multimediaType : type.getMultimediaFileTypes()) {
                                     String multimediaFileTypeDirectory = multimediaTypeDirectory + "/" + multimediaType.toString();
                                     PutObjectRequest multimediaFileTypeRequest = new PutObjectRequest(bucketName, multimediaFileTypeDirectory + "/index.html", indexResource.getInputStream(), indexMetadata);
                                     amazonS3.putObject(multimediaFileTypeRequest);
                                }
                            }
                        } catch(IOException ioe) {
                            logger.error("Error creating ObjectMetadata {}", new Object[]{ioe});
                        }
                        BucketWebsiteConfiguration bucketWebsiteConfiguration = new BucketWebsiteConfiguration("index.html","error.html");
                        amazonS3.setBucketWebsiteConfiguration(bucketName, bucketWebsiteConfiguration);
                        SetBucketPolicyRequest bucketPolicyRequest = new SetBucketPolicyRequest(bucketName, "{\"Version\":\"2015-12-22\",\"Statement\":[{\"Sid\":\"PublicReadForGetBucketObjects\",\"Effect\":\"Allow\",\"Principal\": \"*\",\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::"+ bucketName + "/*\"]}]}");
                        amazonS3.setBucketPolicy(bucketPolicyRequest);
                        // TODO Configure Route53 Recordsets
			logger.info("Static directory for tenant {} ({}) has been created successfully", new Object[]{ multitenantEvent.getIdentifier(), bucketName});
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
            String result = fileToString(from, toName);
            from.delete();
	    return result;
        }
	
	public void moveFileIn(String fromName, File to) throws IOException {
		stringToFile(fromName, to);
                delete(fromName);
        }

	public String copyFileOut(File from, String toName) throws IOException {
	    return fileToString(from,toName);
        }
	
	public void copyFileIn(String fromName, File to) throws IOException {
            stringToFile(fromName, to);
        }

	public void copyDirectoryIn(String from, File to) throws IOException {

        }
	
	public void copyDirectoryOut(File from, String to) throws IOException {

        }
	
	public boolean delete(String fileName) throws IOException {
            DeleteObjectRequest deleteObjectRequest = null;
            if(fileName.startsWith("static://")) {
	        deleteObjectRequest = new DeleteObjectRequest(MultitenantContextHolder.getContext().getTenantId(),fileName.substring(9));
	    } else if(fileName.startsWith("upload://")) {
	        deleteObjectRequest = new DeleteObjectRequest(uploadBucketArn,fileName.substring(9));
	    } else {
                throw new IllegalArgumentException("key must start with static:// or upload://");
	    }

            logger.debug("Deleting {} {}", new Object[]{deleteObjectRequest.getBucketName(), deleteObjectRequest.getKey()});
	    amazonS3.deleteObject(deleteObjectRequest);
            return true;
        }

	public boolean exists(String string) throws IOException {
            return false;
        }
}
