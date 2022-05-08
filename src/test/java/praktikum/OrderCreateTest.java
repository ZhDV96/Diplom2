package praktikum;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;
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
    String accessToken = loginResponse.extract().path("accessToken").toString().substring(7);

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        System.out.println(accessToken);
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @DisplayName("Получение ингредиентов")
    @Description("Тест, проверяющий возможность получения списка заказов")
    @Test
    public void CreateOrderWithLogin() {

        ArrayList<String> ingredients = new ArrayList<>();
        order = new Order();
        ValidatableResponse getIngredientsResponse = orderClient.check(order);
        ingredients.add(getIngredientsResponse.extract().path("data[0]._id").toString());
        ingredients.add(getIngredientsResponse.extract().path("data[1]._id").toString());
        ingredients.add(getIngredientsResponse.extract().path("data[2]._id").toString());
        System.out.println(ingredients);
        Response orderResponse =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(ingredients)
                        .when()
                        .auth().oauth2(accessToken)
                        .post("api/orders/");
        orderResponse.then()
                .body("success", equalTo(true))
                .body("order.number", notNullValue());

    }

    @DisplayName("Получение ингредиентов")
    @Description("Тест, проверяющий возможность получения списка заказов")
    @Test
    public void CreateOrderWithoutLogin() {

        ArrayList<String> ingredientsId = new ArrayList<>();
        order = new Order();
        ValidatableResponse getIngredientsResponse = orderClient.check(order);
        ingredientsId.add(getIngredientsResponse.extract().path("data[0]._id").toString());
        ingredientsId.add(getIngredientsResponse.extract().path("data[1]._id").toString());
        ingredientsId.add(getIngredientsResponse.extract().path("data[2]._id").toString());
        System.out.println(ingredientsId);
        ValidatableResponse orderResponse = orderClient.create(ingredientsId);
        Boolean responseMessageStatus = orderResponse.extract().path("false");
        String responseMessageName = orderResponse.extract().path("name");
        assertThat("Response is incorrect", responseMessageStatus, is("true"));
        assertThat("Name is incorrect", responseMessageName, notNullValue());

    }

    @DisplayName("Получение ингредиентов")
    @Description("Тест, проверяющий возможность получения списка заказов")
    @Test
    public void CreateOrderWithoutIngredients() {

        ArrayList<String> ingredientsId = new ArrayList<>();
        System.out.println(ingredientsId);
        ValidatableResponse orderResponse = orderClient.create(ingredientsId);
        Boolean responseMessageStatus = orderResponse.extract().path("false");
        String responseMessageName = orderResponse.extract().path("name");

    }

    @After
    public void tearDown() {
        customerClient.delete(accessToken);
    }

}

