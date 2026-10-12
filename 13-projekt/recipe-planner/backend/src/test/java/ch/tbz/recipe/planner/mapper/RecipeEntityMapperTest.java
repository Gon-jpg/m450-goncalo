package ch.tbz.recipe.planner.mapper;

import ch.tbz.recipe.planner.domain.Ingredient;
import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.domain.Unit;
import ch.tbz.recipe.planner.entities.IngredientEntity;
import ch.tbz.recipe.planner.entities.RecipeEntity;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class RecipeEntityMapperTest {

    private final RecipeEntityMapper recipeMapper = new RecipeEntityMapperImpl();
    private final IngredientEntityMapper ingredientMapper = new IngredientEntityMapperImpl();

    private final UUID recipeId = UUID.randomUUID();
    private final UUID ingredientId = UUID.randomUUID();

    private IngredientEntity ingredientEntity() {
        return new IngredientEntity(ingredientId, "Flour", "sifted", Unit.GRAMM, 250);
    }

    private Ingredient ingredient() {
        return new Ingredient(ingredientId, "Flour", "sifted", Unit.GRAMM, 250);
    }

    @Test
    void recipeEntityToDomain() {
        RecipeEntity entity = new RecipeEntity(recipeId, "Soup", "Hot", "http://img/soup.jpg", List.of(ingredientEntity()));

        Recipe domain = recipeMapper.entityToDomain(entity);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(domain.getId()).isEqualTo(recipeId);
        softly.assertThat(domain.getName()).isEqualTo("Soup");
        softly.assertThat(domain.getDescription()).isEqualTo("Hot");
        softly.assertThat(domain.getImageUrl()).isEqualTo("http://img/soup.jpg");
        softly.assertThat(domain.getIngredients()).hasSize(1);
        Ingredient first = domain.getIngredients().get(0);
        softly.assertThat(first.getId()).isEqualTo(ingredientId);
        softly.assertThat(first.getName()).isEqualTo("Flour");
        softly.assertThat(first.getComment()).isEqualTo("sifted");
        softly.assertThat(first.getUnit()).isEqualTo(Unit.GRAMM);
        softly.assertThat(first.getAmount()).isEqualTo(250);
        softly.assertAll();
    }

    @Test
    void recipeDomainToEntity() {
        Recipe domain = new Recipe(recipeId, "Soup", "Hot", "http://img/soup.jpg", List.of(ingredient()));

        RecipeEntity entity = recipeMapper.domainToEntity(domain);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(entity.getId()).isEqualTo(recipeId);
            softly.assertThat(entity.getName()).isEqualTo("Soup");
            softly.assertThat(entity.getDescription()).isEqualTo("Hot");
            softly.assertThat(entity.getImageUrl()).isEqualTo("http://img/soup.jpg");
            softly.assertThat(entity.getIngredients()).hasSize(1);
            IngredientEntity first = entity.getIngredients().get(0);
            softly.assertThat(first.getId()).isEqualTo(ingredientId);
            softly.assertThat(first.getName()).isEqualTo("Flour");
            softly.assertThat(first.getComment()).isEqualTo("sifted");
            softly.assertThat(first.getUnit()).isEqualTo(Unit.GRAMM);
            softly.assertThat(first.getAmount()).isEqualTo(250);
        });
    }

    @Test
    void recipeMappersReturnNullForNullInput() {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(recipeMapper.entityToDomain(null)).isNull();
            softly.assertThat(recipeMapper.domainToEntity(null)).isNull();
        });
    }

    @Test
    void ingredientEntityToDomain() {
        Ingredient domain = ingredientMapper.entityToDomain(ingredientEntity());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(domain.getId()).isEqualTo(ingredientId);
            softly.assertThat(domain.getName()).isEqualTo("Flour");
            softly.assertThat(domain.getComment()).isEqualTo("sifted");
            softly.assertThat(domain.getUnit()).isEqualTo(Unit.GRAMM);
            softly.assertThat(domain.getAmount()).isEqualTo(250);
        });
    }

    @Test
    void ingredientDomainToEntity() {
        IngredientEntity entity = ingredientMapper.domainToEntity(ingredient());

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(entity.getId()).isEqualTo(ingredientId);
            softly.assertThat(entity.getName()).isEqualTo("Flour");
            softly.assertThat(entity.getComment()).isEqualTo("sifted");
            softly.assertThat(entity.getUnit()).isEqualTo(Unit.GRAMM);
            softly.assertThat(entity.getAmount()).isEqualTo(250);
        });
    }

    @Test
    void ingredientListsAreMappedInBothDirections() {
        UUID secondId = UUID.randomUUID();
        List<Ingredient> domains = ingredientMapper.entitiesToDomains(
                List.of(ingredientEntity(), new IngredientEntity(secondId, "Milk", "", Unit.LITRE, 1)));
        List<IngredientEntity> entities = ingredientMapper.domainsToEntities(
                List.of(ingredient(), new Ingredient(secondId, "Milk", "", Unit.LITRE, 1)));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(domains).extracting(Ingredient::getName).containsExactly("Flour", "Milk");
            softly.assertThat(domains).extracting(Ingredient::getUnit).containsExactly(Unit.GRAMM, Unit.LITRE);
            softly.assertThat(entities).extracting(IngredientEntity::getId).containsExactly(ingredientId, secondId);
            softly.assertThat(entities).extracting(IngredientEntity::getAmount).containsExactly(250, 1);
        });
    }

    @Test
    void ingredientMappersReturnNullForNullInput() {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(ingredientMapper.entityToDomain(null)).isNull();
            softly.assertThat(ingredientMapper.domainToEntity(null)).isNull();
            softly.assertThat(ingredientMapper.entitiesToDomains(null)).isNull();
            softly.assertThat(ingredientMapper.domainsToEntities(null)).isNull();
        });
    }
}
