package com.graphql.server;

import com.graphql.config.MongoDbConfig;
import com.graphql.controllers.RecipeController;
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
}
