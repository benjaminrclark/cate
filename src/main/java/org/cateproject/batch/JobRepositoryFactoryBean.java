package org.cateproject.batch;

import org.springframework.batch.core.repository.dao.JobInstanceDao;

public class JobRepositoryFactoryBean extends org.springframework.batch.core.repository.support.JobRepositoryFactoryBean {

    public JobInstanceDao getJobInstanceDao() {
        try {
             return super.createJobInstanceDao();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
