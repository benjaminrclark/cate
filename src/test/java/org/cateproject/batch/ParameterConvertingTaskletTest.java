package org.cateproject.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.test.MetaDataInstanceFactory;

public class ParameterConvertingTaskletTest {

    private ParameterConvertingTasklet parameterConvertingTasklet;

    private ChunkContext chunkContext;

    private StepContribution stepContribution;

    @Before
    public void setUp() {
        parameterConvertingTasklet = new ParameterConvertingTasklet();
        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        chunkContext = new ChunkContext(new StepContext(stepExecution));
        stepContribution = new StepContribution(stepExecution);
    }

    @Test
    public void testExecute() throws Exception {
        Map<String, String> jobParameters = new HashMap<String,String>();
        jobParameters.put("PARAMETER_1_KEY", "PARAMETER_1_VALUE");
        jobParameters.put("PARAMETER_2_KEY_array","PARAMETER_2_VALUE_1\tPARAMETER_2_VALUE_2");
        jobParameters.put("PARAMETER_3_KEY_map","PARAMETER_3_VALUE_1_KEY=PARAMETER_3_VALUE_1_VALUE\tPARAMETER_3_VALUE_2_KEY=PARAMETER_3_VALUE_2_VALUE\tPARAMETER_3_VALUE_3_KEY:PARAMETER_3_VALUE_3_VALUE");
        parameterConvertingTasklet.setJobParameters(jobParameters);

        Map<String, Object> expectedExecutionContext = new HashMap<String,Object>();
        expectedExecutionContext.put("PARAMETER_1_KEY", "PARAMETER_1_VALUE");
        expectedExecutionContext.put("PARAMETER_2_KEY", new String[] {"PARAMETER_2_VALUE_1","PARAMETER_2_VALUE_2"});
        Map<String, String> parameter3Value = new HashMap<String,String>();
        parameter3Value.put("PARAMETER_3_VALUE_1_KEY","PARAMETER_3_VALUE_1_VALUE");
        parameter3Value.put("PARAMETER_3_VALUE_2_KEY","PARAMETER_3_VALUE_2_VALUE");
        expectedExecutionContext.put("PARAMETER_3_KEY",parameter3Value);
        parameterConvertingTasklet.execute(stepContribution,chunkContext);    
        assertEquals("executionContext should be set with the correct value for 'PARAMETER_1_KEY'",expectedExecutionContext.get("PARAMETER_1_KEY"), chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("PARAMETER_1_KEY"));
        assertArrayEquals("executionContext should be set with the correct value for 'PARAMETER_2_KEY'",(Object[])expectedExecutionContext.get("PARAMETER_2_KEY"), (Object[])chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("PARAMETER_2_KEY"));
        assertEquals("executionContext should be set with the correct value for 'PARAMETER_3_KEY'",expectedExecutionContext.get("PARAMETER_3_KEY"), chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("PARAMETER_3_KEY"));
        
    }
}
