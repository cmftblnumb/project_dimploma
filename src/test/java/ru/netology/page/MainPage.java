package ru.netology.page;

import com.codeborne.selenide.SelenideElement;


import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {
    private SelenideElement heading = $(withText("Путешествие дня"));
    private SelenideElement debitButton = $(withText("Купить"));
    private SelenideElement creditButton = $(withText("Купить в кредит"));

    public void mainPage() {
        heading.shouldBe(visible);

    }
    public DebitPage cardPayment() {
        debitButton.click();
        return new DebitPage();
    }

    public CreditPage creditPayment() {
        creditButton.click();
        return new CreditPage();
    }
}


