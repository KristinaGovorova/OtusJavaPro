package ru.tele2.govorova.otus.java.pro.reflection_api;


import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {

        TestRunner.runTests(TestSuite.class);
    }
}
