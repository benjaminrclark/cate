package org.cateproject.web.batch;

import java.util.ArrayList;
import java.util.List;

import org.cateproject.repository.jdbc.batch.JobInstanceRepository;
import org.cateproject.repository.jdbc.batch.JobRepository;
import org.cateproject.repository.search.batch.JobExecutionRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/batch/job")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobInstanceRepository jobInstanceRepository;

    @Autowired JobExecutionRepository jobExecutionRepository;

    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public void setJobInstanceRepository(JobInstanceRepository jobInstanceRepository) {
        this.jobInstanceRepository = jobInstanceRepository;
    }

    public void setJobExecutionRepository(JobExecutionRepository jobExecutionRepository) {
        this.jobExecutionRepository = jobExecutionRepository;
    }

    @RequestMapping(produces = "text/html")
    public String list(Pageable pageable, Model uiModel) {        
        Page<Job> results = jobRepository.findAll(pageable);
        uiModel.addAttribute("results", results);
        return "batch/job/list";
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.GET, produces = "text/html")
    public String show(@PathVariable("name") String name, Model uiModel, Pageable pageable) {
        uiModel.addAttribute("result", jobRepository.findOne(name));

        List<JobInstanceInfo> jobInstanceList = new ArrayList<JobInstanceInfo>();
        Page<JobInstance> jobInstances = jobInstanceRepository.findAll(pageable, name);
        for(JobInstance jobInstance : jobInstances.getContent()) {
           JobInstanceInfo jobInstanceInfo = new JobInstanceInfo();
           jobInstanceInfo.setLastJobExecution(new JobExecutionInfo(jobExecutionRepository.findOneByJobInstanceOrderByStartTimeDesc(jobInstance)));
           jobInstanceInfo.setJobExecutionCount(jobExecutionRepository.countByJobInstance(jobInstance));
           jobInstanceList.add(jobInstanceInfo);
        }

        uiModel.addAttribute("jobInstances", new PageImpl<JobInstanceInfo>(jobInstanceList, pageable, jobInstances.getTotalElements()));
        return "batch/job/show";
    }
}
