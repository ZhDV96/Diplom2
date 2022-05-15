package praktikum;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import java.util.ArrayList;

public class GetCustomerOrderTest {

    Order order;
    OrderClient orderClient = new OrderClient();
    CustomerClient customerClient = new CustomerClient();
    CustomerGenerator customerGenerator = new CustomerGenerator();
    Customer customer = customerGenerator.getRandom();
    ValidatableResponse loginResponse = customerClient.create(customer);
    Order orderList;

    @DisplayName("Получение ингредиентов")
    @Description("Тест, проверяющий возможность получения списка заказов")
    @Test
    public void getOrderAuthorized() {

        CustomerCredentials customerCredentials = new CustomerCredentials(customer.getEmail(), customer.getPassword());
        ValidatableResponse authorizationResponse = customerClient.login(customerCredentials);
        String accessToken = authorizationResponse.extract().path("accessToken").toString().substring(7);
        System.out.println(accessToken);
        ArrayList<String> ingredients = new ArrayList<>();
        ArrayList<String> actualIngredients = new ArrayList<>();
        order = new Order();
        ValidatableResponse getIngredientsResponse = orderClient.check(order);
        ingredients.add(getIngredientsResponse.extract().path("data[0]._id").toString());
        ingredients.add(getIngredientsResponse.extract().path("data[1]._id").toString());
        System.out.println(ingredients);
        orderList = new Order(ingredients);
        ValidatableResponse createOrder = orderClient.createAuthorized(orderList, accessToken);
        ValidatableResponse getOrderList = customerClient.checkProfileInfoAuthorized(customerCredentials, accessToken);
        Boolean success = getOrderList.extract().path("success");
        assertThat("Can't check a customer`s info", success, equalTo(true));
        actualIngredients.add(getOrderList.extract().path("orders[0].ingredients[0]"));
        actualIngredients.add(getOrderList.extract().path("orders[0].ingredients[1]"));
        ArrayList<String> expectedIngredients = ingredients;
        System.out.println(actualIngredients);
        assertThat("Can't get a customer`s order", expectedIngredients.equals(actualIngredients), equalTo(true));

    }

    @DisplayName("Получение ингредиентов")
    @Description("Тест, проверяющий возможность получения списка заказов без авторизации")
    @Test
    public void getOrderUnauthorized() {

        CustomerCredentials customerCredentials = new CustomerCredentials(customer.getEmail(), customer.getPassword());
        ValidatableResponse authorizationResponse = customerClient.login(customerCredentials);
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
        ValidatableResponse getOrderList = customerClient.checkProfileInfoUnauthorized(customerCredentials);
        Boolean success = getOrderList.extract().path("success");
        String responseMessage = getOrderList.extract().path("message");
        assertThat("An error occurred", success, equalTo(false));
        assertThat("Response is incorrect", responseMessage, equalTo("You should be authorised"));


    }

}
