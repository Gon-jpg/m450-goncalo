package ch.tbz.recipe.planner.service;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ShoppingListServiceTest {

    private ShoppingListService service;

    @BeforeEach
    void setUp() {
        service = new ShoppingListService();
    }

    private Ingredient ingredient(String name, Unit unit, int amount) {
        return new Ingredient(UUID.randomUUID(), name, "", unit, amount);
    }

    private Recipe recipe(String name, Ingredient... ingredients) {
        return new Recipe(UUID.randomUUID(), name, "", "", List.of(ingredients));
    }

    @Test
    void noRecipesGivesEmptyList() {
        assertThat(service.create(List.of())).isEmpty();
    }

    @Test
    void recipeWithoutIngredientsGivesEmptyList() {
        assertThat(service.create(List.of(recipe("Water")))).isEmpty();
    }

    @Test
    void singleRecipeReturnsItsIngredients() {
        Recipe soup = recipe("Soup",
                ingredient("Carrot", Unit.PIECE, 3),
                ingredient("Water", Unit.LITRE, 1));

        List<Ingredient> result = service.create(List.of(soup));

        assertThat(result)
                .extracting(Ingredient::getName, Ingredient::getUnit, Ingredient::getAmount)
                .containsExactly(
                        tuple("Carrot", Unit.PIECE, 3),
                        tuple("Water", Unit.LITRE, 1));
    }

    @Test
    void sameNameAndUnitAreMergedAndAmountsAdded() {
        Recipe gratin = recipe("Gratin", ingredient("Potato", Unit.GRAMM, 500));
        Recipe salad = recipe("Salad", ingredient("potato", Unit.GRAMM, 300));

        List<Ingredient> result = service.create(List.of(gratin, salad));

        assertThat(result)
                .extracting(Ingredient::getName, Ingredient::getUnit, Ingredient::getAmount)
                .containsExactly(tuple("Potato", Unit.GRAMM, 800));
    }

    @Test
    void namesAreComparedIgnoringCaseAndSurroundingSpaces() {
        Recipe first = recipe("A", ingredient("Potato", Unit.GRAMM, 100));
        Recipe second = recipe("B", ingredient("  POTATO ", Unit.GRAMM, 200));

        List<Ingredient> result = service.create(List.of(first, second));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAmount()).isEqualTo(300);
    }

    @Test
    void sameNameWithDifferentUnitStaysSeparate() {
        Recipe cake = recipe("Cake", ingredient("Milk", Unit.LITRE, 1));
        Recipe pudding = recipe("Pudding", ingredient("Milk", Unit.DECILITRE, 2));

        List<Ingredient> result = service.create(List.of(cake, pudding));

        assertThat(result)
                .extracting(Ingredient::getName, Ingredient::getUnit, Ingredient::getAmount)
                .containsExactlyInAnyOrder(
                        tuple("Milk", Unit.LITRE, 1),
                        tuple("Milk", Unit.DECILITRE, 2));
    }

    @Test
    void resultIsSortedByName() {
        Recipe recipe = recipe("Mixed",
                ingredient("Tomato", Unit.PIECE, 2),
                ingredient("Apple", Unit.PIECE, 1),
                ingredient("Milk", Unit.LITRE, 1));

        List<Ingredient> result = service.create(List.of(recipe));

        assertThat(result).extracting(Ingredient::getName).containsExactly("Apple", "Milk", "Tomato");
    }
}
