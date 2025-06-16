package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.MainPage;

import static com.codeborne.selenide.Selenide.open;



public class DebitCardTest {

    String approvedCardNumber = DataHelper.getApprovedCard().getCardNumber();
    String declinedCardNumber = DataHelper.getDeclinedCard().getCardNumber();
    String validMonth = DataHelper.getRandomMonth(1);
    String validYear = DataHelper.getRandomYear(1);
    String validOwnerName = DataHelper.getRandomName();
    String validCode = DataHelper.getNumberCVC(3);

    @BeforeAll
    static void setUpAllureListener() {
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide().screenshots(true).savePageSource(true));
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
    }

    @AfterEach
    public void shouldCleanBase() {
        SQLHelper.cleanBase();
    }

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    public void shouldCardPaymentApproved() {
        MainPage page = new MainPage();
        page.mainPage();
        var DebitPage = page.cardPayment();
        DebitPage.cleanFields();
        DebitPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, validCode);
        DebitPage.bankApprovedOperation();
        Assertions.assertEquals("APPROVED", SQLHelper.getCardPayment());
    }

    @Test
    public void shouldDeclinedCardPayment() {
        MainPage page = new MainPage();
        page.mainPage();
        var debitCardPage = page.cardPayment();
        debitCardPage.cleanFields();
        debitCardPage.fillCardPaymentForm(declinedCardNumber, validMonth, validYear, validOwnerName, validCode);
        debitCardPage.bankDeclinedOperation();
        Assertions.assertEquals("DECLINED", SQLHelper.getCardPayment());
    }


    @Test
    public void shouldHandleEmptyFields() {
        MainPage page = new MainPage();
        page.mainPage();
        var debitCardPage = page.cardPayment();
        var emptyField = DataHelper.getEmptyField();
        debitCardPage.cleanFields();
        debitCardPage.fillCardPaymentForm(emptyField, emptyField, emptyField, emptyField, emptyField);
        debitCardPage.errorFormat();
    }
}