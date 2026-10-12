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
            if (recipe.getIngredients() == null) {
                continue;
            }
            for (Ingredient ingredient : recipe.getIngredients()) {
                String name = ingredient.getName().trim();
                merged.merge(name.toLowerCase() + "|" + ingredient.getUnit(),
                        new Ingredient(null, name, "", ingredient.getUnit(), ingredient.getAmount()),
                        (first, next) -> new Ingredient(null, first.getName(), "", first.getUnit(),
                                first.getAmount() + next.getAmount()));
            }
        }

        List<Ingredient> result = new ArrayList<>(merged.values());
        result.sort(Comparator.comparing(Ingredient::getName, String.CASE_INSENSITIVE_ORDER));
        return result;
    }
}
