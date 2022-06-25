package com.graphql.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Category {
    @Id
    private String id;
    private String description;
}
