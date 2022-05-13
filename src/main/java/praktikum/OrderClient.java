package praktikum;
import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Step;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderClient extends StellarClient {

    private static final String ORDER_PATH = "api/";

    @Step("Создание заказа")
    public ValidatableResponse create(List<String> ingredientsId) {
        return given()
                .spec(getBaseSpec())
                .body(ingredientsId)
                .when()
                .post(ORDER_PATH + "orders")
                .then();
    }

    @Step("Создание заказа")
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
