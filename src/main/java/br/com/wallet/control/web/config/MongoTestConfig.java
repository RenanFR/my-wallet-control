package br.com.wallet.control.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class MongoTestConfig {
//
//    private static final String MONGO_DB_URL = "localhost";
//    
//    private static final String MONGO_DB_NAME = "wallet-control-embed-db";
//    @Bean
//    public MongoTemplate mongoTemplate() throws IOException {
//        EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
//        mongo.setBindIp(MONGO_DB_URL);
//        MongoClient mongoClient = mongo.getObject();
//        @SuppressWarnings("deprecation")
//		MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, MONGO_DB_NAME);
//        return mongoTemplate;
//    }
}
