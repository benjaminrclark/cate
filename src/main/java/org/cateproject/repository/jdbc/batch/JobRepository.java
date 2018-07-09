package org.cateproject.repository.jdbc.batch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class JobRepository {

    private static final Logger logger = LoggerFactory.getLogger(JobRepository.class);

    @Autowired
    private ListableJobLocator listableJobLocator;

    public void setListableJobLocator(ListableJobLocator listableJobLocator) {
        this.listableJobLocator = listableJobLocator;
    }

    public Page<Job> findAll(Pageable pageable) {
        Collection<String> allJobNames = new LinkedHashSet<String>(listableJobLocator.getJobNames());
        List<String> jobNames = null;
	if(pageable.getPageNumber() * pageable.getPageSize() > allJobNames.size()) {
	    jobNames = new ArrayList<String>();
	} else if((pageable.getPageNumber() * pageable.getPageSize()) + pageable.getPageSize() > allJobNames.size()) {
            jobNames = new ArrayList<String>(allJobNames).subList(pageable.getPageNumber() * pageable.getPageSize(), allJobNames.size());
        } else {
             jobNames = new ArrayList<String>(allJobNames).subList(pageable.getPageNumber() * pageable.getPageSize(), (pageable.getPageNumber() * pageable.getPageSize()) + pageable.getPageSize());
        } 

        List<Job> jobs = new ArrayList<Job>();
        for(String jobName : jobNames) {
            try {
                jobs.add(listableJobLocator.getJob(jobName));
            } catch(NoSuchJobException nsje) {
                throw new RuntimeException(nsje);
            }
        }

        return new PageImpl<Job>(jobs, pageable, allJobNames.size());
    }

    public Job findOne(String name) {
        try {
            return listableJobLocator.getJob(name);
        } catch(NoSuchJobException nsje) {
            throw new RuntimeException(nsje);
        }
    }
}
