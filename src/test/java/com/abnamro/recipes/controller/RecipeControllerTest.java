package com.abnamro.recipes.controller;


import com.abnamro.recipes.Model.Dto.IngredientDto;
import com.abnamro.recipes.Model.Dto.RecipeRequestDto;
import com.abnamro.recipes.Model.Dto.RecipeResponseDto;
import com.abnamro.recipes.Model.MealType;
import com.abnamro.recipes.exception.RecipeAlreadyExistException;
import com.abnamro.recipes.exception.RecipeNotFoundException;
import com.abnamro.recipes.exception.SearchParamException;
import com.abnamro.recipes.service.RecipeService;
import com.abnamro.recipes.util.ApiConstants;
import com.abnamro.recipes.util.ApiMapping;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({RecipeController.class})
@ExtendWith(SpringExtension.class)
public class RecipeControllerTest {

    @MockBean
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private RecipeResponseDto recipeResponseDto;
    private RecipeRequestDto recipeRequestDto;
    private IngredientDto ingredientDto;

    @BeforeEach
    void init() {
        List<IngredientDto> ingredientDtoList = List.of( new IngredientDto("Chickpeas", "gram" ,BigDecimal.valueOf(100)),
        ingredientDto = new IngredientDto("Tahini", "gram" ,BigDecimal.valueOf(50)),
        ingredientDto = new IngredientDto("OliveOil", "liter" ,BigDecimal.valueOf(1)),
        ingredientDto = new IngredientDto("Lemon", "piece" ,BigDecimal.valueOf(1)));
        recipeRequestDto = new RecipeRequestDto("Humus",ingredientDtoList ,MealType.VEGAN,"Use tahini and chickpeas squize lemon juice",BigDecimal.TEN);
        recipeResponseDto = new RecipeResponseDto("Humus",ingredientDtoList ,MealType.VEGAN,"Use tahini and chickpeas squize lemon juice",BigDecimal.TEN);

    }

   @Test
   @WithMockUser("user")
    void addRecipe() throws Exception {
        when(recipeService.createRecipe(any(RecipeRequestDto.class))).thenReturn(recipeResponseDto);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/"+ApiConstants.API_VERSION + ApiMapping.ADD_RECIPE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.mapper.writeValueAsString(recipeRequestDto)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeName").exists())
                .andExpect(jsonPath("$.mealType").exists())
                .andExpect(jsonPath("$.numberOfPeople").exists())
                .andExpect(status().isCreated());
    }
    @Test
    @WithMockUser("user")
    void updateRecipe() throws Exception {
        when(recipeService.updateRecipe(any(RecipeRequestDto.class))).thenReturn(recipeResponseDto);
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/"+ApiConstants.API_VERSION + ApiMapping.UPDATE_RECIPE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.mapper.writeValueAsString(recipeRequestDto)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.recipeName").exists())
                .andExpect(jsonPath("$.mealType").exists())
                .andExpect(jsonPath("$.numberOfPeople").exists())
                .andExpect(status().isAccepted());
    }
    @Test
    @WithMockUser("user")
    void removeRecipe() throws Exception {
        doNothing().when(recipeService).deleteRecipe(any());
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/"+ApiConstants.API_VERSION + ApiMapping.REMOVE_RECIPE,"humus")
                                )
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser("user")
    void fetchRecipe() throws Exception {
        when(recipeService.fetchRecipes(any(),any(),any(),any(),any())).thenReturn(List.of(recipeResponseDto));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/"+ApiConstants.API_VERSION + ApiMapping.SEARCH_RECIPE)
                        .param("instructions","something")
                        .param("mealType", String.valueOf(MealType.VEGAN))
                        .param("servingNumber", String.valueOf(1))
                        .param("instructions","something")
                        .param("includingIngredientsName", String.valueOf(List.of("Tahini")))
                        .param("exludingIngredientsName", String.valueOf(List.of("Bean")))
                        .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].recipeName").value(recipeResponseDto.getRecipeName()))
                .andExpect(jsonPath("$.[0].mealType").value(recipeResponseDto.getMealType().toString()));
    }

    @Test
    @WithMockUser("user")
    void addAlreadyExistedRecipe_Exception() throws Exception {
        when(recipeService.createRecipe(any(RecipeRequestDto.class))).thenThrow(RecipeAlreadyExistException.class);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/"+ApiConstants.API_VERSION + ApiMapping.ADD_RECIPE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.mapper.writeValueAsString(recipeRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorDate").exists())
                .andExpect(jsonPath("$.details").exists());
    }

    @Test
    @WithMockUser("user")
    void recipeNotFound_Exception() throws Exception {
        when(recipeService.updateRecipe(any(RecipeRequestDto.class))).thenThrow(RecipeNotFoundException.class);
        mockMvc.perform(
                        MockMvcRequestBuilders.put("/"+ApiConstants.API_VERSION + ApiMapping.UPDATE_RECIPE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.mapper.writeValueAsString(recipeRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorDate").exists())
                .andExpect(jsonPath("$.details").exists());
    }

    @Test
    @WithMockUser("user")
    void SearchParam_Exception() throws Exception {
        when(recipeService.fetchRecipes(any(),any(),any(),any(),any())).thenThrow(SearchParamException.class);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/"+ApiConstants.API_VERSION + ApiMapping.SEARCH_RECIPE)
                                .param("instructions","something")
                                .param("mealType", String.valueOf(MealType.VEGAN))
                                .param("servingNumber", String.valueOf(1))
                                .param("instructions","something")
                                .param("includingIngredientsName", String.valueOf(List.of("Tahini")))
                                .param("exludingIngredientsName", String.valueOf(List.of("Tahini")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorDate").exists())
                .andExpect(jsonPath("$.details").exists());
    }
    @Test
    @WithMockUser("user")
    void general_Exception() throws Exception {
        when(recipeService.fetchRecipes(any(),any(),any(),any(),any())).thenThrow(NullPointerException.class);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/"+ApiConstants.API_VERSION + ApiMapping.SEARCH_RECIPE)
                                .param("instructions","something")
                                .param("mealType", String.valueOf(MealType.VEGAN))
                                .param("servingNumber", String.valueOf(1))
                                .param("instructions","something")
                                .param("includingIngredientsName", String.valueOf(List.of("Tahini")))
                                .param("exludingIngredientsName", String.valueOf(List.of("Tahini")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorDate").exists())
                .andExpect(jsonPath("$.details").exists());
    }
}
