package com.graphql.repositories;

import com.graphql.domain.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Category findCategoryByName(String name);
}
