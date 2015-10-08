package org.cateproject.domain.convert.solr;

import org.apache.solr.common.SolrDocument;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class JobExecutionReadingConverter implements Converter<SolrDocument, JobExecution> {
	
	@Autowired
	private JobExplorer jobExplorer;
	

	@Override
	public JobExecution convert(SolrDocument solrDocument) {
		if(solrDocument.getFieldValue("base.class_s").equals("org.springframework.batch.core.JobExecution")) {
		    Long id = (Long)solrDocument.getFieldValue("base.id_l");
		    return jobExplorer.getJobExecution(id);
		} else {
			return null;
		}
    }

}
