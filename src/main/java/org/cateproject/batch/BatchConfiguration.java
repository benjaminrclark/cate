package org.cateproject.batch;

import javax.sql.DataSource;

import org.cateproject.batch.job.ProcessDarwinCoreArchiveConfiguration;
import org.cateproject.batch.job.ProcessMultimediaConfiguration;
import org.springframework.batch.core.configuration.support.AutomaticJobRegistrar;
import org.springframework.batch.core.configuration.support.DefaultJobLoader;
import org.springframework.batch.core.configuration.annotation.AbstractBatchConfiguration;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collection;

@Configuration
//@EnableBatchProcessing(modular = true)
public class BatchConfiguration extends AbstractBatchConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private AutomaticJobRegistrar registrar = new AutomaticJobRegistrar();

    private BatchConfigurer getCustomConfigurer() {
        DefaultBatchConfigurer configurer = new DefaultBatchConfigurer(dataSource);
	configurer.initialize();
        return configurer;
    }

    @Override
    @Bean
    public JobRepository jobRepository() throws Exception {
	return getCustomConfigurer().getJobRepository();
    }

    @Override
    @Bean
    public JobLauncher jobLauncher() throws Exception {
	return getCustomConfigurer().getJobLauncher();
    }

    @Override
    @Bean
    public JobExplorer jobExplorer() throws Exception {
	return getCustomConfigurer().getJobExplorer();
    }

    @Override
    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
         return this.transactionManager;
    }

    @Bean
    public AutomaticJobRegistrar jobRegistrar() throws Exception {
	registrar.setJobLoader(new DefaultJobLoader(jobRegistry()));
	/*for (ApplicationContextFactory factory : context.getBeansOfType(ApplicationContextFactory.class).values()) {
	    registrar.addApplicationContextFactory(factory);
	}*/
        registrar.addApplicationContextFactory(processDarwinCoreArchiveConfiguration());
	registrar.addApplicationContextFactory(processMultimediaConfiguration());
	return registrar;
    }

    @Bean
    public ApplicationContextFactory processDarwinCoreArchiveConfiguration() {
        return new GenericApplicationContextFactory(ProcessDarwinCoreArchiveConfiguration.class);
    }

    @Bean
    public ApplicationContextFactory processMultimediaConfiguration() {
        return new GenericApplicationContextFactory(ProcessMultimediaConfiguration.class);
    }
}
