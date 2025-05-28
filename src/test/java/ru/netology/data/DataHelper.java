package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Data;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private static Faker faker = new Faker(new Locale("en"));
    private static Faker fakerRus = new Faker(new Locale("ru"));

    private DataHelper() {
    }

    public static CardInfo getApprovedCard() {
        return new CardInfo("4444 4444 4444 4441", "APPROVED");

    }

    public static CardInfo getDeclinedCard() {

        return new CardInfo("4444 4444 4444 4442", "DECLINED");
    }

    public static String getRandomCardNumber() {
        return faker.business().creditCardNumber();

    }

    public static String getShortNumber() {
        int shortNumber = faker.random().nextInt(16);
        return faker.number().digits(shortNumber);
    }

    public static String getRandomMonth(int month) {
        return LocalDate.now().plusMonths(month).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getRandomYear(int year) {
        return LocalDate.now().plusYears(year).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getInvalidMonth() {
        return "00";
    }

    public static String getRandomName() {
        return faker.name().fullName();
    }

    public static String getRandomNameRus() {
        return fakerRus.name().fullName();
    }

    public static String getNumberName() {
        return faker.number().digit();
    }

    public static String getNumberCVC(int code) {
        return faker.number().digits(code);
    }

    public static String getSpecialCharactersName() {
        return "&^^%&^%*&^";
    }

    public static String getEmptyField() {
        return "";
    }

    @Value
    public static class CardInfo {
        public String cardNumber;
        public String Status;

    }
}