package com.abnamro.recipes.Model.Dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngredientDto {
    private String ingredientName;
    private String metric;
    private BigDecimal amount;
}
