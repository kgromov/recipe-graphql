package com.graphql.controllers;

import com.graphql.domain.Category;
import com.graphql.domain.Recipe;
import com.graphql.domain.dtos.RecipeDto;
import com.graphql.repositories.CategoryRepository;
import com.graphql.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.function.Function.identity;
import static org.springframework.data.util.Pair.toMap;

@Controller
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;

    @QueryMapping("recipe")
//    @SchemaMapping(typeName = "Query", field = "recipe")
    public Mono<RecipeDto> recipe(@Argument String id) {
        return recipeRepository.findById(id).map(RecipeDto::new);
    }

    @QueryMapping("recipes")
    public Flux<Recipe> recipes() {
        return recipeRepository.findAll();
    }

    @SchemaMapping(typeName = "Category")
    public Mono<Category> category(@Argument String id) {
        return categoryRepository.findById(id);
    }

    @BatchMapping
    Map<Recipe, Category> category(Flux<Recipe> recipes) {
//        recipes.zipWith(recipe -> recipe, recipe -> Mono.just(recipe.getCategory())))
        StreamSupport.stream(recipes.toIterable().spliterator(), false)
                .collect(Collectors.toMap(identity(), Recipe::getCategory));

    }

    @MutationMapping("addRecipe")
    public Mono<Recipe> addRecipe(@Argument String description) {
        return recipeRepository.save(new Recipe(UUID.randomUUID().toString(), description));
    }

    @MutationMapping("addRecipeWithPayload")
    public Mono<RecipeDto> addRecipeWithPayload(@Argument RecipeDto recipeDto) {
        Recipe recipe = new Recipe(UUID.randomUUID().toString(), recipeDto.getDescription());
        recipe.setDifficulty(recipeDto.getDifficulty());
        recipe.setPrepTime(recipeDto.getCookTime());
        return recipeRepository.save(recipe).map(RecipeDto::new);
    }
}
