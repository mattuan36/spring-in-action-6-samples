package tacos;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import tacos.Ingredient.Type;
import tacos.data.IngredientRepository;
import tacos.data.OrderRepository;
import tacos.web.DesignTacoController;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DesignTacoController.class)
public class DesignTacoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private List<Ingredient> ingredients;

  private Taco design;

  @MockBean
  private IngredientRepository ingredientRepository;

  @MockBean
  private OrderRepository orderRepository;

  @BeforeEach
  public void setup() {
    ingredients = Arrays.asList(
            new Ingredient("FUJI", "Fuji Apple", Type.PLAIN),
            new Ingredient("GALA", "Gala Apple", Type.PLAIN),
            new Ingredient("HCRP", "Honeycrisp Apple", Type.PLAIN),
            new Ingredient("RDLS", "Red Delicious Apple", Type.PLAIN),
            new Ingredient("HASH", "Apple Hash", Type.FOOD),
            new Ingredient("SAGE", "Chicken-Apple Sausage", Type.FOOD),
            new Ingredient("TART", "Apple Tart", Type.DESSERT),
            new Ingredient("APIE", "Apple Pie", Type.DESSERT),
            new Ingredient("JUCE", "Apple Juice", Type.DRINK),
            new Ingredient("CIDR", "Apple Cider", Type.DRINK)
    );

    when(ingredientRepository.findAll())
        .thenReturn(ingredients);

    when(ingredientRepository.findById("FUJI")).thenReturn(Optional.of(new Ingredient("FUJI", "Fuji Apple", Type.PLAIN)));
    when(ingredientRepository.findById("HASH")).thenReturn(Optional.of(new Ingredient("HASH", "Apple Hash", Type.FOOD)));
    when(ingredientRepository.findById("TART")).thenReturn(Optional.of(new Ingredient("TART", "Apple Tart", Type.DESSERT)));
    design = new Taco();
    design.setName("Test Taco");

    design.setIngredients(
        Arrays.asList(
            new Ingredient("FUJI", "Fuji Apple", Type.PLAIN),
            new Ingredient("HASH", "Apple Hash", Type.FOOD),
            new Ingredient("TART", "Apple Tart", Type.DESSERT)));

  }

  @Test
  public void testShowDesignForm() throws Exception {
    mockMvc.perform(get("/design"))
        .andExpect(status().isOk())
        .andExpect(view().name("design"))
        .andExpect(model().attribute("plain", ingredients.subList(0, 4)))
        .andExpect(model().attribute("food", ingredients.subList(4, 6)))
        .andExpect(model().attribute("dessert", ingredients.subList(6, 8)))
        .andExpect(model().attribute("drink", ingredients.subList(8, 10)));
  }

  @Test
  public void processTaco() throws Exception {
    mockMvc.perform(post("/design")
        .content("name=Test+Taco&ingredients=FUJI,TART,SAGE")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().stringValues("Location", "/orders/current"));
  }

}
