package praktikum;
import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;

public class OrderClient extends StellarClient {

    private static final String ORDER_PATH = "api/";

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createUnauthorized(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH + "orders")
                .then();
    }

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createAuthorized(Order order, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .auth().oauth2(accessToken)
                .post(ORDER_PATH + "orders")
                .then();
    }

    @Step("Получение списка ингредиентов")
    public ValidatableResponse check(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .get(ORDER_PATH + "ingredients")
                .then();
    }

}
