package com.graphql.repositories;

import com.graphql.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, String> {
    Optional<Recipe> findByDescription(String description);

    Collection<Recipe> findByCategoryId(String categoryId);
}
