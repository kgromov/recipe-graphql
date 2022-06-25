package com.graphql.repositories;

import com.graphql.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
    Mono<Category> getCategoryByDescription(String description);
}
