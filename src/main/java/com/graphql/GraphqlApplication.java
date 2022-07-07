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
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
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
import java.util.stream.Collectors;

@Slf4j
@EnableMongoRepositories
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
                    wiring -> wiring
                            .dataFetcher("categories", env -> categoryRepository.findAll())
                            .dataFetcher("category", env -> {
                                String categoryId = env.getArgument("id");
                                return categoryRepository.findById(categoryId);
                            })
            );

            builder.type("Category", wiring -> wiring.dataFetcher(
                    "recipes", env -> {
                        Category category = env.getSource();
                        return recipeRepository.findByCategoryId(category.getId());
                    }
            ));
            // In order to return category inside recipe
            builder.type("Recipe", wiring -> wiring.dataFetcher(
                    "category", env -> {
                        Recipe recipe = env.getSource();
                        return categoryRepository.findById(recipe.getCategoryId());
                    }
            ));
        };
    }

    @Bean
    ApplicationRunner applicationRunner(CategoryRepository categoryRepository,
                                        RecipeRepository recipeRepository) {
        return args -> {
            Map<String, List<String>> recipesPerCategory = Map.of(
                    "Fast food", List.of("Pizza", "Sushi", "Hot-Dog", "Hamburger"),
                    "Slow food", List.of("Borsch", "Pasta")
            );
            categoryRepository.deleteAll();
            recipeRepository.deleteAll();
            recipesPerCategory.forEach((categoryName, recipeNames) -> {
                Category category = new Category(UUID.randomUUID().toString(), categoryName);
                List<Recipe> recipes = recipeNames.stream()
                        .map(name -> new Recipe(UUID.randomUUID().toString(), name, category))
                        .collect(Collectors.toList());
                recipeRepository.saveAll(recipes);
                categoryRepository.save(category);
            });
        };
    }
}
