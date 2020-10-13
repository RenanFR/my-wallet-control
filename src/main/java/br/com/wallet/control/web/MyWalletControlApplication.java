package br.com.wallet.control.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableResourceServer
@EnableMongoRepositories(basePackages = "br.com.wallet.control.web.repository.mongo")
@EnableNeo4jRepositories(basePackages = "br.com.wallet.control.web.repository.neo4j")
public class MyWalletControlApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MyWalletControlApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}	

}
