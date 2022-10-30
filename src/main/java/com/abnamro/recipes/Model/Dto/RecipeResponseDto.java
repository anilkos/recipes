package com.abnamro.recipes.Model.Dto;

import com.abnamro.recipes.Model.MealType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RecipeResponseDto {
    private String recipeName;
    private List<IngredientDto> ingredientList;
    private MealType mealType;
    private String instructions;
    private BigDecimal numberOfPeople;
}
