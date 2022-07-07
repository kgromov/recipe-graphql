package com.graphql.controllers;

import com.graphql.domain.Category;
import com.graphql.domain.Recipe;
import com.graphql.repositories.CategoryRepository;
import com.graphql.repositories.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;

    @QueryMapping("categories")
    public List<Category> categories() {
        return categoryRepository.findAll();
    }

    @QueryMapping("category")
    public Category category(@Argument String id) {
        return categoryRepository.findById(id).orElse(null);
    }

  /*  @SchemaMapping(typeName = "Category")
    public Iterable<Recipe> recipes(Category category) {
        log.info("Fetching recipes for category {}", category);
        return recipeRepository.findAllById(category.getRecipeIds());
    }*/

    @BatchMapping(typeName = "Category")
    public Map<Category, Iterable<Recipe>> recipes(Collection<Category> categories) {
        log.info("Fetching recipes for categories {}", categories);
       return categories.stream()
                .collect(Collectors.toMap(category -> category,
                        category -> recipeRepository.findAllById(category.getRecipeIds())));
    }


}
