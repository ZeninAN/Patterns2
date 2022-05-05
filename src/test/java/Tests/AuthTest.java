package Tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static data.Generator.*;

public class AuthTest {

    @BeforeEach
    void setUp() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }


    @Test
    void happyPath() {
        var validUser = getActiveRegisteredUser();
        $("[data-test-id=login] input").setValue(validUser.getLogin());
        $("[data-test-id=password] input").setValue(validUser.getPassword());
        $("[data-test-id='action-login']").click();
        $$("h2").findBy(text("Личный кабинет")).shouldBe(visible);
    }

    @Test
    void shouldNotSussesLoginWithWrongNameUser() {
        var wrongNameUser = getWrongNameUser();
        $("[data-test-id=login] input").setValue(wrongNameUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongNameUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(text("Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    void shouldNotSussesLoginWithWrongPasswordUser() {
        var wrongPasswordUser = getWrongPasswordUser();
        $("[data-test-id=login] input").setValue(wrongPasswordUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPasswordUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id=error-notification] .notification__content").shouldHave(text("Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    void badPath() {
        var notValidUser = getNotRegisteredUser();
        $("[data-test-id=login] input").setValue(notValidUser.getLogin());
        $("[data-test-id=password] input").setValue(notValidUser.getPassword());
        $("[data-test-id='action-login']").click();
        $(withText("Пользователь заблокирован")).shouldBe(Condition.visible);
    }
}
