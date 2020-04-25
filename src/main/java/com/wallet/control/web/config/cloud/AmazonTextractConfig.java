package com.wallet.control.web.config.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;

@Configuration
public class AmazonTextractConfig {
	
	@Bean
	public AmazonTextract amazonTextract(@Autowired AWSCredentials awsCredentials) {
		String textractEndpoint = "https://textract.us-east-1.amazonaws.com";
		String textractRegion = "us-east-1";		
		AmazonTextract textractClient = AmazonTextractClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.withEndpointConfiguration(new EndpointConfiguration(textractEndpoint, textractRegion))
				.build();
		return textractClient;		
	}

}
