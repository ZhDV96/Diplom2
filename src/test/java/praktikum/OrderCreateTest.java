package praktikum;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

import java.util.ArrayList;

public class OrderCreateTest {

    Order order;
    OrderClient orderClient;
    CustomerClient customerClient = new CustomerClient();
    CustomerGenerator customerGenerator = new CustomerGenerator();
    Customer customer = customerGenerator.getRandom();
    ValidatableResponse loginResponse = customerClient.create(customer);
    Order orderList;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @DisplayName("Создание заказа c валидными данными")
    @Description("Тест, проверяющий возможность создания заказа с авторизацией и валидными данными")
    @Test
    public void CreateOrderWithLogin() {

        ValidatableResponse authorizationResponse = customerClient.login(new CustomerCredentials(customer.getEmail(), customer.getPassword()));
        String accessToken = authorizationResponse.extract().path("accessToken").toString().substring(7);
        System.out.println(accessToken);
        ArrayList<String> ingredients = new ArrayList<>();
        order = new Order();
        ValidatableResponse getIngredientsResponse = orderClient.check(order);
        ingredients.add(getIngredientsResponse.extract().path("data[0]._id").toString());
        ingredients.add(getIngredientsResponse.extract().path("data[1]._id").toString());
        System.out.println(ingredients);
        orderList = new Order(ingredients);
        ValidatableResponse createOrder = orderClient.createAuthorized(orderList, accessToken);
        String responseMessage = createOrder.extract().path("name");
        Boolean success = createOrder.extract().path("success");
        assertThat("Can't create an oder", success, equalTo(true));
        System.out.println(responseMessage);
        assertThat("Response is incorrect", responseMessage, notNullValue());

    }

    @DisplayName("Создание заказа с неправильным хэшем ингредиентов")
    @Description("Тест, проверяющий возможность создания заказа с авторизацией и невалидными данными")
    @Test
    public void CreateOrderWithLoginAndInvalidHash() {

        ValidatableResponse authorizationResponse = customerClient.login(new CustomerCredentials(customer.getEmail(), customer.getPassword()));
        String accessToken = authorizationResponse.extract().path("accessToken").toString().substring(7);
        System.out.println(accessToken);
        ArrayList<String> ingredients = new ArrayList<>();
        order = new Order();
        ValidatableResponse getIngredientsResponse = orderClient.check(order);
        ingredients.add(getIngredientsResponse.extract().path("data[0]._id").toString());
        ingredients.add("611115a71d1f82001111116d");
        System.out.println(ingredients);
        orderList = new Order(ingredients);
        ValidatableResponse createOrder = orderClient.createAuthorized(orderList, accessToken);
        Boolean success = createOrder.extract().path("success");
        assertThat("Can't create an oder", success, equalTo(false));

    }

    @DisplayName("Создание заказа без авторизации")
    @Description("Тест, проверяющий возможность создания заказа без авторизации")
    @Test
    public void CreateOrderWithoutLogin() {

        ArrayList<String> ingredients = new ArrayList<>();
        order = new Order();
        ValidatableResponse getIngredientsResponse = orderClient.check(order);
        ingredients.add(getIngredientsResponse.extract().path("data[0]._id").toString());
        ingredients.add(getIngredientsResponse.extract().path("data[1]._id").toString());
        System.out.println(ingredients);
        orderList = new Order(ingredients);
        ValidatableResponse createOrderUnauthorized = orderClient.createUnauthorized(orderList);
        String responseMessage = createOrderUnauthorized.extract().path("message");
        Boolean success = createOrderUnauthorized.extract().path("success");
        assertThat("Can't create an oder", success, equalTo(false));

    }

    @DisplayName("Создание пустого заказа")
    @Description("Тест, проверяющий возможность создания заказа без ингредиентов")
    @Test
    public void CreateOrderWithoutIngredients() {

        ArrayList<String> ingredientsEmptyList = new ArrayList<>();
        CustomerCredentials customerCredentials = new CustomerCredentials(customer.getEmail(), customer.getPassword());
        ValidatableResponse authorizationResponse = customerClient.login(customerCredentials);
        String accessToken = authorizationResponse.extract().path("accessToken").toString().substring(7);
        System.out.println(accessToken);
        order = new Order();
        orderList = new Order(ingredientsEmptyList);
        ValidatableResponse createOrder = orderClient.createAuthorized(orderList, accessToken);
        String responseMessage = createOrder.extract().path("message");
        Boolean success = createOrder.extract().path("success");
        assertThat("Can't create an oder", success, equalTo(false));
        System.out.println(responseMessage);
        assertThat("Response is incorrect", responseMessage, equalTo("Ingredient ids must be provided"));

    }

    @After
    public void tearDown() {

    }

}

