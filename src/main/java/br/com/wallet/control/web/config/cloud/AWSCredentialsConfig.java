package br.com.wallet.control.web.config.cloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

import br.com.wallet.control.web.config.Properties;

@Configuration
public class AWSCredentialsConfig {
	
	@Autowired
	private Properties properties;
	
	@Bean
	public AWSCredentials awsCredentials() {
		AWSCredentials credentials = new BasicAWSCredentials(
				properties.getAccessKey(), 
				properties.getAccessSecret()
				);
		return credentials;
	}

}
