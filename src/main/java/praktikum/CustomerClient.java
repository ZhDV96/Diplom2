package praktikum;
import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;

public class CustomerClient  extends StellarClient {

    private static final String CUSTOMER_PATH = "api/auth/";

    @Step("Создание ользователя")
    public ValidatableResponse create(Customer customer) {
        return given()
                .spec(getBaseSpec())
                .body(customer)
                .when()
                .post(CUSTOMER_PATH + "register")
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse login(CustomerCredentials customerCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(customerCredentials)
                .when()
                .post(CUSTOMER_PATH + "login")
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .body(accessToken)
                .when()
                .delete(CUSTOMER_PATH + "user")
                .then();
    }

    @Step("Выход пользователя из системы")
    public ValidatableResponse logout(String refreshToken) {
        return given()
                .spec(getBaseSpec())
                .body(refreshToken)
                .when()
                .post(CUSTOMER_PATH + "logout")
                .then();
    }

    @Step("Выход пользователя из системы")
    public ValidatableResponse delete(Customer customer) {
        return given()
                .spec(getBaseSpec())
                .body(customer)
                .when()
                .delete(CUSTOMER_PATH + "user")
                .then();
    }

}
