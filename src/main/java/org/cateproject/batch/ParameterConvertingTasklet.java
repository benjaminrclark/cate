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
    	if(jobParameters.containsKey("dwca.fieldsTerminatedBy")) {
    		fieldSeparator = jobParameters.get("dwca.fieldsTerminatedBy");
    	}
    	for(String key : jobParameters.keySet()) {
    		String string = jobParameters.get(key);
    		Object value = null;
    		if(key.endsWith(MAP_SUFFIX)) {
    			Map<String,String> defaultValuesMap = new HashMap<String,String>();
    			for(String defaultValue : string.split(fieldSeparator)) {
    			    if(defaultValue.indexOf("=") > -1) {
    				    String k = defaultValue.substring(0,defaultValue.indexOf("="));
    				    String v = defaultValue.substring(defaultValue.indexOf("=") + 1, defaultValue.length());
    				    defaultValuesMap.put(k,v);
    			    }
    			}
    			value = defaultValuesMap;
    		} else if(key.endsWith(ARRAY_SUFFIX)) {
    			value = string.split(fieldSeparator);
    		} else {
    			value = string;
    		}
    		chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put(key, value);
    	}
        return RepeatStatus.FINISHED;
    }
}
