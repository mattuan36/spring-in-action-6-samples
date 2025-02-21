package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import tacos.Ingredient.Type;
import tacos.data.IngredientRepository;
import tacos.data.UserRepository;

@Profile("!prod")
@Configuration
public class DevelopmentConfig {

  @Bean
  public CommandLineRunner dataLoader(IngredientRepository repo,
        UserRepository userRepo, PasswordEncoder encoder) { // user repo for ease of testing with a built-in user
    return new CommandLineRunner() {
      @Override
      public void run(String... args) throws Exception {
    	repo.deleteAll();
        userRepo.deleteAll();

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
                
        userRepo.save(new User("test", encoder.encode("pw"),
            "Test User", "123 North Street", "Cross Roads", "TX",
            "76227", "123-123-1234"));

        // List<Taco> tacos = List.of(new Taco[]{new Taco(), new Taco()});

//        orderRepo.save(new TacoOrder(new Date(), userRepo.findByUsername("test"), userRepo.findByUsername("test").getFullname(),
//                userRepo.findByUsername("test").getStreet(), userRepo.findByUsername("test").getCity(), userRepo.findByUsername("test").getState(),
//                userRepo.findByUsername("test").getZip(), "4111111111111111", "12/45", "123", tacos));
      }
    };
  }
  
}
