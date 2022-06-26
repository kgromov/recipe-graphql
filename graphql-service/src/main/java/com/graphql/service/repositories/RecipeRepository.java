package com.graphql.service.repositories;

import com.graphql.service.domain.Recipe;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface RecipeRepository extends ReactiveMongoRepository<Recipe, String> {
    Mono<Recipe> findByDescription(String description);
}
