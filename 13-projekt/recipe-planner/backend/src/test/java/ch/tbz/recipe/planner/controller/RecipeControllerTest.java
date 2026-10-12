package ch.tbz.recipe.planner.controller;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.mapper.RecipeEntityMapper;
import ch.tbz.recipe.planner.repository.RecipeRepository;
import ch.tbz.recipe.planner.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RecipeService service;

    @MockBean
    private RecipeEntityMapper mapper;

    @MockBean
    private RecipeRepository repository;

    private Recipe soup;
    private Recipe pasta;

    @BeforeEach
    void setUp() {
        soup = new Recipe(UUID.randomUUID(), "Soup", "Hot", "http://img/soup.jpg",
                List.of(new Ingredient(UUID.randomUUID(), "Carrot", "peeled", Unit.PIECE, 3)));
        pasta = new Recipe(UUID.randomUUID(), "Pasta", "Quick", "http://img/pasta.jpg", List.of());
    }

    @Test
    void getRecipesReturns200AndList() throws Exception {
        when(service.getRecipes()).thenReturn(List.of(soup, pasta));

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Soup"))
                .andExpect(jsonPath("$[1].name").value("Pasta"));
    }

    @Test
    void getRecipeByIdReturns200AndRecipe() throws Exception {
        when(service.getRecipeById(soup.getId())).thenReturn(Optional.of(soup));

        mockMvc.perform(get("/api/recipes/recipe/{id}", soup.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Soup"))
                .andExpect(jsonPath("$.ingredients[0].name").value("Carrot"))
                .andExpect(jsonPath("$.ingredients[0].unit").value("PIECE"));
    }

    @Test
    void addRecipeReturns200AndCallsService() throws Exception {
        when(service.addRecipe(any())).thenReturn(soup);

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(soup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Soup"));

        verify(service).addRecipe(any());
    }

    @Test
    void getRecipeWithUnknownIdReturns404() throws Exception {
        UUID unknownId = UUID.randomUUID();
        when(service.getRecipeById(unknownId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/recipes/recipe/{id}", unknownId))
                .andExpect(status().isNotFound());
    }
}
