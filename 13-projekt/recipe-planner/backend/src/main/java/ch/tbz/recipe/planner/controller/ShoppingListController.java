package ch.tbz.recipe.planner.controller;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.service.RecipeService;
import ch.tbz.recipe.planner.service.ShoppingListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000"})
public class ShoppingListController {

    private final RecipeService recipeService;

    private final ShoppingListService shoppingListService;

    public ShoppingListController(RecipeService recipeService, ShoppingListService shoppingListService) {
        this.recipeService = recipeService;
        this.shoppingListService = shoppingListService;
    }

    @PostMapping("/api/shopping-list")
    public ResponseEntity<List<Ingredient>> create(@RequestBody List<UUID> recipeIds) {
        List<Recipe> recipes = new ArrayList<>();
        for (UUID recipeId : recipeIds) {
            Optional<Recipe> recipe = recipeService.getRecipeById(recipeId);
            if (recipe.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            recipes.add(recipe.get());
        }
        return ResponseEntity.ok(shoppingListService.create(recipes));
    }

}
