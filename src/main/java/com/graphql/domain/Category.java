package com.graphql.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
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
