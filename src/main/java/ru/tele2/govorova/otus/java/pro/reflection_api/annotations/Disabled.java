package ru.tele2.govorova.otus.java.pro.reflection_api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Disabled {
    String reason() default "No reason provided";
}

