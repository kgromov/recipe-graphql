package com.graphql.controllers;

import com.graphql.domain.Category;
import com.graphql.domain.Recipe;
import com.graphql.domain.dtos.RecipeDto;
import com.graphql.repositories.CategoryRepository;
import com.graphql.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Slf4j
@Controller
public class RecipeController {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;

    public RecipeController(RecipeRepository recipeRepository,
                            CategoryRepository categoryRepository,
                            BatchLoaderRegistry registry) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        registry.forTypePair(String.class, Category.class).registerMappedBatchLoader((categoryIds, env) -> {
            // return Map<String, Category>
            return Mono.just(categoryRepository.findAllById(categoryIds).stream()
                    .collect(Collectors.toMap(Category::getId, identity()))
            );
        });
    }

    @QueryMapping("recipe")
//    @SchemaMapping(typeName = "Query", field = "recipe")
    public Recipe recipe(@Argument String id) {
        return recipeRepository.findById(id).orElse(null);
    }

    @QueryMapping("recipes")
    public Collection<Recipe> recipes() {
        return recipeRepository.findAll();
    }

    @QueryMapping("categories")
    public Collection<Category> categories() {
        return categoryRepository.findAll();
    }

    @QueryMapping("category")
    public Category category(@Argument String id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @QueryMapping("categoryByName")
    public Category categoryByName(@Argument String name) {
        return categoryRepository.findByName(name);
    }

    /*
     * Possible values:
     * 1) Map<K, V> - A map with parent objects as keys, and batch loaded objects as values.
     * 2) Mono<Map<K, V>> - the same in reactive style
     * 3) Flux<V> - the same sequence as sources passed to the method as argument
     * 4) Callable<Map<K,V>>, Callable<Collection<V>> - Imperative variants, e.g. without remote calls to make. ?
     * 5) Callable<Map<K,V>>, Callable<Collection<V>> - For this to work, AnnotatedControllerConfigurer must be configured with an Executor
     */
    @BatchMapping(typeName = "Category")
    public Map<Category, Iterable<Recipe>> recipes(Collection<Category> categories) {
        log.info("Batching recipes for categories {}", categories);
        return categories.stream()
                .collect(Collectors.toMap(identity(),
                        Category::getRecipes)
                );
    }

    @MutationMapping("addRecipe")
    public Recipe addRecipe(@Argument String description) {
        return recipeRepository.save(new Recipe(UUID.randomUUID().toString(), description));
    }

    @MutationMapping("addCategory")
    public Category addCategory(@Argument String name) {
        return categoryRepository.save(new Category(UUID.randomUUID().toString(), name));
    }

    @MutationMapping("addRecipeWithPayload")
    public RecipeDto addRecipeWithPayload(@Argument RecipeDto recipeDto) {
        Recipe recipe = new Recipe(UUID.randomUUID().toString(), recipeDto.getDescription());
        recipe.setDifficulty(recipeDto.getDifficulty());
        recipe.setPrepTime(recipeDto.getCookTime());
        Recipe savedRecipe = recipeRepository.save(recipe);
        return new RecipeDto(savedRecipe);
    }
}
