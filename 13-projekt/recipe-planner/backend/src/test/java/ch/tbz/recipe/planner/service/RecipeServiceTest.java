package ch.tbz.recipe.planner.service;

import ch.tbz.recipe.planner.domain.Recipe;
import ch.tbz.recipe.planner.entities.RecipeEntity;
import ch.tbz.recipe.planner.mapper.RecipeEntityMapper;
import ch.tbz.recipe.planner.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository repository;

    @Mock
    private RecipeEntityMapper mapper;

    @InjectMocks
    private RecipeService service;

    @Test
    void getRecipeByIdReturnsMappedRecipe() {
        UUID id = UUID.randomUUID();
        RecipeEntity entity = new RecipeEntity();
        Recipe recipe = new Recipe();
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.entityToDomain(entity)).thenReturn(recipe);

        assertThat(service.getRecipeById(id)).contains(recipe);
    }

    @Test
    void getRecipeByIdWithUnknownIdIsEmpty() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThat(service.getRecipeById(id)).isEmpty();
        verifyNoInteractions(mapper);
    }
}
