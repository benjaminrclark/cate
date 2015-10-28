package org.cateproject.file;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileConfiguration {

    @Bean
    public FileTransferService fileTransferService() {
        return new DefaultFileTransferServiceImpl();
    }

    @Bean
    public GetResourceClient getResourceClient() {
       return new GetResourceClient();
    }

    
}
