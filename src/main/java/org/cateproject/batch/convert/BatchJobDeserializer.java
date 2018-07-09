package org.cateproject.batch.convert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class BatchJobDeserializer extends JsonDeserializer<JobLaunchRequest>{

      private static Logger logger = LoggerFactory.getLogger(BatchJobDeserializer.class);

      @Autowired
      private JobLocator jobLocator;

      public void setJobLocator(JobLocator jobLocator) {
          this.jobLocator = jobLocator;
      }

    public JobLaunchRequest deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JsonParseException, IOException {
        JsonNode jobLaunchRequestNode = jsonParser.getCodec().readTree(jsonParser);
        
        JsonNode parametersNode =  jobLaunchRequestNode.get("parameters");
        Map<String, JobParameter> jobParametersMap = new HashMap<String,JobParameter>();
        for(Iterator<String> parameterNames = parametersNode.fieldNames(); parameterNames.hasNext();) {
            String parameterName = parameterNames.next();
            logger.debug("Parameter Name {}", new Object[]{parameterName});
            JsonNode parameterNode = parametersNode.get(parameterName); 
            JobParameter jobParameter = new JobParameter(parameterNode.get("value").asText(),parameterNode.get("identifying").asBoolean());
            jobParametersMap.put(parameterName,jobParameter);        
        }
        JobParameters jobParameters = new JobParameters(jobParametersMap);
        String jobName = jobLaunchRequestNode.get("job").asText();
        try {
            Job job = jobLocator.getJob(jobName);
            JobLaunchRequest jobLaunchRequest = new JobLaunchRequest(job, jobParameters);
            return jobLaunchRequest;
        } catch (NoSuchJobException nsje) {
            throw new JsonParseException("Could not find job '" + jobName + "'", jsonParser.getCurrentLocation(), nsje);
        }
    }
}
