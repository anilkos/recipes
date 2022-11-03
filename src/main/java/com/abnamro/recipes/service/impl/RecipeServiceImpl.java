package com.abnamro.recipes.service.impl;

import com.abnamro.recipes.Model.Dto.RecipeRequestDto;
import com.abnamro.recipes.Model.Dto.RecipeResponseDto;
import com.abnamro.recipes.Model.Ingredient;
import com.abnamro.recipes.Model.MealType;
import com.abnamro.recipes.entity.Recipe;
import com.abnamro.recipes.exception.RecipeAlreadyExistException;
import com.abnamro.recipes.exception.RecipeNotFoundException;
import com.abnamro.recipes.exception.SearchParamException;
import com.abnamro.recipes.repository.RecipeRepository;
import com.abnamro.recipes.service.RecipeService;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    private ModelMapper modelMapper;
    private RecipeRepository recipeRepository;
    private MongoTemplate mongoTemplate;

    public RecipeServiceImpl(ModelMapper modelMapper,RecipeRepository recipeRepository, MongoTemplate mongoTemplate) {
        this.modelMapper = modelMapper;
        this.recipeRepository = recipeRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public RecipeResponseDto createRecipe(RecipeRequestDto recipeRequest) {
        if(recipeRepository.findByTheRecipesRecipeName(recipeRequest.getRecipeName()).isPresent()) {
            throw new RecipeAlreadyExistException("Recipe exists with name"+recipeRequest.getRecipeName());
        }
        return modelMapper.map(recipeRepository.save(getRecipe(recipeRequest)),RecipeResponseDto.class);
    }


    @Override
    public RecipeResponseDto updateRecipe(RecipeRequestDto recipeRequest) {
        Optional<Recipe> recipe = recipeRepository.findByTheRecipesRecipeName(recipeRequest.getRecipeName());
        if(!recipe.isPresent()){
            throw new RecipeNotFoundException("Recipe not found with name:"+recipeRequest.getRecipeName());
        }
        Recipe updatedRecipe = getRecipe(recipeRequest);
        updatedRecipe.setId(recipe.get().getId());
        recipeRepository.save(updatedRecipe);
        return modelMapper.map(recipeRepository.save(updatedRecipe),RecipeResponseDto.class);
    }

    @Override
    public void deleteRecipe(String name) {
        Optional<Recipe> recipe = recipeRepository.findByTheRecipesRecipeName(name);
        if(!recipe.isPresent()){
            throw new RecipeNotFoundException("Recipe not found with name:"+name);
        }
        recipeRepository.delete(recipe.get());
    }

    @Override
    public List<RecipeResponseDto> fetchRecipes(String instructions, MealType mealType, BigDecimal servingNumber, List<String> includingIngredientName, List<String> exludingIngredientName) {
        Query query = new Query();
        generateQuery(mealType, servingNumber, includingIngredientName, exludingIngredientName, query, instructions);
        List<Recipe> recipes = mongoTemplate.find(query, Recipe.class);
        return recipes.stream().map(i->modelMapper.map(i,RecipeResponseDto.class)).collect(Collectors.toList());
    }
    private Recipe getRecipe(RecipeRequestDto recipeRequest) {
        Recipe newRecipe = modelMapper.map(recipeRequest,Recipe.class);
        List<Ingredient> ingredientList = recipeRequest.getIngredientList().stream().map(
                i->modelMapper.map(i,Ingredient.class)
        ).collect(Collectors.toList());
        newRecipe.setIngredientList(ingredientList);
        return newRecipe;
    }
    private void generateQuery(MealType mealType, BigDecimal servingNumber, List<String> includingIngredientName, List<String> exludingIngredientName, Query query, String instructions) {
        if(exludingIngredientName != null && exludingIngredientName.size()>0 &&
                includingIngredientName != null && includingIngredientName.size()>0) {
            verifyExcludingIncluding(includingIngredientName,exludingIngredientName);
            Criteria criteria = new Criteria();
            criteria.andOperator(Criteria.
                            where("ingredientList.name").in(includingIngredientName.toArray(String[]::new)),
                    Criteria.where("ingredientList.name").not().in(exludingIngredientName.toArray(String[]::new)));
            query.addCriteria(criteria);
        }
        if(exludingIngredientName != null && exludingIngredientName.size()>0) {
            query.addCriteria(Criteria.where("ingredientList.name").not().in(exludingIngredientName.toArray(String[]::new)));
        }
        if(includingIngredientName != null && includingIngredientName.size()>0) {
            query.addCriteria(Criteria.where("ingredientList.name").in(includingIngredientName.toArray(String[]::new)));
        }
        if(servingNumber !=null)
            query.addCriteria(Criteria.where("numberOfPeople").lte(servingNumber));
        if(mealType !=null)
            query.addCriteria(Criteria.where("mealType").is(mealType));
        if(instructions != null)
            query.addCriteria(Criteria.where("instructions").regex(instructions));
    }

    private void verifyExcludingIncluding(List<String> includingIngredientName, List<String> exludingIngredientName) {
        if (includingIngredientName.stream()
                .filter(i->exludingIngredientName.contains(i))
                .count() > 0)
            throw new SearchParamException("You cannot use same ingredient for including and excluding");
    }
}
