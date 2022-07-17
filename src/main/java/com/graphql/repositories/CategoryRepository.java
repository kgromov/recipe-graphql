package com.graphql.repositories;

import com.graphql.domain.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
    Mono<Category> findByName(String name);

    @Override
    Mono<Category> findById(String id);

    @Override
    Flux<Category> findAllById(Iterable<String> ids);
}
