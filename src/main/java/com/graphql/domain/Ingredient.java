package com.graphql.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Ingredient {
    @Id
    private Long id;
    private String description;
    private BigDecimal amount;
    private UnitOfMeasure uom;

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom) {
        this.description = description;
        this.amount = amount;
        this.uom = uom;
    }
}
