package com.abnamro.recipes.config;

import com.abnamro.recipes.util.ApiConstants;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.management.Query;
import java.time.Duration;

@Component
public class RecipeConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder) {

        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(ApiConstants.TIME_OUT))
                .setReadTimeout(Duration.ofSeconds(ApiConstants.TIME_OUT))
                .build();
    }

    @Bean
    public Query query() {
        return new Query();
    }

}
