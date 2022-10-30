package com.abnamro.recipes.service;

import com.abnamro.recipes.Model.Dto.RecipeRequestDto;
import com.abnamro.recipes.Model.Dto.RecipeResponseDto;
import com.abnamro.recipes.Model.MealType;

import java.math.BigDecimal;
import java.util.List;

public interface RecipeService {
    RecipeResponseDto createRecipe(RecipeRequestDto recipeRequest);
    RecipeResponseDto updateRecipe(RecipeRequestDto recipeRequest);
    void deleteRecipe(String name);
    List<RecipeResponseDto> fetchRecipes(String instructions, MealType mealType, BigDecimal servingNumber,List<String> includingIngredientName, List<String> exludingIngredientName);

}
