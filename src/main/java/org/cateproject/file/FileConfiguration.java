package org.cateproject.file;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.messaging.MessageHandler;

@Configuration
public class FileConfiguration {

    @Bean
    public FileTransferService fileTransferService() {
        return new DefaultFileTransferServiceImpl();
    }

    @Bean
    @ServiceActivator(inputChannel="localTenantEvents")
    public MessageHandler fileTransferServiceHandler(FileTransferService fileTransferService) {
	return new ServiceActivatingHandler(fileTransferService, "handle");
    }

    @Bean
    public GetResourceClient getResourceClient() {
       return new GetResourceClient();
    }

    
}
