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

public class EditingCustomersDataTest {

    CustomerClient customerClient = new CustomerClient();
    CustomerGenerator customerGenerator = new CustomerGenerator();
    Customer customer = customerGenerator.getRandom();
    ValidatableResponse createResponse = customerClient.create(customer);
    String accessToken = createResponse.extract().path("accessToken").toString().substring(7);

    @DisplayName("Создание пользователя с валидными данными")
    @Description("Первый тест, проверяющий корректную работу api/register/")
    @Test
    public void createCourierWithValidatableData() {

        ValidatableResponse loginResponse = customerClient.login(new CustomerCredentials(customer.getEmail(), customer.getPassword()));
        Boolean success = createResponse.extract().path("success");
        assertThat("Can't create a customer", success, equalTo(true));
        String loginAccessToken = loginResponse.extract().path("accessToken").toString().substring(7);
        assertThat("Can't login customer", loginAccessToken, notNullValue());

    }

}
