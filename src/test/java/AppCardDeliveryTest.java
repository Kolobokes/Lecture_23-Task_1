import com.codeborne.selenide.logevents.SelenideLogger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

import io.qameta.allure.selenide.AllureSelenide;

public class AppCardDeliveryTest {

    public String gatDate(int shift) {
        LocalDate date = LocalDate.now();
        LocalDate plusDate = date.plusDays(shift);

        String dateString = plusDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return dateString;
    }

    @BeforeAll
    public static void beforeTest() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
    //    SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @Test
    void correctTest(){

        String correctDate = gatDate(4);

        open("http://localhost:9999");
        SelenideElement form = $("#root");
        form.$("[data-test-id='city'] .input__control").setValue("Москва");
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(correctDate);
        form.$("[data-test-id='name'] .input__control").setValue("Иванов Иван Иванович");
        form.$("[data-test-id='phone'] .input__control").setValue("+79054040005");
        form.$(".checkbox__box").click();
        form.$(withText("Забронировать")).click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно забронирована на " + correctDate));
    }

    @Test
    void incorrectCityTest(){

        String correctDate = gatDate(4);

        open("http://localhost:9999");
        SelenideElement form = $("#root");
        form.$("[data-test-id='city'] .input__control").setValue("Горячий ключ");
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(correctDate);
        form.$("[data-test-id='name'] .input__control").setValue("Иванов Иван Иванович");
        form.$("[data-test-id='phone'] .input__control").setValue("+79054040005");
        form.$(".checkbox__box").click();
        form.$(withText("Забронировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void incorrectDateTest(){

        String incorrectDate = gatDate(0);

        open("http://localhost:9999");
        SelenideElement form = $("#root");
        form.$("[data-test-id='city'] .input__control").setValue("Москва");
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(incorrectDate);
        form.$("[data-test-id='name'] .input__control").setValue("Иванов Иван Иванович");
        form.$("[data-test-id='phone'] .input__control").setValue("+79054040005");
        form.$(".checkbox__box").click();
        form.$(withText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub")
                .shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void incorrectNameTest(){

        String correctDate = gatDate(4);

        open("http://localhost:9999");
        SelenideElement form = $("#root");
        form.$("[data-test-id='city'] .input__control").setValue("Москва");
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(correctDate);
        form.$("[data-test-id='name'] .input__control").setValue("Ivanov Ivan Ivanovich 4");
        form.$("[data-test-id='phone'] .input__control").setValue("+79054040005");
        form.$(".checkbox__box").click();
        form.$(withText("Забронировать")).click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void incorrectPhoneTest(){

        String correctDate = gatDate(4);

        open("http://localhost:9999");
        SelenideElement form = $("#root");
        form.$("[data-test-id='city'] .input__control").setValue("Москва");
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(correctDate);
        form.$("[data-test-id='name'] .input__control").setValue("Иванов Иван Иванович");
        form.$("[data-test-id='phone'] .input__control").setValue("89054040005");
        form.$(".checkbox__box").click();
        form.$(withText("Забронировать")).click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void NotAgreementTest(){

        String correctDate = gatDate(4);

        open("http://localhost:9999");
        SelenideElement form = $("#root");
        form.$("[data-test-id='city'] .input__control").setValue("Москва");
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        form.$("[data-test-id='date'] .input__control").sendKeys(Keys.DELETE);
        form.$("[data-test-id='date'] .input__control").setValue(correctDate);
        form.$("[data-test-id='name'] .input__control").setValue("Иванов Иван Иванович");
        form.$("[data-test-id='phone'] .input__control").setValue("+79054040005");
        form.$(withText("Забронировать")).click();
        $("[class='checkbox checkbox_size_m checkbox_theme_alfa-on-white input_invalid'").shouldBe(visible);
    }

    @AfterAll
    static void tearDownAll(){
        SelenideLogger.removeListener("AllureSelenide");
    }
}
