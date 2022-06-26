package com.graphql.service.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
