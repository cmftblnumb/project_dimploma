package ru.netology.test;


import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.CreditPage;
import ru.netology.page.MainPage;

import static org.junit.jupiter.api.Assertions.*;

public class CreditCardTest {

    private String approvedCardNumber;
    private String declinedCardNumber;
    private String validMonth;
    private String validYear;
    private String validOwnerName;
    private String validCode;

    @BeforeAll
    static void setUpAllureListener() {
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide().screenshots(true).savePageSource(true));
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void init() {
        Selenide.open("http://localhost:8080");
        approvedCardNumber = DataHelper.getApprovedCard().getCardNumber();
        declinedCardNumber = DataHelper.getDeclinedCard().getCardNumber();
        validMonth = DataHelper.getRandomMonth(1);
        validYear = DataHelper.getRandomYear(1);
        validOwnerName = DataHelper.getRandomName();
        validCode = DataHelper.getNumberCVC(3);
    }

    @AfterEach
    void clean() {
        SQLHelper.cleanBase();
    }

    private CreditPage openCreditCardPage() {
        MainPage page = new MainPage();
        page.mainPage();
        CreditPage creditCardPage = page.creditPayment();
        creditCardPage.cleanFields();
        return creditCardPage;
    }

    // Проверка успешной оплаты в кредит с валидной картой
    @Test
    void shouldCreditPaymentApproved() {
        CreditPage creditCardPage = openCreditCardPage();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.bankApprovedOperation();
        assertEquals("APPROVED", SQLHelper.getCreditPayment());
    }

    // Проверка отказа в оплате при использовании отклонённой карты
    @Test
    void shouldDeclinedCardPayment() {
        CreditPage creditCardPage = openCreditCardPage();
        creditCardPage.fillCardPaymentForm(declinedCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.bankDeclinedOperation();
        assertEquals("Declined", SQLHelper.getCreditPayment());
    }

    // Проверки на некорректные номера карт
    @Test
    void shouldShowErrorForInvalidCardNumber() {
        CreditPage creditCardPage = openCreditCardPage();
        String invalidCardNumber = DataHelper.getShortNumber();
        creditCardPage.fillCardPaymentForm(invalidCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    // Проверка ошибки при отсутствии номера карты
    @Test
    void shouldShowErrorForEmptyCardNumber() {
        CreditPage creditCardPage = openCreditCardPage();
        String emptyCardNumber = DataHelper.getEmptyField();
        creditCardPage.fillCardPaymentForm(emptyCardNumber, validMonth, validYear, validOwnerName, validCode);
        creditCardPage.errorFormat();
    }

    // Проверки срока действия карты
    @Test
    void shouldShowErrorForExpiredMonth() {
        CreditPage creditCardPage = openCreditCardPage();
        String monthExpired = DataHelper.getRandomMonth(-2);
        creditCardPage.fillCardPaymentForm(approvedCardNumber, monthExpired, validYear, validOwnerName, validCode);
        creditCardPage.errorCardTermValidity();
    }

    // Проверка ошибки при вводе просроченного года
    @Test
    void shouldShowErrorForExpiredYear() {
        CreditPage creditCardPage = openCreditCardPage();
        String expiredYear = DataHelper.getRandomYear(-5);
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, expiredYear, validOwnerName, validCode);
        creditCardPage.termValidityExpired();
    }

    // Проверки поля владельца карты
    @Test
    void shouldShowErrorForRussianNameInOwner() {
        CreditPage creditCardPage = openCreditCardPage();
        String rusLanguageName = DataHelper.getRandomNameRus();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, rusLanguageName, validCode);
        creditCardPage.errorFormat();
    }

    // Проверка ошибки при вводе цифр в поле "владелец"
    @Test
    void shouldShowErrorForDigitsInOwnerName() {
        CreditPage creditCardPage = openCreditCardPage();
        String digitsName = DataHelper.getNumberName();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, digitsName, validCode);
        creditCardPage.errorFormat();
    }

    // Проверка ошибки при вводе спецсимволов в поле "владелец"
    @Test
    void shouldShowErrorForSpecialSymbolsInOwnerName() {
        CreditPage creditCardPage = openCreditCardPage();
        String specSymbolsName = DataHelper.getSpecialCharactersName();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, specSymbolsName, validCode);
        creditCardPage.errorFormat();
    }

    // Проверка ошибки при пустом поле "владелец"
    @Test
    void shouldShowErrorForEmptyOwnerName() {
        CreditPage creditCardPage = openCreditCardPage();
        String emptyName = DataHelper.getEmptyField();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, emptyName, validCode);
        creditCardPage.emptyField();
    }

    // Проверки CVC
    @Test
    void shouldShowErrorForTwoDigitCVC() {
        CreditPage creditCardPage = openCreditCardPage();
        String twoDigitCVC = DataHelper.getNumberCVC(2);
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, twoDigitCVC);
        creditCardPage.errorFormat();
    }

    // Проверка ошибки при вводе 1-значного CVC
    @Test
    void shouldShowErrorForOneDigitCVC() {
        CreditPage creditCardPage = openCreditCardPage();
        String oneDigitCVC = DataHelper.getNumberCVC(1);
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, oneDigitCVC);
        creditCardPage.errorFormat();
    }

    // Проверка ошибки при отсутствии CVC
    @Test
    void shouldShowErrorForEmptyCVC() {
        CreditPage creditCardPage = openCreditCardPage();
        String emptyCVC = DataHelper.getEmptyField();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, emptyCVC);
        creditCardPage.errorFormat();
    }

    // Проверка ошибки при использовании спецсимволов в CVC
    @Test
    void shouldShowErrorForSpecialSymbolsInCVC() {
        CreditPage creditCardPage = openCreditCardPage();
        String specSymbolsCVC = DataHelper.getSpecialCharactersName();
        creditCardPage.fillCardPaymentForm(approvedCardNumber, validMonth, validYear, validOwnerName, specSymbolsCVC);
        creditCardPage.errorFormat();
    }

    // Проверка всех пустых полей
    @Test
    void shouldShowErrorForAllFieldsEmpty() {
        CreditPage creditCardPage = openCreditCardPage();
        String emptyField = DataHelper.getEmptyField();
        creditCardPage.fillCardPaymentForm(emptyField, emptyField, emptyField, emptyField, emptyField);
        creditCardPage.errorFormat();
    }
}