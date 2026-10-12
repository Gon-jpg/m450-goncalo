package ch.tbz.recipe.planner.controller;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.service.RecipeService;
import ch.tbz.recipe.planner.service.ShoppingListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
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
        List<Recipe> recipes = recipeIds.stream()
                .map(recipeService::getRecipeById)
                .filter(Objects::nonNull)
                .toList();
        return new ResponseEntity<>(shoppingListService.create(recipes), HttpStatus.OK);
    }

}
