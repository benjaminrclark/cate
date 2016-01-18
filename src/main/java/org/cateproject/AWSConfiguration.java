package org.cateproject;

import java.util.List;

import org.cateproject.multitenant.event.SnsRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cloud.aws.context.config.annotation.EnableContextCredentials;
import org.springframework.cloud.aws.context.config.annotation.EnableContextInstanceData;
import org.springframework.cloud.aws.context.config.annotation.EnableContextRegion;
import org.springframework.cloud.aws.context.config.annotation.EnableStackConfiguration;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSns;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.elasticache.AmazonElastiCache;
import com.amazonaws.services.elasticache.AmazonElastiCacheClient;
import com.amazonaws.services.elasticache.model.CacheCluster;
import com.amazonaws.services.elasticache.model.DescribeCacheClustersResult;
import com.amazonaws.services.elasticache.model.Endpoint;
import com.amazonaws.services.route53.AmazonRoute53;
import com.amazonaws.services.route53.AmazonRoute53Client;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;

@Profile("aws")
@Configuration
@EnableContextCredentials(instanceProfile=true)
@EnableContextRegion(autoDetect=true)
@EnableContextInstanceData
@EnableStackConfiguration
@EnableSqs
@EnableSns
public class AWSConfiguration {

        private static Logger logger = LoggerFactory.getLogger(AWSConfiguration.class);

        @Autowired
	private AmazonSNS amazonSNS;

        @Autowired
        private AmazonSQS amazonSQS;

        @Autowired
        private AWSCredentialsProvider awsCredentialsProvider;

        @Value("${cloudformation.topicArn}")
        private String topicArn;

        @Autowired
        private ObjectMapper objectMapper;

        @Bean
        public SnsRegistrar snsRegistrar() {
            return new SnsRegistrar(amazonSNS, amazonSQS, topicArn);
        }

        @Bean
        public MappingJackson2MessageConverter messageConverter() {
            MappingJackson2MessageConverter messageConverter =  new MappingJackson2MessageConverter();
            messageConverter.setObjectMapper(objectMapper);
            return messageConverter;
        }

        @Bean
        public AmazonRoute53 amazonRoute53() {
            AmazonRoute53Client amazonRoute53 = new AmazonRoute53Client(awsCredentialsProvider);
            return amazonRoute53;
        }

        @Bean AmazonElastiCache amazonElastiCache() {
            AmazonElastiCacheClient amazonElastiCache = new AmazonElastiCacheClient(awsCredentialsProvider);
            return amazonElastiCache; 
        }

        @Bean
        public RedisProperties redisProperties() {
            AmazonElastiCache amazonElastiCache = amazonElastiCache();
            DescribeCacheClustersResult describeCacheClustersResult = amazonElastiCache.describeCacheClusters();
            List<CacheCluster> cacheClusters = describeCacheClustersResult.getCacheClusters();
            if(cacheClusters.size() == 0) {
                throw new RuntimeException("Could not obtain information about cache cluster");
            } else {
                Endpoint endpoint = cacheClusters.get(0).getConfigurationEndpoint();
                logger.info("Configuring redisProperties: Address {}, Port {}", new Object[]{endpoint.getAddress(), endpoint.getPort()});
                RedisProperties redisProperties = new RedisProperties();
                redisProperties.setHost(endpoint.getAddress());
                redisProperties.setPort(endpoint.getPort());
                return redisProperties;
            }
        }
}
