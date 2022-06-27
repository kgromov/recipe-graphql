package com.graphql.service.controllers;

import com.graphql.service.domain.Recipe;
import com.graphql.service.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeRepository recipeRepository;

    @QueryMapping("getRecipes")
    public Flux<Recipe> getRecipes() {
        return recipeRepository.findAll();
    }

    @SubscriptionMapping("recipes")
    public Flux<Recipe> recipes() {
        return recipeRepository.findAll().delayElements(Duration.ofMillis(500));
    }
}
