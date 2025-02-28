package tacos;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import tacos.Ingredient.Type;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

@Profile("!prod")
@Configuration
public class DevelopmentConfig {

  @Bean
  public CommandLineRunner dataLoader(
      IngredientRepository repo,
      UserRepository userRepo,
      PasswordEncoder encoder,
      TacoRepository tacoRepo) {
    return args -> {
      Ingredient fuji = new Ingredient(
          "FUJI", "Fuji Apple", Type.PLAIN);
      Ingredient gala = new Ingredient(
          "GALA", "Gala Apple", Type.PLAIN);
      Ingredient honeycrisp = new Ingredient(
          "HCRP", "Honeycrisp Apple", Type.PLAIN);
      Ingredient redDelicious = new Ingredient(
          "RDLS", "Red Delicious Apple", Type.PLAIN);
      Ingredient hash = new Ingredient(
          "HASH", "Apple Hash", Type.FOOD);
      Ingredient sausage = new Ingredient(
          "SAGE", "Chicken-Apple Sausage", Type.FOOD);
      Ingredient tart = new Ingredient(
          "TART", "Apple Tart", Type.DESSERT);
      Ingredient pie = new Ingredient(
          "APIE", "Apple Pie", Type.DESSERT);
      Ingredient juice = new Ingredient(
          "JUCE", "Apple Juice", Type.DRINK);
      Ingredient cider = new Ingredient(
          "CIDR", "Apple Cider", Type.DRINK);
      repo.save(fuji);
      repo.save(gala);
      repo.save(honeycrisp);
      repo.save(redDelicious);
      repo.save(hash);
      repo.save(sausage);
      repo.save(tart);
      repo.save(pie);
      repo.save(juice);
      repo.save(cider);

      userRepo.save(new User("matt", encoder.encode("password"),
          "Test User", "123 North Street", "Cross Roads", "TX",
          "76227", "123-123-1234"));

      Taco taco1 = new Taco();
      taco1.setName("Applelover");
      taco1.setIngredients(Arrays.asList(
              fuji, honeycrisp, tart,
              pie, cider));
      tacoRepo.save(taco1);

      Taco taco2 = new Taco();
      taco2.setName("Fastfood");
      taco2.setIngredients(Arrays.asList(
              sausage, hash, tart,
              pie));
      tacoRepo.save(taco2);

      Taco taco3 = new Taco();
      taco3.setName("Sweets");
      taco3.setIngredients(Arrays.asList(
              tart, pie, juice,
              cider));
      tacoRepo.save(taco3);

      Taco taco4 = new Taco();
      taco4.setName("Apple4");
      taco4.setIngredients(Arrays.asList(
              fuji, hash, sausage));
      tacoRepo.save(taco4);

      Taco taco5 = new Taco();
      taco5.setName("Matt's Apples");
      taco5.setIngredients(Arrays.asList(
              tart, juice, hash));
      tacoRepo.save(taco5);
    };
  }
  
}
