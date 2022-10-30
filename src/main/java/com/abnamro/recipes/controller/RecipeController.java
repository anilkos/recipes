package com.abnamro.recipes.controller;


import com.abnamro.recipes.Model.Dto.RecipeRequestDto;
import com.abnamro.recipes.Model.Dto.RecipeResponseDto;
import com.abnamro.recipes.Model.MealType;
import com.abnamro.recipes.service.RecipeService;
import com.abnamro.recipes.util.ApiConstants;
import com.abnamro.recipes.util.ApiMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @PostMapping(ApiConstants.API_VERSION + ApiMapping.ADD_RECIPE)
    public ResponseEntity<RecipeResponseDto> addRecipe(@RequestBody RecipeRequestDto addRecipeRequest) {

        return new ResponseEntity<>(recipeService.createRecipe(addRecipeRequest),HttpStatus.CREATED);
    }

    @PutMapping(ApiConstants.API_VERSION + ApiMapping.UPDATE_RECIPE)
    public ResponseEntity<RecipeResponseDto> updateRecipe(@RequestBody RecipeRequestDto updateRecipeRequest) {

        return new ResponseEntity<>(recipeService.updateRecipe(updateRecipeRequest),HttpStatus.ACCEPTED);
    }

    @DeleteMapping(ApiConstants.API_VERSION + ApiMapping.REMOVE_RECIPE)
    public ResponseEntity removeRecipe(@PathVariable String recipeName) {
        recipeService.deleteRecipe(recipeName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(ApiConstants.API_VERSION + ApiMapping.SEARCH_RECIPE)
    public ResponseEntity<List<RecipeResponseDto>> fetchRecipe(
    @RequestParam(name = "instructions",required = false) String instructions,
    @RequestParam(name = "mealType",required = false) MealType mealType,
    @RequestParam(name = "servingNumber",required = false) BigDecimal servingNumber,
    @RequestParam(name = "includingIngredientsName",required = false) List<String> includingIngredientName,
    @RequestParam(name = "exludingIngredientsName",required = false) List<String> exludingIngredientName
    ) {

        return new ResponseEntity<>(recipeService.fetchRecipes(instructions,mealType,servingNumber,includingIngredientName,exludingIngredientName),HttpStatus.OK);
    }
}
