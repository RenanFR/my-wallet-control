package br.com.wallet.control.web.config.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonS3Config {
	
	@Bean
	@Primary
	public AmazonS3 amazonS3Client(
			@Autowired 
			AWSCredentials awsCredentials
	) {
		AmazonS3 s3Client = AmazonS3ClientBuilder 
				.standard() 
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)) 
				.withRegion(Regions.US_EAST_1) 
				.build();
		return s3Client;
	}

}
