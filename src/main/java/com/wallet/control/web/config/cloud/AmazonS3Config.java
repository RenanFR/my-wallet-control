package com.wallet.control.web.config.cloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonS3Config {
	
	@Value("${amazon.accessKey}")
	private String accessKey;
	
	@Value("${amazon.accessSecret}")
	private String accessSecret;
	
	@Bean
	public AmazonS3 amazonS3Client() {
		AWSCredentials credentials = new BasicAWSCredentials(
				accessKey, 
				accessSecret
		);
		AmazonS3 s3Client = AmazonS3ClientBuilder .standard() .withCredentials(new AWSStaticCredentialsProvider(credentials)) .withRegion(Regions.SA_EAST_1) .build();
		return s3Client;
	}

}
