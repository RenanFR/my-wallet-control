package com.wallet.control.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

@Configuration
public class AWSCredentialsConfig {
	
	@Value("${amazon.accessKey}")
	private String accessKey;
	
	@Value("${amazon.accessSecret}")
	private String accessSecret;	
	
	@Bean
	public AWSCredentials awsCredentials() {
		AWSCredentials credentials = new BasicAWSCredentials(
				accessKey, 
				accessSecret
				);
		return credentials;
	}

}
