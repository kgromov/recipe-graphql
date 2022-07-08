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

   /* @SchemaMapping(typeName = "Category")
    public Flux<Recipe> recipes(Category category) {
        log.info("Fetching recipes for category {}", category);
        return categoryRepository.findById(category.getId())
                .flatMapIterable(Category::getRecipes);
    }*/

    @BatchMapping(typeName = "Category")
    public Map<Category, Iterable<Recipe>> recipes(Collection<Category> categories) {
        log.info("Batching recipes for categories {}", categories);
        return categories.stream()
                .collect(Collectors.toMap(identity(),
                        Category::getRecipes)
                );
    }

   /* @BatchMapping(typeName = "Category")
    public Map<Category, Flux<Recipe>> recipes(Flux<Category> categories) {
        log.info("Reactive batching");
        return StreamSupport.stream(categories.toIterable().spliterator(), false)
                .collect(Collectors.toMap(identity(),
                        category -> recipeRepository.findByCategoryId(category.getId()))
                );
    }*/

    /* @BatchMapping(typeName = "Category")
     public Flux<Tuple2<Category, Recipe>> recipes(Flux<Category> categories) {
         log.info("Reactive batching");
         Flux<Recipe> recipeFlux = categories.flatMap(category -> Flux.fromIterable(category.getRecipes()));
         return categories.zipWith(recipeFlux);
     }
 */
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
