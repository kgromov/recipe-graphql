package com.graphql.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Category {
    @Id
    private String id;
    private String name;
    private List<String> recipeIds = new ArrayList<>();

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addRecipe(Recipe recipe) {
        recipeIds.add(recipe.getId());
    }
}
