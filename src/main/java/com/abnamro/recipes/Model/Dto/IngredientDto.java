package com.abnamro.recipes.Model.Dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientDto {
    private String ingredientName;
    private String metric;
    private BigDecimal amount;
}
