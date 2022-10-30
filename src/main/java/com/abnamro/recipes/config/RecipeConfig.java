package com.abnamro.recipes.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RecipeConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
