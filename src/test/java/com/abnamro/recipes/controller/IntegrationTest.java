package com.abnamro.recipes.controller;

import com.abnamro.recipes.Model.Dto.IngredientDto;
import com.abnamro.recipes.Model.Dto.RecipeRequestDto;
import com.abnamro.recipes.Model.Dto.RecipeResponseDto;
import com.abnamro.recipes.Model.Dto.WebUserDto;
import com.abnamro.recipes.Model.MealType;
import com.abnamro.recipes.RecipesApplication;
import com.abnamro.recipes.util.ApiConstants;
import com.abnamro.recipes.util.ApiMapping;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = RecipesApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTest {

    private final String host = "http://localhost:";
    String token = "";
    @Value("${login.username}")
    String user;
    @Value("${login.password}")
    String password;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    private String recipeName;

    @BeforeEach
    void setToken() throws JSONException {
        WebUserDto webUserDto = new WebUserDto(user, password);
        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity(host + port + "/login", webUserDto, String.class);
        token = "Bearer " + new JSONObject(responseEntity.getBody()).getString("token");
        recipeName = "NewRecipe2";
    }


    @Order(1)
    @Test
    void CreateRecipeIntegrationTest() {
        List<IngredientDto> ingredientDtoList = List.of(new IngredientDto("Chickpeas", "gram", BigDecimal.valueOf(100)),
                new IngredientDto("Tahini", "gram", BigDecimal.valueOf(50)),
                new IngredientDto("OliveOil", "liter", BigDecimal.valueOf(1)),
                new IngredientDto("Lemon", "piece", BigDecimal.valueOf(1)));
        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(recipeName, ingredientDtoList, MealType.VEGAN, "Use tahini and chickpeas squize lemon juice", BigDecimal.TEN);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<RecipeRequestDto> request = new HttpEntity<>(recipeRequestDto, headers);
        ResponseEntity responseEntity = this.restTemplate
                .postForEntity(host + port + "/" + ApiConstants.API_VERSION + ApiMapping.ADD_RECIPE, request, RecipeRequestDto.class);

        assertEquals(201, responseEntity.getStatusCodeValue());

    }

    @Test
    @Order(2)
    void UpdateRecipeIntegrationTest() {
        List<IngredientDto> ingredientDtoList = List.of(new IngredientDto("Chickpeas", "gram", BigDecimal.valueOf(100)),
                new IngredientDto("Tahini", "gram", BigDecimal.valueOf(50)),
                new IngredientDto("OliveOil", "liter", BigDecimal.valueOf(1)),
                new IngredientDto("Lemon", "piece", BigDecimal.valueOf(1)));
        RecipeRequestDto recipeRequestDto = new RecipeRequestDto(recipeName, ingredientDtoList, MealType.MEATY, "Use tahini and chickpeas squize lemon juice", BigDecimal.TEN);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<RecipeRequestDto> request = new HttpEntity<>(recipeRequestDto, headers);
        String url = host + port + "/" + ApiConstants.API_VERSION + ApiMapping.UPDATE_RECIPE;
        ResponseEntity responseEntity = this.restTemplate.exchange
                (url, HttpMethod.PUT,request,RecipeResponseDto.class);

        assertEquals(202, responseEntity.getStatusCodeValue());
        RecipeResponseDto recipeResponseDto = (RecipeResponseDto) responseEntity.getBody();
        assertThat(recipeResponseDto.getMealType()).isEqualTo(MealType.MEATY);
    }

    @Test
    @Order(3)
    void SearchRecipeIntegrationTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        String url = host + port + "/" + ApiConstants.API_VERSION + ApiMapping.SEARCH_RECIPE;

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("meatType", MealType.MEATY.toString());
        url = uriBuilder.toUriString();
        HttpEntity<RecipeRequestDto> request = new HttpEntity<>(headers);
        ResponseEntity responseEntity = this.restTemplate.exchange
                (url, HttpMethod.GET,request,RecipeResponseDto[].class);

        assertEquals(200, responseEntity.getStatusCodeValue());
        RecipeResponseDto[] recipeResponseDto = (RecipeResponseDto[]) responseEntity.getBody();
        assertThat(recipeResponseDto).hasSizeGreaterThan(0);
        assertThat(recipeResponseDto[0].getMealType()).isEqualTo(MealType.MEATY);
    }

    @Test
    @Order(4)
    void DeleteRecipeIntegrationTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        String url = host + port + "/" + ApiConstants.API_VERSION + ApiMapping.REMOVE_RECIPE;
        Map<String, String> params = new HashMap<>();
        params.put("recipeName", recipeName);
        URI uri = UriComponentsBuilder.fromUriString(url)
                .buildAndExpand(params)
                .toUri();
        HttpEntity<RecipeRequestDto> request = new HttpEntity<>(headers);
        ResponseEntity responseEntity = this.restTemplate.exchange
                (uri, HttpMethod.DELETE,request,ResponseEntity.class);

        assertEquals(204, responseEntity.getStatusCodeValue());
    }
}
