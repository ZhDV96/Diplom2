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

    Customer customer;
    CustomerClient customerClient;
    CustomerGenerator courierGenerator = new CustomerGenerator();
    int courierId;
    String randomEmail = RandomStringUtils.randomAlphabetic(10, 15) + "@yandex.ru";
    String randomPassword = RandomStringUtils.randomAlphabetic(4);

    @Before
    public void setUp() {
        customerClient = new CustomerClient();
        customer = courierGenerator.getRandom();
        customerClient.create(customer);
    }

    @DisplayName("Использование пользователем учетной записи с валидными данными")
    @Description("Тест, проверяющий возможность логина пользователя с валидными данными")
    @Test
    public void loginCourierWithValidateData() {

        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(), courier.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        assertThat("Courier can't login", statusCode, equalTo(200));
        courierId = loginResponse.extract().path("id");
        System.out.println(courierId);
        assertThat("Can't login courier", courierId, is(not(0)));

    }

    @DisplayName("Использование пользователем учетной записи без логина")
    @Description("Тест, проверяющий возможность логина пользователя при отсутствии части данных")
    @Test
    public void loginCourierWithoutLogin() {

        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(null, courier.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        assertThat("Courier can't login", statusCode, equalTo(400));
        String responseMessage = loginResponse.extract().path("message");
        System.out.println(responseMessage);
        assertThat("Response is incorrect", responseMessage, equalTo("Недостаточно данных для входа"));

    }

    @DisplayName("Использование пользователем учетной записи с несуществующими данными")
    @Description("Тест, проверяющий возможность логина пользователя при использовании несуществующих данных")
    @Test
    public void loginCourierWithNonExistentData() {

        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(randomLogin, randomPassword));
        int statusCode = loginResponse.extract().statusCode();
        assertThat("Courier can't login", statusCode, equalTo(404));
        String responseMessage = loginResponse.extract().path("message");
        System.out.println(responseMessage);
        assertThat("Response is incorrect", responseMessage, equalTo("Учетная запись не найдена"));


    }

    @DisplayName("Использование пользователем учетной записи без пароля")
    @Description("Тест, проверяющий возможность логина пользователя при отсутствии части данных")
    @Test
    public void loginCourierWithoutPassword() {

        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(), null));
        int statusCode = loginResponse.extract().statusCode();
        assertThat("Courier can't login", statusCode, equalTo(400));
        String responseMessage = loginResponse.extract().path("message");
        System.out.println(responseMessage);
        assertThat("Response is incorrect", responseMessage, equalTo("Недостаточно данных для входа"));


    }


    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }

}
