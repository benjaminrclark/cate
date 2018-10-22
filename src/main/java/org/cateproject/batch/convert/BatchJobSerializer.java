package org.cateproject.batch.convert;

import java.io.IOException;

import org.cateproject.domain.batch.JobLaunchRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


public class BatchJobSerializer extends JsonSerializer<JobLaunchRequest>{
 
    private static Logger logger = LoggerFactory.getLogger(BatchJobSerializer.class);

    public void serialize(JobLaunchRequest value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        generator.writeStartObject();
        generator.writeFieldName("job");
        generator.writeString(value.getJob().getName());
        generator.writeFieldName("parameters");
        generator.writeStartObject();
        JobParameters jobParameters = value.getJobParameters();
        logger.debug("Parameter names are {}", new Object[]{jobParameters.getParameters().keySet()});        
        for(String parameterName : jobParameters.getParameters().keySet()) {
            JobParameter jobParameter = jobParameters.getParameters().get(parameterName);
            generator.writeFieldName(parameterName);
            generator.writeStartObject();
            generator.writeFieldName("value");
            generator.writeString(jobParameter.getValue().toString());
            generator.writeFieldName("identifying");
            generator.writeBoolean(jobParameter.isIdentifying());
            generator.writeEndObject();
        }
        generator.writeEndObject();
        generator.writeFieldName("jobExecution");
        if(value.getJobExecutionId() == null) {
            generator.writeNull();
        } else {
            generator.writeNumber(value.getJobExecutionId());
        }
        generator.writeEndObject();
    }

}
