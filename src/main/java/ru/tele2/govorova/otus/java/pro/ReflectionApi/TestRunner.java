package ru.tele2.govorova.otus.java.pro.ReflectionApi;

import ru.tele2.govorova.otus.java.pro.ReflectionApi.annotations.AfterSuite;
import ru.tele2.govorova.otus.java.pro.ReflectionApi.annotations.BeforeSuite;
import ru.tele2.govorova.otus.java.pro.ReflectionApi.annotations.Disabled;
import ru.tele2.govorova.otus.java.pro.ReflectionApi.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TestRunner {
    private static final Logger logger = LogManager.getLogger(TestRunner.class.getName());

    public static void runTests(Class<?> cl) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {

        Method beforeSuiteMethod = null;
        Method afterSuiteMethod = null;
        ArrayList<Method> testMethods = new ArrayList<>();

        int passedTests = 0;
        int failedTests = 0;
        int disabledTests = 0;

        Class testSuite = TestSuite.class;

        for (Method method : testSuite.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeSuiteMethod == null) {
                    beforeSuiteMethod = method;
                } else {
                    throw new RuntimeException("В классе может быть только один метод с аннотацией @BeforeSuite");
                }
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuiteMethod == null) {
                    afterSuiteMethod = method;
                } else {
                    throw new RuntimeException("В классе может быть только один метод с аннотацией @AfterSuite");
                }
            } else if (method.isAnnotationPresent(Test.class)) {
                if (method.isAnnotationPresent(Disabled.class)) {
                    Disabled disabled = method.getAnnotation(Disabled.class);
                    logger.info("Метод {} отключен: {}", method.getName(), disabled.reason());
                    disabledTests++;
                } else {
                    testMethods.add(method);
                }
            }
        }

        Collections.sort(testMethods, Comparator.comparingInt(m -> m.getAnnotation(Test.class).priority()));

        Object testSuiteInstance = testSuite.getDeclaredConstructor().newInstance();

        try {
            if (beforeSuiteMethod != null) {
                beforeSuiteMethod.invoke(testSuiteInstance);
            }

            for (Method testMethod : testMethods) {
                try {
                    testMethod.invoke(testSuiteInstance);
                    passedTests++;
                } catch (Exception e) {
                    logger.error("Тест {} провален: {}", testMethod.getName(), e.getCause());
                    failedTests++;
                }
            }

            if (afterSuiteMethod != null) {
                afterSuiteMethod.invoke(testSuiteInstance);
            }
        } catch (Exception e) {
            logger.error("Ошибка при выполнении тестов", e);
        }

        // Вывод результатов
        System.out.println("Всего тестов: " + (passedTests + failedTests + disabledTests));
        System.out.println("Успешно пройдено: " + passedTests);
        System.out.println("Провалено: " + failedTests);
        System.out.println("Пропущено: " + disabledTests);
        logger.info("Всего тестов: {}", (passedTests + failedTests + disabledTests));
        logger.info("Успешно пройдено тестов: {}", passedTests);
        logger.info("Провалено тестов: {}", failedTests);
        logger.info("Пропущено тестов: {}", disabledTests);
    }
}