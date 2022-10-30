package com.abnamro.recipes.entity;


import com.abnamro.recipes.Model.Ingredient;
import com.abnamro.recipes.Model.MealType;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "recipes")
@Data
@NoArgsConstructor
public class Recipe {
    @Id
    @Generated
    private String id;
    private String recipeName;
    private MealType mealType;
    private List<Ingredient> ingredientList;
    private String instructions;
    private BigDecimal numberOfPeople;
}
