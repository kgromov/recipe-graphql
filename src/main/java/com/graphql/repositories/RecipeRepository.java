package com.graphql.repositories;

import com.graphql.domain.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    Optional<Recipe> findByDescription(String description);

    Collection<Recipe> findByCategoryId(String categoryId);
}
