package com.mattzm.gungale.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates that the following code refers or partially refers to the vanilla source code.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface VanillaCode {
    /**
     * If used on a class, this represents the reference class name.
     * If used on a method, this represents the reference method name.
     * If used on a constructor, this represents the reference class name.
     * @return the name of the reference object.
     */
    String value() default "";
}
