package praktikum;
import java.util.ArrayList;

public class Customer {

    public String email;
    public String password;
    public String name;

    public Customer(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Customer() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
