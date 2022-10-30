package com.abnamro.recipes.exception;

public class RecipeAlreadyExistException extends RuntimeException{
    public RecipeAlreadyExistException(String message){
        super(message);
    }
}
