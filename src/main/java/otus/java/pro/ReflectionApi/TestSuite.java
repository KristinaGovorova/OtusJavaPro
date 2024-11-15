package otus.java.pro.ReflectionApi;

import otus.java.pro.ReflectionApi.annotations.AfterSuite;
import otus.java.pro.ReflectionApi.annotations.BeforeSuite;
import otus.java.pro.ReflectionApi.annotations.Disabled;
import otus.java.pro.ReflectionApi.annotations.Test;

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
