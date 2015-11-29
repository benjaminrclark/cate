package org.cateproject.batch;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class ParameterConvertingTasklet implements Tasklet {

    private Logger logger = LoggerFactory.getLogger(ParameterConvertingTasklet.class);
    
    private Map<String, String> jobParameters;
	
    private String fieldSeparator = "	";
    
    private static String MAP_SUFFIX = "_map";
    
    private static String ARRAY_SUFFIX = "_array";

    public void setJobParameters(Map<String, String> jobParameters) {
        this.jobParameters = jobParameters;
    }

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.debug("jobParameters = {}", new Object[]{jobParameters});
    	for(String key : jobParameters.keySet()) {
    		String string = jobParameters.get(key);
    		Object value = null;
    		if(key.endsWith(MAP_SUFFIX)) {
                        key = key.substring(0, key.length() - MAP_SUFFIX.length());
    			Map<String,String> valueMap = new HashMap<String,String>();
    			for(String keyValue : string.split(fieldSeparator)) {
    			    if(keyValue.indexOf("=") > -1) {
    				    String k = keyValue.substring(0, keyValue.indexOf("="));
    				    String v = keyValue.substring(keyValue.indexOf("=") + 1, keyValue.length());
    				    valueMap.put(k,v);
    			    }
    			}
    			value = valueMap;
    		} else if(key.endsWith(ARRAY_SUFFIX)) {
                        key = key.substring(0, key.length() - ARRAY_SUFFIX.length());
    			value = string.split(fieldSeparator);
    		} else {
    			value = string;
    		}
    		chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put(key, value);
    	}
        return RepeatStatus.FINISHED;
    }
}
