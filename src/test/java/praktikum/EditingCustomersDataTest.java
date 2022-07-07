package praktikum;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
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
    String newEmail = RandomStringUtils.randomAlphabetic(10, 15) + "@yandex.ru";
    String newPassword = RandomStringUtils.randomAlphabetic(4);
    String newName = RandomStringUtils.randomAlphabetic(3, 12);
    Customer customerNewData = new Customer(newEmail, newPassword, newName);
    ValidatableResponse createResponse = customerClient.create(customer);
    String accessToken = createResponse.extract().path("accessToken").toString().substring(7);

    @DisplayName("Изменения данных пользователя с валидными данными")
    @Description("Тест, проверяющий корректную работу api/user/ при успешном сценарии")
    @Test
    public void updateCustomerInfoAuthorized() {

        ValidatableResponse changeCustomerInfoResponse = customerClient.changeProfileInfo(customerNewData, accessToken);
        Boolean success = changeCustomerInfoResponse.extract().path("success");
        String changedEmail = changeCustomerInfoResponse.extract().path("user.email");
        assertThat("Can't create a customer", success, equalTo(true));
        assertThat("Response is incorrect", changedEmail, equalTo(newEmail.toLowerCase()));

    }

    @DisplayName("Изменения данных пользователя без авторизации")
    @Description("Тест, проверяющий корректную работу api/user/ при неуспешном сценарии")
    @Test
    public void updateCustomerInfoUnauthorized() {

        ValidatableResponse changeCustomerInfoResponse = customerClient.changeProfileInfoUnauthorized(customerNewData);
        Boolean success = changeCustomerInfoResponse.extract().path("success");
        String changedEmail = changeCustomerInfoResponse.extract().path("message");
        assertThat("Can't create a customer", success, equalTo(false));
        assertThat("Response is incorrect", changedEmail, equalTo("You should be authorised"));

    }

}
