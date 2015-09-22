package org.cateproject.multitenant.solr;

import java.util.HashSet;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.cateproject.domain.Base;
import org.cateproject.domain.convert.solr.BaseReadingConverter;
import org.cateproject.domain.convert.solr.DatasetWritingConverter;
import org.cateproject.domain.convert.solr.IdentificationKeyWritingConverter;
import org.cateproject.domain.convert.solr.MappingSolrConverterFactoryBean;
import org.cateproject.domain.convert.solr.MultimediaWritingConverter;
import org.cateproject.domain.convert.solr.ReferenceWritingConverter;
import org.cateproject.domain.convert.solr.TaxonWritingConverter;
import org.cateproject.domain.convert.solr.TermWritingConverter;
import org.cateproject.multitenant.MultitenantRepository;
import org.cateproject.repository.search.BaseRepository;
import org.cateproject.repository.search.BaseRepositoryImpl;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.convert.MappingSolrConverter;
import org.springframework.data.solr.core.convert.SolrConverter;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.repository.query.SolrEntityInformation;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.messaging.MessageHandler;

@Configuration
@EnableSolrRepositories
public class MultitenantSolrAutoConfiguration {

        @Autowired
        MultitenantRepository multitenantRepository;

	@Bean
	@ConfigurationProperties(prefix = "solr")
	public SolrServer solrServer() {
	    MultitenantAwareSolrServer multitenantAwareSolrServer = new MultitenantAwareSolrServer() ;
            multitenantAwareSolrServer.setMultitenantRepository(multitenantRepository);
	    return multitenantAwareSolrServer;
	}

	@Bean
	@ServiceActivator(inputChannel="localTenantEvents")
	public MessageHandler tenantSolrServerHandle() {
		return new ServiceActivatingHandler(solrServer(), "handle");
	}
	
	@Bean
	@ServiceActivator(inputChannel="incomingTenantEvents")
	public MessageHandler tenantSolrServerNotify() {
		return new ServiceActivatingHandler(solrServer(), "notify");
	}

        @Bean
        public Converter<SolrDocument, Base> baseReadingConverter() {
            return new BaseReadingConverter();
        }

        @Bean
        public FactoryBean<MappingSolrConverter> solrConverterFactoryBean() {
             Set<Converter<SolrDocument, ? extends Object>> readingConverters = new HashSet<Converter<SolrDocument, ? extends Object>>();
             readingConverters.add(baseReadingConverter());
         
             Set<Converter<? extends Object, SolrInputDocument>> writingConverters = new HashSet<Converter<? extends Object, SolrInputDocument>>();
             writingConverters.add(new DatasetWritingConverter());
             writingConverters.add(new IdentificationKeyWritingConverter());
             writingConverters.add(new TaxonWritingConverter());
             writingConverters.add(new MultimediaWritingConverter());
             writingConverters.add(new ReferenceWritingConverter());
             writingConverters.add(new TermWritingConverter());
         
             MappingSolrConverterFactoryBean solrConverterFactoryBean = new MappingSolrConverterFactoryBean();
             solrConverterFactoryBean.setReadingConverters(readingConverters);
             solrConverterFactoryBean.setWritingConverters(writingConverters);
             return solrConverterFactoryBean;
        }

        public SolrConverter solrConverter() {
            try {
                return solrConverterFactoryBean().getObject();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Bean
        public SolrOperations solrTemplate() throws Exception {
            SolrTemplate solrTemplate = new SolrTemplate(solrServer());
            solrTemplate.setSolrConverter(solrConverter());
            return solrTemplate;
        }

        @Bean
        public BaseRepository baseRepository() throws Exception {
            BaseRepositoryImpl baseRepository = new BaseRepositoryImpl();
            baseRepository.setSolrOperations(solrTemplate());
            return baseRepository;
        }
}
