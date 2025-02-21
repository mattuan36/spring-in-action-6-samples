package tacos;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Disabled("TODO: Need to get around authentication in this test")
public class DesignTacoControllerBrowserTest {
  
  private static ChromeDriver browser;
  
  @LocalServerPort
  private int port;
  
  @Autowired
  TestRestTemplate rest;
  
  @BeforeAll
  public static void openBrowser() {
    //System.setProperty("webdriver.chrome.driver","D:\\List_of_Jar\\chromedriver.exe");
    browser = new ChromeDriver();
    browser.manage().timeouts()
        .implicitlyWait(10, TimeUnit.SECONDS);
  }
  
  @AfterAll
  public static void closeBrowser() {
    browser.quit();
  }
  
  @Test
  @Disabled("TODO: Need to get around authentication in this test")
  public void testDesignATacoPage() throws Exception {
    browser.get("http://localhost:" + port + "/design");

    List<WebElement> ingredientGroups = browser.findElementsByClassName("ingredient-group");
    assertThat(ingredientGroups).hasSize(4);
    
    WebElement wrapGroup = ingredientGroups.get(0);
    List<WebElement> wraps = wrapGroup.findElements(By.tagName("div"));
    assertThat(wraps).hasSize(4);
    assertIngredient(wrapGroup, 0, "FUJI", "Fuji Apple");
    assertIngredient(wrapGroup, 1, "GALA", "Gala Apple");
    assertIngredient(wrapGroup, 2, "HCRP", "Honeycrisp Apple");
    assertIngredient(wrapGroup, 3, "RDLS", "Red Delicious Apple");
    
    WebElement proteinGroup = ingredientGroups.get(1);
    List<WebElement> proteins = proteinGroup.findElements(By.tagName("div"));
    assertThat(proteins).hasSize(2);
    assertIngredient(proteinGroup, 0, "HASH", "Apple Hash");
    assertIngredient(proteinGroup, 1, "SAGE", "Chicken-Apple Sausage");
  }
  
  private void assertIngredient(WebElement ingredientGroup, 
                                int ingredientIdx, String id, String name) {
    List<WebElement> proteins = ingredientGroup.findElements(By.tagName("div"));
    WebElement ingredient = proteins.get(ingredientIdx);
    assertThat(ingredient.findElement(By.tagName("input")).getAttribute("value")).isEqualTo(id);
    assertThat(ingredient.findElement(By.tagName("span")).getText()).isEqualTo(name);
  }
  
}
