package praktikum;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class CustomerGenerator extends StellarClient {

    @Step("Создание набора случайных данных для создания курьера")
    public Customer getRandom() {
        String email = RandomStringUtils.randomAlphabetic(10, 15) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(4);
        String name = RandomStringUtils.randomAlphabetic(3, 12);

        return new Customer(email, password, name);
    }

    @Step("Создание набора случайных данных для создания курьера")
    public Customer getRandomWithoutEmail() {
        String email = RandomStringUtils.randomAlphabetic(10, 15) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(4);
        String name = RandomStringUtils.randomAlphabetic(3, 12);

        return new Customer(null, password, name);
    }

    @Step("Создание набора случайных данных для создания курьера")
    public Customer getRandomWithoutPassword() {
        String email = RandomStringUtils.randomAlphabetic(10, 15) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(4);
        String name = RandomStringUtils.randomAlphabetic(3, 12);

        return new Customer(email, null, name);
    }

    @Step("Создание набора случайных данных для создания курьера")
    public Customer getRandomWithoutName() {
        String email = RandomStringUtils.randomAlphabetic(10, 15) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(4);
        String name = RandomStringUtils.randomAlphabetic(3, 12);

        return new Customer(email, password, null);
    }

}
