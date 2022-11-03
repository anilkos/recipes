package com.abnamro.recipes.Model.Dto;

import com.abnamro.recipes.Model.MealType;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequestDto {
    private String recipeName;
    private List<IngredientDto> ingredientList;
    private MealType mealType;
    private String instructions;
    private BigDecimal numberOfPeople;
}
