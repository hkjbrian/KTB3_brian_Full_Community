package com.community.global.validation.annotation;

import com.community.global.validation.validator.AtLeastOneFieldValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = AtLeastOneFieldValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface AtLeastOneField {

    String message() default "최소 한 개 이상의 값을 입력해야 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Property names to inspect for non-empty values.
     */
    String[] fields();
}
