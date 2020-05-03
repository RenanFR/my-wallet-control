package br.com.wallet.control.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class Properties {
	
	@Value("${amazon.s3.bucket}")
	private String bucketName;
	
	@Value("${amazon.sqs.queue.name}")
	private String queueName;
	
	@Value("${cloud.aws.credentials.accessKey}")
	private String accessKey;
	
	@Value("${cloud.aws.credentials.secretKey}")
	private String accessSecret;	

}
