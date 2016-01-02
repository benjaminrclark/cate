package org.cateproject.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.core.region.RegionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("aws")
@Configuration
public class FileAWSConfiguration {

        @Autowired
        private RegionProvider regionProvider;

        @Value("${cloudformation.uploadBucketArn}")
        private String uploadBucketArn;

        @Bean
        FileTransferService fileTransferService() {
            S3FileTransferService s3FileTransferService = new S3FileTransferService();
            s3FileTransferService.setUploadBucketArn(uploadBucketArn);
            s3FileTransferService.setRegion(regionProvider.getRegion().getName());
            return s3FileTransferService;
        }
}
