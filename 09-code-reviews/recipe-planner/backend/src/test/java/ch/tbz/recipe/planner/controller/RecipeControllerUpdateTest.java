package ch.tbz.recipe.planner.controller;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.Unit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecipeControllerUpdateTest {

    @Autowired
    private TestRestTemplate rest;

    private Recipe create() {
        Recipe recipe = new Recipe(null, "Soup", "Hot", "",
                new ArrayList<>(List.of(new Ingredient(null, "Potato", "waxy", Unit.GRAMM, 500))));
        return rest.postForObject("/api/recipes", recipe, Recipe.class);
    }

    private ResponseEntity<Recipe> put(UUID id, Recipe recipe) {
        return rest.exchange("/api/recipes/recipe/" + id, HttpMethod.PUT, new HttpEntity<>(recipe), Recipe.class);
    }

    @Test
    void updateKeepsIngredientIdsWithoutDuplicates() {
        Recipe created = create();
        UUID ingredientId = created.getIngredients().get(0).getId();
        created.setName("Better Soup");
        created.getIngredients().get(0).setAmount(700);

        ResponseEntity<Recipe> response = put(created.getId(), created);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Recipe loaded = rest.getForObject("/api/recipes/recipe/" + created.getId(), Recipe.class);
        assertEquals("Better Soup", loaded.getName());
        assertEquals(1, loaded.getIngredients().size());
        assertEquals(ingredientId, loaded.getIngredients().get(0).getId());
        assertEquals(700, loaded.getIngredients().get(0).getAmount());
    }

    @Test
    void pathIdWinsOverBodyId() {
        Recipe created = create();
        UUID pathId = created.getId();
        created.setId(UUID.randomUUID());

        ResponseEntity<Recipe> response = put(pathId, created);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(pathId, response.getBody().getId());
    }

    @Test
    void unknownIdReturns404() {
        ResponseEntity<Recipe> response = put(UUID.randomUUID(), create());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
