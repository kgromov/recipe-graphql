package com.graphql.clients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.graphql.client.RSocketGraphQlClient;

// In order to use should be to a separate project or module
//@Configuration
public class RecipeClient {

    @Bean
    public HttpGraphQlClient httpGraphQlClient() {
        return HttpGraphQlClient.builder().url("http://localhost:8080/graphiql").build();
    }

    @Bean
    public RSocketGraphQlClient rSocketGraphQlClient() {
        return RSocketGraphQlClient.builder().tcp("localhost", 9191).route("graphiql").build();
    }
}
