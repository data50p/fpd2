package com.femtioprocent.fpd2.config;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DeployGuard {
    boolean deployBooleanValue() default false;
    int deployIntValue() default 0;
    String deployStringValue() default "";
}
