package org.cateproject.batch.multimedia;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultimediaConfiguration {
 
    @Bean
    public MultimediaHandler multimediaHandler() {
        return new MultimediaHandler();
    }
}
