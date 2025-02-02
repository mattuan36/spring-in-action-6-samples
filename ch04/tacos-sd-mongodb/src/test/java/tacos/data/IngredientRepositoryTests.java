package tacos.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tacos.Ingredient;
import tacos.Ingredient.Type;

@SpringBootTest
public class IngredientRepositoryTests {

  @Autowired
  IngredientRepository ingredientRepo;
  
  @Test
  public void findById() {
    Optional<Ingredient> fuji = ingredientRepo.findById("FUJI");
    assertThat(fuji.isPresent()).isTrue();
    assertThat(fuji.get()).isEqualTo(new Ingredient("FUJI", "Fuji Apple", Type.PLAIN));
    
    Optional<Ingredient> xxxx = ingredientRepo.findById("XXXX");
    assertThat(xxxx.isEmpty()).isTrue();

  }
  
}
