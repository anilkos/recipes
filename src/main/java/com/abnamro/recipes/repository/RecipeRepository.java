package com.abnamro.recipes.repository;

import com.abnamro.recipes.entity.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe,String> {
    @Query("{ 'recipeName' : ?0 }")
    Optional<Recipe> findByTheRecipesRecipeName(String recipeName);


}
