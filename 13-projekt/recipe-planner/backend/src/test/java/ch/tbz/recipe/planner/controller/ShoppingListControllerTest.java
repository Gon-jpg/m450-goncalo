package ch.tbz.recipe.planner.controller;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.repository.RecipeRepository;
import ch.tbz.recipe.planner.service.RecipeService;
import ch.tbz.recipe.planner.service.ShoppingListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShoppingListController.class)
class ShoppingListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private ShoppingListService shoppingListService;

    @MockBean
    private RecipeRepository repository;

    private Recipe recipe(String name) {
        return new Recipe(UUID.randomUUID(), name, "", "", List.of());
    }

    @Test
    void createLoadsRecipesInBodyOrderAndReturnsShoppingList() throws Exception {
        Recipe soup = recipe("Soup");
        Recipe pasta = recipe("Pasta");
        when(recipeService.getRecipeById(soup.getId())).thenReturn(soup);
        when(recipeService.getRecipeById(pasta.getId())).thenReturn(pasta);
        when(shoppingListService.create(anyList())).thenReturn(List.of(
                new Ingredient(null, "Carrot", "", Unit.PIECE, 3),
                new Ingredient(null, "Flour", "", Unit.GRAMM, 500)));

        mockMvc.perform(post("/api/shopping-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(soup.getId(), pasta.getId()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Carrot"));

        verify(shoppingListService).create(List.of(soup, pasta));
    }

    @Test
    void emptyBodyGivesEmptyList() throws Exception {
        mockMvc.perform(post("/api/shopping-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void unknownIdIsIgnored() throws Exception {
        Recipe soup = recipe("Soup");
        UUID unknownId = UUID.randomUUID();
        when(recipeService.getRecipeById(soup.getId())).thenReturn(soup);
        when(recipeService.getRecipeById(unknownId)).thenReturn(null);
        when(shoppingListService.create(anyList())).thenReturn(List.of());

        mockMvc.perform(post("/api/shopping-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(unknownId, soup.getId()))))
                .andExpect(status().isOk());

        verify(shoppingListService).create(List.of(soup));
    }
}
