package com.abnamro.recipes.Model;

import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    private String name;
    private String metric;
    private BigDecimal amount;
}
