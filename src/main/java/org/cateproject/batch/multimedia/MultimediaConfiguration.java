package org.cateproject.batch.multimedia;

import org.im4java.core.ConvertCmd;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultimediaConfiguration {
 
    @Value("${imagemagick.search.path:/usr/bin")
    private String imageMagickSearchPath;

    @Bean
    public ConvertCmd convertCmd() {
        ConvertCmd convertCmd = new ConvertCmd();
        convertCmd.setSearchPath(imageMagickSearchPath);
        return convertCmd;
    }

    @Bean
    public MultimediaHandler multimediaHandler() {
        return new MultimediaHandler();
    }

    @Bean
    public MultimediaFileService multimediaFileService() {
        return new MultimediaFileService();
    }
}
