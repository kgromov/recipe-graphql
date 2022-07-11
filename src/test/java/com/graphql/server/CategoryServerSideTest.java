package com.graphql.server;

import com.graphql.config.MongoDbConfig;
import com.graphql.controllers.RecipeController;
import com.graphql.domain.Category;
import com.graphql.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.execution.DefaultExecutionGraphQlService;
import org.springframework.graphql.server.WebGraphQlHandler;
import org.springframework.graphql.test.tester.ExecutionGraphQlServiceTester;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.function.Predicate;

@SpringBootTest
@AutoConfigureGraphQlTester
// equivalent to:
/*@GraphQlTest(RecipeController.class)
@Import(MongoDbConfig.class)*/
// Seems the 1st option  is preferable - https://github.com/spring-projects/spring-graphql/issues/75
public class CategoryServerSideTest {

    @Autowired
    private DefaultExecutionGraphQlService executionGraphQlService;
    private GraphQlTester graphQlTester;

    @BeforeEach
    void setUp() {
        graphQlTester = ExecutionGraphQlServiceTester.create(executionGraphQlService);
    }

    @Test
    void categories() {
        String document = "{categories {name} }";

        graphQlTester.document(document)
                .execute()
                .path("categories[*].name")
                .entityList(String.class)
                .hasSizeGreaterThan(1);
    }

    @Test
    void categoryByName() {
        graphQlTester.documentName("categoriesByName")
                .variable("name", "Fast food")
                .execute()
                .errors()
                .verify()
                .path("categoryByName.id")
                .entity(String.class)
                .matches((Predicate<String>) s -> s.length() >= 32);
    }

    @Test
    void recipesByCategory() {
//        String document = "{categories {recipes {id, description} } }";
        String document = "{categoryByName(name: \"Fast Food\") {recipes {id, description} } }";

        graphQlTester.document(document)
                .execute()
//                .path("categories[*].recipes")
                .path("categoryByName.recipes")
                .entityList(Recipe.class)
                .hasSizeGreaterThan(0);
    }

    @Test
    void addCategory(){
        String document ="{query: mutation{ addCategory(" +
                "id:  \"1e0ae311-7a62-45cd-93f2-f5e8754bd90b\", name: \"Ukrainian food\") {id, name} } }";

//        String document ="{ addCategory(" +
//                "id:  \"1e0ae311-7a62-45cd-93f2-f5e8754bd90b\", name: \"Ukrainian food\") {id, name} } ";

        Category addCategory = graphQlTester.document(document)
//                .operationName("mutation")
                .execute()
                .errors()
                .verify()
                .path("addCategory")
                .entity(Category.class)
                .get();
    }
}
