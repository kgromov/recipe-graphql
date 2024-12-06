package com.graphql;

import com.graphql.domain.Category;
import com.graphql.domain.Recipe;
import com.graphql.repositories.CategoryRepository;
import com.graphql.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@EnableJpaRepositories
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

            builder.type("Query",
                    wiring -> wiring
                            .dataFetcher("recipes", env -> recipeRepository.findAll())
                            .dataFetcher("recipe", env ->
                            {
                                String recipeId = env.getArgument("id");
                                return recipeRepository.findById(recipeId);
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
