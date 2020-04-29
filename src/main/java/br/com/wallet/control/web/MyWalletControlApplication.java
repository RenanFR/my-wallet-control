package br.com.wallet.control.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "br.com.wallet.control.web.repository")
public class MyWalletControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyWalletControlApplication.class, args);
	}

}
