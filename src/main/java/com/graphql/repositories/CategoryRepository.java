package com.graphql.repositories;

import com.graphql.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Category findCategoryByName(String name);

    Category findByName(String name);
}
