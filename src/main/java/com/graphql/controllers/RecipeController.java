package com.graphql.controllers;

import com.graphql.domain.Recipe;
import com.graphql.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeRepository recipeRepository;

    @QueryMapping("recipe")
//    @SchemaMapping(typeName = "Query", field = "recipe")
    public Mono<Recipe> recipe(String id) {
        return recipeRepository.findById(id);
    }

    @QueryMapping("recipes")
    public Flux<Recipe> recipes() {
        return recipeRepository.findAll();
    }

    @MutationMapping("addRecipe")
    public Mono<Recipe> addRecipe(@RequestBody String description) {
        return recipeRepository.save(new Recipe(UUID.randomUUID().toString(), description));
    }
}
