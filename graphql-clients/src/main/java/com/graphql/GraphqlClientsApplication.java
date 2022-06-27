package com.graphql;

import com.graphql.domain.dtos.RecipeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.client.RSocketGraphQlClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
@SpringBootApplication
public class GraphqlClientsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlClientsApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(HttpGraphQlClient http,
                                        RSocketGraphQlClient rsocket) {
        return args -> {
            String httpRequestDocument = "query {getRecipes {id, description}}";
            Flux.concat(http.document(httpRequestDocument)
                    .retrieve("getRecipes")
                    .toEntityList(new ParameterizedTypeReference<RecipeDto>() {
                    }))
                    .subscribe(recipe -> log.info("http client received: {}", recipe));

            String rsocketRequestDocument = "subscription {recipes {id, description}}";
            rsocket.document(rsocketRequestDocument)
                    .retrieveSubscription("recipes")
                    .toEntity(RecipeDto.class)
                    .delayElements(Duration.ofMillis(500))
                    .subscribe(recipe -> log.info("http client received: {}", recipe));
            ;
        };
    }
}
