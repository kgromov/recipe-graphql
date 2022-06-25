package com.graphql.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class UnitOfMeasure {
    @Id
    private String id;
    private String description;
}
