package com.graphql;

import com.graphql.domain.Category;
import com.graphql.domain.Recipe;
import com.graphql.domain.dtos.RecipeDto;
import com.graphql.repositories.CategoryRepository;
import com.graphql.repositories.RecipeRepository;
import graphql.schema.DataFetcher;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.client.RSocketGraphQlClient;
import org.springframework.graphql.data.query.QuerydslDataFetcher;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;

@Slf4j
@EnableReactiveMongoRepositories
@SpringBootApplication
public class GraphqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlApplication.class, args);
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(CategoryRepository categoryRepository,
                                                           RecipeRepository recipeRepository) {
        return builder -> {
            builder.type("Query",
                    wiring -> wiring.dataFetcher("recipes", env -> recipeRepository.findAll())
            );

            builder.type("Category",  wiring -> wiring.dataFetcher(
                    "recipes", env -> {
                        Category category = env.getSource();
                        return recipeRepository.findByCategoryId(category.getId());
                    }
            ));
        };
    }

    @Bean
    ApplicationRunner applicationRunner(CategoryRepository categoryRepository,
                                        RecipeRepository recipeRepository) {
        return args -> {
            Flux<String> slowFood = Flux.just("Borsch", "Pasta");
            Flux<String> fastFood = Flux.just("Pizza", "Sushi", "Hot-Dog", "Hamburger");
            categoryRepository.deleteAll()
                    .thenMany(
                            Flux.just("Fast food", "Slow food")
                                    .map(name -> new Category(UUID.randomUUID().toString(), name))
//                                    .flatMap(categoryRepository::save)
                    ).subscribe(category -> {
                        recipeRepository.deleteAll()
                                .thenMany(
                                        (category.getName().startsWith("Slow") ? slowFood : fastFood)
                                                .map(name -> new Recipe(UUID.randomUUID().toString(), name, category))
                                                /*.map(name -> {
                                                    Recipe recipe = new Recipe(UUID.randomUUID().toString(), name);
                                                    category.addRecipe(recipe);
                                                    return recipe;
                                                })*/
                                                .flatMap(recipeRepository::save)
                                )
                                .thenMany(recipeRepository.findAll())
                                .subscribe(recipe -> log.info("saved {}", recipe));
                        categoryRepository.save(category).subscribe();
                    }
            );
        };
    }
}
