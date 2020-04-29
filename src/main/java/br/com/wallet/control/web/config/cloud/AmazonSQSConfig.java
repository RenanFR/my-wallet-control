package br.com.wallet.control.web.config.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

@Configuration
public class AmazonSQSConfig {
	
	@Bean
	@Primary
	public AmazonSQSAsync sqsClient(
			@Autowired
			AWSCredentials awsCredentials) {
		AmazonSQSAsync sqsClient = AmazonSQSAsyncClientBuilder
					.standard()
					.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
					.withRegion(Regions.US_EAST_1)
					.build();
		return sqsClient;
	}
	
	@Bean
	public QueueMessagingTemplate messagingTemplate(
			@Autowired
			AmazonSQSAsync sqsAsyncClient) {
		return new QueueMessagingTemplate(sqsAsyncClient);
	}

}
