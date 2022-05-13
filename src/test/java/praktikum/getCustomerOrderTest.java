package praktikum;
import freemarker.template.utility.CollectionUtils;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import java.util.ArrayList;
import java.util.Arrays;

public class getCustomerOrderTest {

    Order order;
    OrderClient orderClient = new OrderClient();
    CustomerClient customerClient = new CustomerClient();
    CustomerGenerator customerGenerator = new CustomerGenerator();
    Customer customer = customerGenerator.getRandom();
    ValidatableResponse loginResponse = customerClient.create(customer);
    Order orderList;

    @Before
    public void setUp() {
    }

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
        ValidatableResponse getOrderList = customerClient.checkProfileInfo(customerCredentials, accessToken);
        Boolean success = getOrderList.extract().path("success");
        assertThat("Can't create a customer", success, equalTo(true));
        actualIngredients.add(getOrderList.extract().path("orders[0].ingredients[0]"));
        actualIngredients.add(getOrderList.extract().path("orders[0].ingredients[1]"));
        ArrayList<String> expectedIngredients = ingredients;
        System.out.println(actualIngredients);
        assertThat("Can't create a customer", expectedIngredients.equals(actualIngredients), equalTo(true));

    }

}
