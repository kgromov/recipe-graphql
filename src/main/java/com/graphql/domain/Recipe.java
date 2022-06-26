package com.graphql.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Recipe {
    @Id
    private String id;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String notes;
    private Set<Ingredient> ingredients = new HashSet<>();
    private Byte[] image;
    private Difficulty difficulty;
//    private Category category;

    public Recipe(String description) {
        this.description = description;
    }

    public Recipe(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public Recipe addIngredient(Ingredient ingredient){
        this.ingredients.add(ingredient);
        return this;
    }
}
