package com.community.global.validation.validator;

import com.community.global.validation.annotation.NoWhiteSpace;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoWhiteSpaceValidator implements ConstraintValidator<NoWhiteSpace, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true; // @NotBlank로 null/empty는 별도로 처리
        return value.chars().noneMatch(Character::isWhitespace);
    }
}


