package com.abnamro.recipes.service;

import com.abnamro.recipes.Model.Dto.IngredientDto;
import com.abnamro.recipes.Model.Dto.RecipeRequestDto;
import com.abnamro.recipes.Model.Dto.RecipeResponseDto;
import com.abnamro.recipes.Model.Ingredient;
import com.abnamro.recipes.Model.MealType;
import com.abnamro.recipes.entity.Recipe;
import com.abnamro.recipes.exception.RecipeAlreadyExistException;
import com.abnamro.recipes.exception.RecipeNotFoundException;
import com.abnamro.recipes.repository.RecipeRepository;
import com.abnamro.recipes.service.impl.RecipeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;
    @InjectMocks
    private RecipeServiceImpl recipeService;
    private Recipe recipe;
    @Mock
    private ModelMapper modelMapper;

    private List<IngredientDto> ingredientDtoList;

    private RecipeRequestDto recipeRequestDto;
    private RecipeResponseDto recipeResponseDto;

    @BeforeEach
    void init(){
        List<Ingredient> ingredientList = List.of( new Ingredient("Chickpeas", "gram" , BigDecimal.valueOf(100)),
               new Ingredient("Tahini", "gram" ,BigDecimal.valueOf(50)),
               new Ingredient("OliveOil", "liter" ,BigDecimal.valueOf(1)),
               new Ingredient("Lemon", "piece" ,BigDecimal.valueOf(1)));
        recipe = new Recipe(UUID.randomUUID().toString(),"Humus", MealType.VEGAN,ingredientList,"Use tahini and chickpeas squize lemon juice",BigDecimal.TEN);
        ingredientDtoList = List.of(new IngredientDto("Chickpeas", "gram", BigDecimal.valueOf(100)),
                new IngredientDto("Tahini", "gram", BigDecimal.valueOf(50)),
                new IngredientDto("OliveOil", "liter", BigDecimal.valueOf(1)),
                new IngredientDto("Lemon", "piece", BigDecimal.valueOf(1)));
         recipeRequestDto = new RecipeRequestDto("Humus", ingredientDtoList, MealType.VEGAN, "Use tahini and chickpeas squize lemon juice", BigDecimal.TEN);
         recipeResponseDto = new RecipeResponseDto("Humus", ingredientDtoList, MealType.VEGAN, "Use tahini and chickpeas squize lemon juice", BigDecimal.TEN);
        lenient().when(modelMapper.map(any(Recipe.class),any())).thenReturn(recipeResponseDto);
        lenient().when(modelMapper.map(any(RecipeRequestDto.class),any())).thenReturn(recipe);
        lenient().when(modelMapper.map(any(Ingredient.class),any())).thenReturn(new IngredientDto("Tahini", "gram", BigDecimal.valueOf(50)));
        lenient().when(modelMapper.map(any(IngredientDto.class),any())).thenReturn(new Ingredient("Tahini", "gram" ,BigDecimal.valueOf(50)));
    }
    @Test
    void createRecipe() {
        when(recipeRepository.findByTheRecipesRecipeName(any(String.class))).thenReturn(Optional.empty());
        when(recipeRepository.save(any())).thenReturn(recipe);
        RecipeResponseDto newRecipe = recipeService.createRecipe(recipeRequestDto);
        assertThat(newRecipe.getRecipeName()).isEqualTo("Humus");
        assertThat(newRecipe.getIngredientList()).isNotEmpty();
        assertThat(newRecipe.getInstructions()).isNotBlank();
    }
    @Test
    void tryToCreateExistedRecipe_Exception() {
        when(recipeRepository.findByTheRecipesRecipeName(any(String.class))).thenReturn(Optional.of(recipe));
        Exception exception = assertThrows(RecipeAlreadyExistException.class, () -> {
            recipeService.createRecipe(recipeRequestDto);
        });
        assertThat(exception).isInstanceOf(RecipeAlreadyExistException.class);
    }
    @Test
    void updateRecipe() {
        when(recipeRepository.findByTheRecipesRecipeName(any(String.class))).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any())).thenReturn(recipe);
        RecipeResponseDto newRecipe = recipeService.updateRecipe(recipeRequestDto);
        assertThat(newRecipe.getRecipeName()).isEqualTo("Humus");
        assertThat(newRecipe.getIngredientList()).isNotEmpty();
        assertThat(newRecipe.getInstructions()).isNotBlank();
    }
    @Test
    void tryToUpdateNotExistedRecipe_Exception() {
        when(recipeRepository.findByTheRecipesRecipeName(any(String.class))).thenReturn(Optional.empty());
        Exception exception = assertThrows(RecipeNotFoundException.class, () -> {
            recipeService.updateRecipe(recipeRequestDto);
        });
        assertThat(exception).isInstanceOf(RecipeNotFoundException.class);
    }

}
