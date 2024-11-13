package otus.java.pro.ReflectionApi.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Disabled {
    String reason() default "No reason provided";
}

