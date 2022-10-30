package com.abnamro.recipes.Model;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class Ingredient {
    private String name;
    private String metric;
    private BigDecimal amount;
}
