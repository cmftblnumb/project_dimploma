package ru.netology.page;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreditPage {
    private final SelenideElement errorFormat = $$("[class=input__inner]").findBy(text("Номер карты"));
    private final SelenideElement emptyField = $$("[class=input__inner]").findBy(text("Владелец")).$(byText("Поле обязательно для заполнения"));
    private final SelenideElement errorCardTermValidity = $$("[class=input__inner]").findBy(text("Месяц"));
    private final SelenideElement termValidityExpired = $$("[class=input__inner]").findBy(text("Год")).$(byText("Истёк срок действия карты"));
    private final SelenideElement bankDeclinedOperation = $$(".notification__content").findBy(text("Ошибка! Банк отказал в проведении операции."));
    private final SelenideElement bankApprovedOperation = $$(".notification__content").findBy(text("Операция одобрена Банком."));
    public SelenideElement ownerField = $$(".input__inner").findBy(text("Владелец")).$(".input__control");

    public SelenideElement codeField = $("[placeholder='999']");
    public SelenideElement contButton = $$(".button__content").find(Condition.exactText("Продолжить"));
    private SelenideElement heading = $(withText("Кредит по данным карты"));
    private SelenideElement cardNumberField = $("[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthField = $("[placeholder='08']");
    private SelenideElement yearField = $("[placeholder='22']");

    public CreditPage() {

        heading.shouldBe(visible);
    }

    public void fillCardPaymentForm(String cardNumber, String month, String year, String owner, String code) {
        cardNumberField.setValue(cardNumber);
        monthField.setValue(month);
        yearField.setValue(year);
        ownerField.setValue(owner);
        codeField.setValue(code);
        contButton.click();
    }
    public void bankApprovedOperation() {
        bankApprovedOperation.shouldBe(visible, Duration.ofSeconds(20));
    }

    public void bankDeclinedOperation() {
        bankDeclinedOperation.shouldBe(visible, Duration.ofSeconds(20));
    }

    public void errorFormat() {
        errorFormat.shouldBe(visible);
    }

    public void emptyField() {
        emptyField.shouldBe(visible);
    }

    public void errorCardTermValidity() {

        errorCardTermValidity.shouldBe(visible);
    }

    public void termValidityExpired() {

        termValidityExpired.shouldBe(visible);
    }

    public void cleanFields() {
        cardNumberField.doubleClick().sendKeys(Keys.BACK_SPACE);
        monthField.doubleClick().sendKeys(Keys.BACK_SPACE);
        yearField.doubleClick().sendKeys(Keys.BACK_SPACE);
        ownerField.doubleClick().sendKeys(Keys.BACK_SPACE);
        codeField.doubleClick().sendKeys(Keys.BACK_SPACE);
    }
}