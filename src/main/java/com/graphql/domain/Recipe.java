package com.graphql.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
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
    private Byte[] image;
    private Difficulty difficulty;
    private String categoryId;

    public Recipe(String description) {
        this.description = description;
    }

    public Recipe(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public Recipe(String id, String description, Category category) {
        this.id = id;
        this.description = description;
        this.categoryId = category.getId();
        category.getRecipes().add(this);
    }
}
