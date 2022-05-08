package praktikum;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

public class CreateCustomerTest {

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

    @DisplayName("Создание копии пользоватеоя с ранее использованными данными")
    @Description("Тест, проверяющий возможность создания в api/register/ второго пользователя с ранее использованными данными")
    @Test
    public void createCouriersCopy() {

        ValidatableResponse createCopyResponse = customerClient.create(customer);
        int CopyStatusCode = createCopyResponse.extract().statusCode();
        assertThat("Error: You can`t create a copy of courier", CopyStatusCode, equalTo(403));
        String responseMessage = createCopyResponse.extract().path("message");
        System.out.println(responseMessage);
        assertThat("Response is incorrect", responseMessage, equalTo("User already exists"));

    }

    @DisplayName("Создание пользователя без использования логина")
    @Description("Тест, проверяющий возможность создания в api/register пользователя без логина")
    @Test
    public void createCourierWithNoLogin() {

        customer = customerGenerator.getRandomWithoutEmail();
        ValidatableResponse createResponse = customerClient.create(customer);
        int statusCode = createResponse.extract().statusCode();
        assertThat("Error: You can`t create a courier without login", statusCode, equalTo(403));
        String responseMessage = createResponse.extract().path("message");
        System.out.println(responseMessage);
        assertThat("Response is incorrect", responseMessage, equalTo("Email, password and name are required fields"));

    }

    @DisplayName("Создание пользователя без использования пароля")
    @Description("Тест, проверяющий возможность создания в api/register пользователя без пароля")
    @Test
    public void createCourierWithNoPassword() {

        customer = customerGenerator.getRandomWithoutPassword();
        ValidatableResponse createResponse = customerClient.create(customer);
        int statusCode = createResponse.extract().statusCode();
        assertThat("Error: You can`t create a courier without login", statusCode, equalTo(403));
        String responseMessage = createResponse.extract().path("message");
        System.out.println(responseMessage);
        assertThat("Response is incorrect", responseMessage, equalTo("Email, password and name are required fields"));

    }

    @After
    public void tearDown() {
        customerClient.delete(accessToken);
    }

}
