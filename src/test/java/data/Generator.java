package data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class Generator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static Faker faker = new Faker(new Locale("en"));

    private Generator() {
    }

    static void setUpUser(UserInfo user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String getLogin() {
        return faker.name().username();
    }

    public static String getPassword() {
        return faker.internet().password();
    }

    public static UserInfo getActiveRegisteredUser() {
        var user = new UserInfo(getLogin(), getPassword(), "active");
        setUpUser(user);
        return user;
    }

    public static UserInfo getWrongNameUser() {
        var password = getPassword();
        var user = new UserInfo(getLogin(), password, "active");
        setUpUser(user);
        return new UserInfo(getLogin(), password, "active");
    }

    public static UserInfo getWrongPasswordUser() {
        var name = getLogin();
        var user = new UserInfo(name, getPassword(), "active");
        setUpUser(user);
        return new UserInfo(name, getPassword(), "active");
    }

    public static UserInfo getNotRegisteredUser() {
        var user = new UserInfo(getLogin(), getPassword(), "blocked");
        setUpUser(user);
        return user;
    }

    @Value
    public static class UserInfo {
        String login;
        String password;
        String status;
    }
}
