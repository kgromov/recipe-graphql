package com.graphql.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(exclude = {"recipes"})
public class Category {
    @Id
    private String id;
    private String name;
    @OneToMany()
    private List<Recipe> recipes = new ArrayList<>();

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }
}
