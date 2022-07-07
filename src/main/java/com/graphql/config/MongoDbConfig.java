package com.graphql.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.List;

;

@Configuration
@EnableMongoRepositories
public class MongoDbConfig extends AbstractMongoClientConfiguration {

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost");
    }

    @Override
    protected String getDatabaseName() {
        return "recipe-graphql";
    }

    @Override
    protected Collection<String> getMappingBasePackages() {
        return List.of("com.graphql");
    }
}