package org.cateproject.file;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("default")
@Configuration
public class LocalFileConfiguration {

    @Bean
    public FileTransferService fileTransferService() {
        return new DefaultFileTransferServiceImpl();
    }
}
