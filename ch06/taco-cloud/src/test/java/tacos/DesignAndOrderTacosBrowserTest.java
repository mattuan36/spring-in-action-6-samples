package tacos;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Disabled("TODO: Fix this to deal with security stuffs")
public class DesignAndOrderTacosBrowserTest {

  private static HtmlUnitDriver browser;

  @LocalServerPort
  private int port;

  @Autowired
  TestRestTemplate rest;

  @BeforeAll
  public static void setup() {
    browser = new HtmlUnitDriver();
    browser.manage().timeouts()
        .implicitlyWait(10, TimeUnit.SECONDS);
  }

  @AfterAll
  public static void closeBrowser() {
    browser.quit();
  }

  @Test
  public void testDesignATacoPage_HappyPath() throws Exception {
    browser.get(homePageUrl());
    clickDesignATaco();
    assertLandedOnLoginPage();
    doRegistration("testuser", "testpassword");
    assertLandedOnLoginPage();
    doLogin("testuser", "testpassword");
    assertDesignPageElements();
    buildAndSubmitATaco("Basic Apples", "FUJI", "GALA", "HCRP", "RDLS");
    clickBuildAnotherTaco();
    buildAndSubmitATaco("Another Apples", "GALA", "HASH", "SAGE", "TART", "JUCE");
    fillInAndSubmitOrderForm();
    assertThat(browser.getCurrentUrl()).isEqualTo(homePageUrl());
    doLogout();
  }

  @Test
  public void testDesignATacoPage_EmptyOrderInfo() throws Exception {
    browser.get(homePageUrl());
    clickDesignATaco();
    assertLandedOnLoginPage();
    doRegistration("testuser2", "testpassword");
    doLogin("testuser2", "testpassword");
    assertDesignPageElements();
    buildAndSubmitATaco("Basic Apples", "FUJI", "GALA", "HCRP", "RDLS");
    submitEmptyOrderForm();
    fillInAndSubmitOrderForm();
    assertThat(browser.getCurrentUrl()).isEqualTo(homePageUrl());
    doLogout();
  }

  @Test
  public void testDesignATacoPage_InvalidOrderInfo() throws Exception {
    browser.get(homePageUrl());
    clickDesignATaco();
    assertLandedOnLoginPage();
    doRegistration("testuser3", "testpassword");
    doLogin("testuser3", "testpassword");
    assertDesignPageElements();
    buildAndSubmitATaco("Basic Apples", "FUJI", "GALA", "HCRP", "RDLS");
    submitInvalidOrderForm();
    fillInAndSubmitOrderForm();
    assertThat(browser.getCurrentUrl()).isEqualTo(homePageUrl());
    doLogout();
  }

  //
  // Browser test action methods
  //
  private void buildAndSubmitATaco(String name, String... ingredients) {
    assertDesignPageElements();

    for (String ingredient : ingredients) {
      browser.findElementByCssSelector("input[value='" + ingredient + "']").click();
    }
    browser.findElementByCssSelector("input#name").sendKeys(name);
    browser.findElementByCssSelector("form#tacoForm").submit();
  }

  private void assertLandedOnLoginPage() {
    assertThat(browser.getCurrentUrl()).isEqualTo(loginPageUrl());
  }

  private void doRegistration(String username, String password) {
    browser.findElementByLinkText("here").click();
    assertThat(browser.getCurrentUrl()).isEqualTo(registrationPageUrl());
    browser.findElementByName("username").sendKeys(username);
    browser.findElementByName("password").sendKeys(password);
    browser.findElementByName("confirm").sendKeys(password);
    browser.findElementByName("fullname").sendKeys("Test McTest");
    browser.findElementByName("street").sendKeys("1234 Test Street");
    browser.findElementByName("city").sendKeys("Testville");
    browser.findElementByName("state").sendKeys("TX");
    browser.findElementByName("zip").sendKeys("12345");
    browser.findElementByName("phone").sendKeys("123-123-1234");
    browser.findElementByCssSelector("form#registerForm").submit();
  }


  private void doLogin(String username, String password) {
    browser.findElementByCssSelector("input#username").sendKeys(username);
    browser.findElementByCssSelector("input#password").sendKeys(password);
    browser.findElementByCssSelector("form#loginForm").submit();
  }

  private void doLogout() {
    WebElement logoutForm = browser.findElementByCssSelector("form#logoutForm");
    if (logoutForm != null) {
      logoutForm.submit();
    }
  }

  private void assertDesignPageElements() {
    assertThat(browser.getCurrentUrl()).isEqualTo(designPageUrl());
    List<WebElement> ingredientGroups = browser.findElementsByClassName("ingredient-group");
    assertThat(ingredientGroups).hasSize(4);

    WebElement wrapGroup = browser.findElementByCssSelector("div.ingredient-group#plain");
    List<WebElement> wraps = wrapGroup.findElements(By.tagName("div"));
    assertThat(wraps).hasSize(4);
    assertIngredient(wrapGroup, 0, "FUJI", "Fuji Apple");
    assertIngredient(wrapGroup, 1, "GALA", "Gala Apple");
    assertIngredient(wrapGroup, 2, "HCRP", "Honeycrisp Apple");
    assertIngredient(wrapGroup, 3, "RDLS", "Red Delicious Apple");

    WebElement proteinGroup = browser.findElementByCssSelector("div.ingredient-group#food");
    List<WebElement> proteins = proteinGroup.findElements(By.tagName("div"));
    assertThat(proteins).hasSize(2);
    assertIngredient(proteinGroup, 0, "HASH", "Apple Hash");
    assertIngredient(proteinGroup, 1, "SAGE", "Chicken-Apple Sausage");

    WebElement cheeseGroup = browser.findElementByCssSelector("div.ingredient-group#drink");
    List<WebElement> cheeses = proteinGroup.findElements(By.tagName("div"));
    assertThat(cheeses).hasSize(2);
    assertIngredient(cheeseGroup, 0, "JUCE", "Apple Juice");
    assertIngredient(cheeseGroup, 1, "CIDR", "Apple Cider");

    WebElement veggieGroup = browser.findElementByCssSelector("div.ingredient-group#dessert");
    List<WebElement> veggies = proteinGroup.findElements(By.tagName("div"));
    assertThat(veggies).hasSize(2);
    assertIngredient(veggieGroup, 0, "TART", "Apple Tart");
    assertIngredient(veggieGroup, 1, "APIE", "Apple Pie");
  }


  private void fillInAndSubmitOrderForm() {
    assertThat(browser.getCurrentUrl()).startsWith(orderDetailsPageUrl());
    fillField("input#deliveryName", "Ima Hungry");
    fillField("input#deliveryStreet", "1234 Culinary Blvd.");
    fillField("input#deliveryCity", "Foodsville");
    fillField("input#deliveryState", "CO");
    fillField("input#deliveryZip", "81019");
    fillField("input#ccNumber", "4111111111111111");
    fillField("input#ccExpiration", "10/24");
    fillField("input#ccCVV", "123");
    browser.findElementByCssSelector("form#orderForm").submit();
  }

  private void submitEmptyOrderForm() {
    assertThat(browser.getCurrentUrl()).isEqualTo(currentOrderDetailsPageUrl());
    // clear fields automatically populated from user profile
    fillField("input#deliveryName", "");
    fillField("input#deliveryStreet", "");
    fillField("input#deliveryCity", "");
    fillField("input#deliveryState", "");
    fillField("input#deliveryZip", "");
    browser.findElementByCssSelector("form#orderForm").submit();

    assertThat(browser.getCurrentUrl()).isEqualTo(orderDetailsPageUrl());

    List<String> validationErrors = getValidationErrorTexts();
    assertThat(validationErrors)
        .hasSize(9)
        .contains(
            "Please correct the problems below and resubmit.",
            "Delivery name is required",
            "Street is required",
            "City is required",
            "State is required",
            "Zip code is required",
            "Not a valid credit card number",
            "Must be formatted MM/YY",
            "Invalid CVV");
  }

  private List<String> getValidationErrorTexts() {
    List<WebElement> validationErrorElements = browser.findElementsByClassName("validationError");
    List<String> validationErrors = validationErrorElements.stream()
        .map(el -> el.getText())
        .collect(Collectors.toList());
    return validationErrors;
  }

  private void submitInvalidOrderForm() {
    assertThat(browser.getCurrentUrl()).startsWith(orderDetailsPageUrl());
    fillField("input#deliveryName", "I");
    fillField("input#deliveryStreet", "1");
    fillField("input#deliveryCity", "F");
    fillField("input#deliveryState", "C");
    fillField("input#deliveryZip", "8");
    fillField("input#ccNumber", "1234432112344322");
    fillField("input#ccExpiration", "14/91");
    fillField("input#ccCVV", "1234");
    browser.findElementByCssSelector("form#orderForm").submit();

    assertThat(browser.getCurrentUrl()).isEqualTo(orderDetailsPageUrl());

    List<String> validationErrors = getValidationErrorTexts();
    assertThat(validationErrors)
        .hasSize(4)
        .contains(
            "Please correct the problems below and resubmit.",
            "Not a valid credit card number",
            "Must be formatted MM/YY",
            "Invalid CVV");
  }

  private void fillField(String fieldName, String value) {
    WebElement field = browser.findElementByCssSelector(fieldName);
    field.clear();
    field.sendKeys(value);
  }

  private void assertIngredient(WebElement ingredientGroup,
                                int ingredientIdx, String id, String name) {
    List<WebElement> proteins = ingredientGroup.findElements(By.tagName("div"));
    WebElement ingredient = proteins.get(ingredientIdx);
    assertThat(ingredient.findElement(By.tagName("input")).getAttribute("value")).isEqualTo(id);
    assertThat(ingredient.findElement(By.tagName("span")).getText()).isEqualTo(name);
  }

  private void clickDesignATaco() {
    assertThat(browser.getCurrentUrl()).isEqualTo(homePageUrl());
    browser.findElementByCssSelector("a[id='design']").click();
  }

  private void clickBuildAnotherTaco() {
    assertThat(browser.getCurrentUrl()).startsWith(orderDetailsPageUrl());
    browser.findElementByCssSelector("a[id='another']").click();
  }


  //
  // URL helper methods
  //
  private String loginPageUrl() {
    return homePageUrl() + "login";
  }

  private String registrationPageUrl() {
    return homePageUrl() + "register";
  }

  private String designPageUrl() {
    return homePageUrl() + "design";
  }

  private String homePageUrl() {
    return "http://localhost:" + port + "/";
  }

  private String orderDetailsPageUrl() {
    return homePageUrl() + "orders";
  }

  private String currentOrderDetailsPageUrl() {
    return homePageUrl() + "orders/current";
  }

}
