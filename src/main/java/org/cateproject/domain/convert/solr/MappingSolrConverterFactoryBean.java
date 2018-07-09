package org.cateproject.domain.convert.solr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.solr.core.convert.CustomConversions;
import org.springframework.data.solr.core.convert.MappingSolrConverter;
import org.springframework.data.solr.core.mapping.SimpleSolrMappingContext;

public class MappingSolrConverterFactoryBean implements FactoryBean<MappingSolrConverter>, ApplicationContextAware, InitializingBean {
        
        private MappingSolrConverter mappingSolrConverter = null;
	
	private Collection<Converter<? extends Object, SolrInputDocument>> writingConverters = new HashSet<Converter<? extends Object, SolrInputDocument>>();
	
	private Collection<Converter<SolrDocument, ? extends Object>> readingConverters = new HashSet<Converter<SolrDocument, ? extends Object>>();

	private ApplicationContext applicationContext;

        public void setWritingConverters(Collection<Converter<? extends Object, SolrInputDocument>> writingConverters) {
            this.writingConverters = writingConverters;
        }

        public void setReadingConverters(Collection<Converter<SolrDocument,? extends Object>> readingConverters) {
            this.readingConverters = readingConverters;
        }

        public void afterPropertiesSet() {
        	List<Converter> customConverters = new ArrayList<Converter>();
		customConverters.addAll(readingConverters);
		customConverters.addAll(writingConverters);
		CustomConversions customConversions = new CustomConversions(customConverters);
		mappingSolrConverter = new MappingSolrConverter(new SimpleSolrMappingContext());
		mappingSolrConverter.setCustomConversions(customConversions);
		mappingSolrConverter.setApplicationContext(applicationContext);
		mappingSolrConverter.afterPropertiesSet();
        }

	@Override
	public MappingSolrConverter getObject() throws Exception {
		return mappingSolrConverter;
	}

	@Override
	public Class<?> getObjectType() {
		return MappingSolrConverter.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
