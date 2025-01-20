package tacos.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import tacos.Ingredient;
import tacos.Ingredient.Type;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {

  private Map<String, Ingredient> ingredientMap = new HashMap<>();
  
  public IngredientByIdConverter() {
    ingredientMap.put("FUJI", new Ingredient("FUJI", "Fuji Apple", Type.PLAIN));
    ingredientMap.put("GALA", new Ingredient("GALA", "Gala Apple", Type.PLAIN));
    ingredientMap.put("HCRP", new Ingredient("HCRP", "Honeycrisp Apple", Type.PLAIN));
    ingredientMap.put("RDLS", new Ingredient("RDLS", "Red Delicious Apple", Type.PLAIN));
    ingredientMap.put("HASH", new Ingredient("HASH", "Apple Hash", Type.FOOD));
    ingredientMap.put("SAGE", new Ingredient("SAGE", "Chicken-Apple Sausage", Type.FOOD));
    ingredientMap.put("TART", new Ingredient("TART", "Apple Tart", Type.DESSERT));
    ingredientMap.put("APIE", new Ingredient("APIE", "Apple Pie", Type.DESSERT));
    ingredientMap.put("JUCE", new Ingredient("JUCE", "Apple Juice", Type.DRINK));
    ingredientMap.put("CIDR", new Ingredient("CIDR", "Apple Cider", Type.DRINK));
  }
  
  @Override
  public Ingredient convert(String id) {
    return ingredientMap.get(id);
  }

}
