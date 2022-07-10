package com.graphql.controllers;

import com.graphql.domain.Category;
import com.graphql.domain.Recipe;
import com.graphql.domain.dtos.RecipeDto;
import com.graphql.repositories.CategoryRepository;
import com.graphql.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.function.Function.identity;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;

/*    @QueryMapping("recipe")
//    @SchemaMapping(typeName = "Query", field = "recipe")
    public Mono<RecipeDto> recipe(@Argument String id) {
        return recipeRepository.findById(id).map(RecipeDto::new);
    }

    @QueryMapping("recipes")
    public Flux<Recipe> recipes() {
        return recipeRepository.findAll();
    }*/

    @QueryMapping("categories")
    public Flux<Category> categories() {
        return categoryRepository.findAll();
    }

    @QueryMapping("category")
    public Mono<Category> category(@Argument String id) {
        return categoryRepository.findById(id);
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
    public Mono<Recipe> addRecipe(@Argument String description) {
        return recipeRepository.save(new Recipe(UUID.randomUUID().toString(), description));
    }

    @MutationMapping("addCategory")
    public Mono<Category> addCategory(@Argument String name) {
        return categoryRepository.save(new Category(UUID.randomUUID().toString(), name));
    }

    @MutationMapping("addRecipeWithPayload")
    public Mono<RecipeDto> addRecipeWithPayload(@Argument RecipeDto recipeDto) {
        Recipe recipe = new Recipe(UUID.randomUUID().toString(), recipeDto.getDescription());
        recipe.setDifficulty(recipeDto.getDifficulty());
        recipe.setPrepTime(recipeDto.getCookTime());
       /* if (recipeDto.getCategoryId() != null) {
            Mono<Category> category = categoryRepository.findById(recipeDto.getCategoryId());
            category.subscribe(recipe::setCategory);
        }*/
        return recipeRepository.save(recipe).map(RecipeDto::new);
    }
}
