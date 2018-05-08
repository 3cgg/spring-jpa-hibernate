package me.libme.module.spring.jpahibernate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by J on 2018/5/8.
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Delete {


    String yes() default "Y";

    String no() default "N";

}
