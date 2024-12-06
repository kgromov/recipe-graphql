package com.graphql;

import com.graphql.controllers.RecipeController;
import com.graphql.domain.Recipe;
import com.graphql.model.client.RecipeGraphQLQuery;
import com.graphql.model.client.RecipeProjectionRoot;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;

@GraphQlTest(RecipeController.class)
public class RecipeControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void shouldGetFirstAccount() {
        var queryRequest = new GraphQLQueryRequest(
                RecipeGraphQLQuery.newRequest().build(),
                new RecipeProjectionRoot().id().notes().category()
        );
        var recipeById = this.graphQlTester
                .document(queryRequest.serialize())
                .execute().path("recipe").entity(Recipe.class).get();
    }
}
