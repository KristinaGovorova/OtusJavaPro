package ru.tele2.govorova.otus.java.pro.reflection_api;

import ru.tele2.govorova.otus.java.pro.reflection_api.annotations.AfterSuite;
import ru.tele2.govorova.otus.java.pro.reflection_api.annotations.BeforeSuite;
import ru.tele2.govorova.otus.java.pro.reflection_api.annotations.Disabled;
import ru.tele2.govorova.otus.java.pro.reflection_api.annotations.Test;

public class TestSuite {

    @BeforeSuite
    public void setUp() {
        System.out.println("Before Suite: Подготовка к тестам");
    }

//    @BeforeSuite
//    public void setUpFoo() {
//        System.out.println("Before Suite: Подготовка к тестам");
//    }

    @Test(priority = 1)
    public void testOne() {
        System.out.println("Тест 1 выполнен");
    }

    @Test(priority = 10)
    public void testTwo() {
        System.out.println("Тест 2 выполнен");
    }

    @Test(priority = 7)
    public void testThree() {
        System.out.println("Тест 3 выполнен");
    }

    @Test(priority = 3)
    @Disabled(reason = "Тест 4 отключен из-за известной проблемы")
    public void testFour() {
        System.out.println("Тест 4 выполнен");
    }

    @AfterSuite
    public void tearDown() {
        System.out.println("After Suite: Завершение тестов");
    }

//    @AfterSuite
//    public void tearDownFoo() {
//        System.out.println("After Suite: Завершение тестов");
//    }

}
