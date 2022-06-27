package com.graphql.service;

import com.graphql.service.domain.Recipe;
import com.graphql.service.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Slf4j
@EnableReactiveMongoRepositories
@SpringBootApplication
public class GraphqlServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlServiceApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(RecipeRepository recipeRepository) {
        return args -> {
            recipeRepository.deleteAll()
                    .thenMany(
                            Flux.just("Perfect Guacamole", "Pizza")
                                    .map(name -> new Recipe(UUID.randomUUID().toString(), name))
                                    .flatMap(recipeRepository::save)
                    )
                    .thenMany(recipeRepository.findAll())
                    .subscribe(recipe -> log.info("saving {}", recipe));
        };
    }
}