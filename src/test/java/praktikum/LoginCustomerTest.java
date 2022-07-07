package praktikum;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.apache.commons.lang3.RandomStringUtils;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

public class LoginCustomerTest {

    CustomerClient customerClient = new CustomerClient();
    CustomerGenerator customerGenerator = new CustomerGenerator();
    Customer customer = customerGenerator.getRandom();
    ValidatableResponse createResponse = customerClient.create(customer);
    String accessToken = createResponse.extract().path("accessToken").toString().substring(7);
    String randomEmail = RandomStringUtils.randomAlphabetic(10, 15) + "@yandex.ru";
    String randomPassword = RandomStringUtils.randomAlphabetic(4);

    @Before
    public void setUp() {

    }

    @DisplayName("Использование пользователем учетной записи с валидными данными")
    @Description("Тест, проверяющий возможность логина пользователя с валидными данными")
    @Test
    public void loginCourierWithValidateData() {

        ValidatableResponse loginResponse = customerClient.login(new CustomerCredentials(customer.getEmail(), customer.getPassword()));
        Boolean success = loginResponse.extract().path("success");
        assertThat("Can't create a customer", success, equalTo(true));
        String loginAccessToken = loginResponse.extract().path("accessToken").toString().substring(7);
        assertThat("Can't login customer", loginAccessToken, notNullValue());

    }

    @DisplayName("Использование пользователем учетной записи без логина")
    @Description("Тест, проверяющий возможность логина пользователя при отсутствии части данных")
    @Test
    public void loginCourierWithoutLogin() {

        ValidatableResponse loginResponse = customerClient.login(new CustomerCredentials(null, customer.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        assertThat("Courier can't login", statusCode, equalTo(401));
        String responseMessage = loginResponse.extract().path("message");
        System.out.println(responseMessage);
        assertThat("Response is incorrect", responseMessage, equalTo("email or password are incorrect"));

    }

    @DisplayName("Использование пользователем учетной записи с несуществующими данными")
    @Description("Тест, проверяющий возможность логина пользователя при использовании несуществующих данных")
    @Test
    public void loginCourierWithNonExistentData() {

        ValidatableResponse loginResponse = customerClient.login(new CustomerCredentials(randomEmail, randomPassword));
        int statusCode = loginResponse.extract().statusCode();
        assertThat("Courier can't login", statusCode, equalTo(401));
        String responseMessage = loginResponse.extract().path("message");
        System.out.println(responseMessage);
        assertThat("Response is incorrect", responseMessage, equalTo("email or password are incorrect"));


    }

    @DisplayName("Использование пользователем учетной записи без пароля")
    @Description("Тест, проверяющий возможность логина пользователя при отсутствии части данных")
    @Test
    public void loginCourierWithoutPassword() {

        ValidatableResponse loginResponse = customerClient.login(new CustomerCredentials(customer.getEmail(), null));
        int statusCode = loginResponse.extract().statusCode();
        assertThat("Courier can't login", statusCode, equalTo(401));
        String responseMessage = loginResponse.extract().path("message");
        System.out.println(responseMessage);
        assertThat("Response is incorrect", responseMessage, equalTo("email or password are incorrect"));


    }


    @After
    public void tearDown() {
        customerClient.delete(accessToken);
    }

}
