package org.cateproject.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.messaging.MessageHandler;

@Configuration
public class FileConfiguration {


    @Autowired
    private FileTransferService fileTransferService;

    @Bean
    @ServiceActivator(inputChannel="localTenantEvents")
    public MessageHandler localFileTransferServiceHandler() {
	return new ServiceActivatingHandler(fileTransferService, "handle");
    }

    @Bean
    @ServiceActivator(inputChannel="remoteTenantEvents")
    public MessageHandler remoteFileTransferServiceHandler() {
	return new ServiceActivatingHandler(fileTransferService, "notify");
    }
}
