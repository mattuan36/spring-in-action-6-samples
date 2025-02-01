package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import tacos.Ingredient.Type;
import tacos.data.IngredientRepository;

@SpringBootApplication
public class TacoCloudApplication {

  public static void main(String[] args) {
    SpringApplication.run(TacoCloudApplication.class, args);
  }

  @Bean
  public CommandLineRunner dataLoader(IngredientRepository repo) {
    return new CommandLineRunner() {
      @Override
      public void run(String... args) throws Exception {
        repo.save(new Ingredient("FUJI", "Fuji Apple", Type.PLAIN));
        repo.save(new Ingredient("GALA", "Gala Apple", Type.PLAIN));
        repo.save(new Ingredient("HCRP", "Honeycrisp Apple", Type.PLAIN));
        repo.save(new Ingredient("RDLS", "Red Delicious Apple", Type.PLAIN));
        repo.save(new Ingredient("HASH", "Apple Hash", Type.FOOD));
        repo.save(new Ingredient("SAGE", "Chicken-Apple Sausage", Type.FOOD));
        repo.save(new Ingredient("TART", "Apple Tart", Type.DESSERT));
        repo.save(new Ingredient("APIE", "Apple Pie", Type.DESSERT));
        repo.save(new Ingredient("JUCE", "Apple Juice", Type.DRINK));
        repo.save(new Ingredient("CIDR", "Apple Cider", Type.DRINK));
      }
    };
  }
  
}
