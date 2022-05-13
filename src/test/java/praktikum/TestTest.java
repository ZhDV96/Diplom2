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

public class TestTest {

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

    @DisplayName("Получение ингредиентов")
    @Description("Тест, проверяющий возможность получения списка заказов")
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
        Boolean success = createOrder.extract().path("success");
        assertThat("Can't create a customer", success, equalTo(true));


    }

}
