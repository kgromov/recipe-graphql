package com.graphql;

import com.graphql.domain.Recipe;
import com.graphql.domain.dtos.RecipeDto;
import com.graphql.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.client.RSocketGraphQlClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@EnableReactiveMongoRepositories
@SpringBootApplication
public class GraphqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlApplication.class, args);
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

    /*@Bean
    ApplicationRunner applicationRunner(HttpGraphQlClient httpGraphQlClient,
                                        RSocketGraphQlClient rSocketGraphQlClient) {
        return args -> {
            String httpRequestDocument = "query {recipes {id, description}}";
            Flux.concat(httpGraphQlClient.document(httpRequestDocument)
                    .retrieve("recipes")
                    .toEntityList(new ParameterizedTypeReference<RecipeDto>() {}))
                    .subscribe(recipe -> log.info("{}", recipe));

            String rsocketRequestDocument = "subscription {recipes {id, description}}";
            rSocketGraphQlClient.document(rsocketRequestDocument)
                    .retrieveSubscription("recipes")
                    .toEntity(RecipeDto.class)
                    .subscribe(recipe -> log.info("{}", recipe));
;            };
    }*/
}
