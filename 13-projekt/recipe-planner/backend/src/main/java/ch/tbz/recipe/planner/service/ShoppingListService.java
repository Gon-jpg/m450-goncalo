package ch.tbz.recipe.planner.service;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShoppingListService {

    public List<Ingredient> create(List<Recipe> recipes) {
        Map<String, Ingredient> merged = new LinkedHashMap<>();

        for (Recipe recipe : recipes) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                String key = ingredient.getName().trim().toLowerCase() + "|" + ingredient.getUnit();
                Ingredient existing = merged.get(key);
                int amount = existing == null
                        ? ingredient.getAmount()
                        : existing.getAmount() + ingredient.getAmount();
                String name = existing == null ? ingredient.getName() : existing.getName();
                merged.put(key, new Ingredient(null, name, "", ingredient.getUnit(), amount));
            }
        }

        List<Ingredient> result = new ArrayList<>(merged.values());
        result.sort(Comparator.comparing(Ingredient::getName));
        return result;
    }
}
