package com.graphql.service.controllers;

import com.graphql.service.domain.Category;
import com.graphql.service.domain.Recipe;
import com.graphql.service.domain.dtos.RecipeDto;
import com.graphql.service.repositories.CategoryRepository;
import com.graphql.service.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
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

   /* @BatchMapping
    Map<Recipe, Category> category(Flux<Recipe> recipes) {
        log.info("Reactive batching");
        return StreamSupport.stream(recipes.toIterable().spliterator(), false)
                .collect(Collectors.toMap(identity(), Recipe::getCategory));
    }*/

//    @BatchMapping
//    Flux<Tuple2<Recipe, Category>> category(Flux<Recipe> recipes) {
//        log.info("Reactive batching");
//        Flux<Category> categoryFlux = recipes.flatMap(recipe -> Mono.just(recipe.getCategory());
//        return recipes.zipWith(categoryFlux);
//    }

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
