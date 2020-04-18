package com.wallet.control.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
  
    @Override
    protected String getDatabaseName() {
        return "wallet-control";
    }
  
    @Override
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://127.0.0.1:27017");
    }
  
    @Override
    protected String getMappingBasePackage() {
        return "com.wallet.control.web.model";
    }
}
