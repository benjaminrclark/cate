package org.cateproject.repository.jdbc.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class JobInstanceRepository {
    private static Logger logger = LoggerFactory.getLogger(JobInstanceRepository.class);

    private JobInstanceDao jobInstanceDao;

    public void setJobInstanceDao(JobInstanceDao jobInstanceDao) {
        this.jobInstanceDao = jobInstanceDao;
    }

    public Page<JobInstance> findAll(Pageable pageable, String jobName) {
        try {
            int count = jobInstanceDao.getJobInstanceCount(jobName);
            List<JobInstance> jobInstances = jobInstanceDao.getJobInstances(jobName, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
	    return new PageImpl<JobInstance>(jobInstances, pageable, count);
        } catch(NoSuchJobException nsje) {
            logger.error("findAll job instances could not find instances for {}", new Object[]{jobName});
            throw new RuntimeException(nsje);
        }
    }
}
