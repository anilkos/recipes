package com.abnamro.recipes.handler;


import com.abnamro.recipes.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.naming.AuthenticationException;
import java.time.LocalDate;

@RestControllerAdvice
public class RecipeExceptionHandler {


    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public final ErrorResponse handleAuthenticationException(Exception ex, WebRequest request) {
        return ErrorResponse.builder()
                .errorDate(LocalDate.now())
                .details(request.getDescription(false))
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(LoginSessionExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public final ErrorResponse handleLoginSessionExpiredException(Exception ex,WebRequest request) {
        return ErrorResponse.builder()
                .errorDate(LocalDate.now())
                .details(request.getDescription(false))
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(RecipeAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse recipeAlreadyExistException(Exception ex,WebRequest request) {
        return ErrorResponse.builder()
                .errorDate(LocalDate.now())
                .details(request.getDescription(false))
                .message(ex.getMessage())
                .build();
    }
    @ExceptionHandler(RecipeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse recipeNotFoundException(Exception ex,WebRequest request) {
        return ErrorResponse.builder()
                .errorDate(LocalDate.now())
                .details(request.getDescription(false))
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(SearchParamException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse searchParamException(Exception ex,WebRequest request) {
        return ErrorResponse.builder()
                .errorDate(LocalDate.now())
                .details(request.getDescription(false))
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllOtherExceptions(Exception ex,WebRequest request) {
        return ErrorResponse.builder()
                .errorDate(LocalDate.now())
                .details(request.getDescription(false))
                .message(ex.getMessage())
                .build();
    }

}
