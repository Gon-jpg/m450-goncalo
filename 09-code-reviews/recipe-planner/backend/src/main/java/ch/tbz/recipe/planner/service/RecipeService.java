package ch.tbz.recipe.planner.service;

import ch.tbz.recipe.planner.entities.IngredientEntity;
import ch.tbz.recipe.planner.entities.RecipeEntity;
import ch.tbz.recipe.planner.mapper.RecipeEntityMapper;
import ch.tbz.recipe.planner.repository.RecipeRepository;
import ch.tbz.recipe.planner.domain.Recipe;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    RecipeEntityMapper mapper;

    RecipeRepository repository;

    public RecipeService(RecipeEntityMapper mapper, RecipeRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public List<Recipe> getRecipes() {
        List<RecipeEntity> recipesEntities = repository.findAll();
        return recipesEntities.stream().map(mapper::entityToDomain).toList();
    }

    public Recipe getRecipeById(UUID recipeId) {
        return mapper.entityToDomain(repository.findById(recipeId).orElse(null));
    }

    public Recipe addRecipe(Recipe recipe) {
        var createdRecipe = repository.save(mapper.domainToEntity(recipe));
        return mapper.entityToDomain(createdRecipe);
    }

    public Optional<Recipe> updateRecipe(UUID recipeId, Recipe recipe) {
        return repository.findById(recipeId).map(existing -> {
            recipe.setId(recipeId);
            if (recipe.getIngredients() == null) {
                recipe.setIngredients(new ArrayList<>());
            }
            Set<UUID> ownIngredientIds = existing.getIngredients() == null
                    ? Set.of()
                    : existing.getIngredients().stream().map(IngredientEntity::getId).collect(Collectors.toSet());
            recipe.getIngredients().forEach(ingredient -> {
                if (ingredient.getId() != null && !ownIngredientIds.contains(ingredient.getId())) {
                    ingredient.setId(null);
                }
            });
            var updatedRecipe = repository.save(mapper.domainToEntity(recipe));
            return mapper.entityToDomain(updatedRecipe);
        });
    }
}
