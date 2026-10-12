package ch.tbz.recipe.planner;

import ch.tbz.recipe.planner.entities.RecipeEntity;
import ch.tbz.recipe.planner.repository.RecipeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingListIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RecipeRepository repository;

    @Test
    void postShoppingListMergesIngredientsOfTwoSeededRecipes() throws Exception {
        List<UUID> ids = repository.findAll().stream()
                .limit(2)
                .map(RecipeEntity::getId)
                .toList();

        mockMvc.perform(post("/api/shopping-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Tomato"))
                .andExpect(jsonPath("$[0].unit").value("PIECE"))
                .andExpect(jsonPath("$[0].amount").value(10));
    }
}
