package org.cateproject.web.batch;

import org.cateproject.domain.batch.JobLaunchRequest;
import org.cateproject.repository.jpa.batch.JobLaunchRequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/batch/launch")
public class JobLaunchRequestController {

    @Autowired
    private JobLaunchRequestRepository jobLaunchRequestRepository;

    public void setJobLaunchRequestRepository(JobLaunchRequestRepository jobLaunchRequestRepository) {
        this.jobLaunchRequestRepository = jobLaunchRequestRepository;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        JobLaunchRequest jobLaunchRequest = jobLaunchRequestRepository.findOne(id);

        if(jobLaunchRequest.getJobExecutionId() != null) {
            return "redirect:/batch/execution/" + jobLaunchRequest.getJobExecutionId();
        } else {
            uiModel.addAttribute("result", jobLaunchRequest);
            return "batch/launch/show";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public JobLaunchRequest getJson(@PathVariable("id") Long id) {
        return jobLaunchRequestRepository.findOne(id);
    }
}
