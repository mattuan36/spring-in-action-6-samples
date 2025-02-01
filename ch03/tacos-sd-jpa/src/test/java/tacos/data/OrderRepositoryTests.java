package tacos.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;
import tacos.TacoOrder;

@DataJpaTest
public class OrderRepositoryTests {

  @Autowired
  OrderRepository orderRepo;
  
  @Test
  public void saveOrderWithTwoTacos() {
    TacoOrder order = new TacoOrder();
    order.setDeliveryName("Test McTest");
    order.setDeliveryStreet("1234 Test Lane");
    order.setDeliveryCity("Testville");
    order.setDeliveryState("CO");
    order.setDeliveryZip("80123");
    order.setCcNumber("4111111111111111");
    order.setCcExpiration("10/23");
    order.setCcCVV("123");
    Taco taco1 = new Taco();
    taco1.setName("Apple One");
    taco1.addIngredient(new Ingredient("GALA", "Gala Apple", Type.PLAIN));
    taco1.addIngredient(new Ingredient("HASH", "Apple Hash", Type.FOOD));
    taco1.addIngredient(new Ingredient("JUCE", "Apple Juice", Type.DRINK));
    order.addTaco(taco1);
    Taco taco2 = new Taco();
    taco2.setName("Apple Two");
    taco2.addIngredient(new Ingredient("RDLS", "Red Delicious Apple", Type.PLAIN));
    taco2.addIngredient(new Ingredient("APIE", "Apple Pie", Type.DESSERT));
    taco2.addIngredient(new Ingredient("CIDR", "Apple Cider", Type.DRINK));
    order.addTaco(taco2);
    
    TacoOrder savedOrder = orderRepo.save(order);
    assertThat(savedOrder.getId()).isNotNull();
        
    TacoOrder fetchedOrder = orderRepo.findById(savedOrder.getId()).get();
    assertThat(fetchedOrder.getDeliveryName()).isEqualTo("Test McTest");
    assertThat(fetchedOrder.getDeliveryStreet()).isEqualTo("1234 Test Lane");
    assertThat(fetchedOrder.getDeliveryCity()).isEqualTo("Testville");
    assertThat(fetchedOrder.getDeliveryState()).isEqualTo("CO");
    assertThat(fetchedOrder.getDeliveryZip()).isEqualTo("80123");
    assertThat(fetchedOrder.getCcNumber()).isEqualTo("4111111111111111");
    assertThat(fetchedOrder.getCcExpiration()).isEqualTo("10/23");
    assertThat(fetchedOrder.getCcCVV()).isEqualTo("123");
    assertThat(fetchedOrder.getPlacedAt().getTime()).isEqualTo(savedOrder.getPlacedAt().getTime());
    List<Taco> tacos = fetchedOrder.getTacos();
    assertThat(tacos.size()).isEqualTo(2);
    assertThat(tacos).containsExactlyInAnyOrder(taco1, taco2);
  }
  
}
