package com.community.global.validation.validator;

import com.community.global.validation.annotation.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;

        boolean hasLower = value.chars().anyMatch(Character::isLowerCase);
        boolean hasUpper = value.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit = value.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = value.chars().anyMatch(ch ->
                !Character.isLetterOrDigit(ch)
        );

        if (hasLower && hasUpper && hasDigit && hasSpecial) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        if (!hasUpper)
            context.buildConstraintViolationWithTemplate("비밀번호에 대문자가 1자 이상 포함되어야 합니다.")
                    .addConstraintViolation();
        else if (!hasLower)
            context.buildConstraintViolationWithTemplate("비밀번호에 소문자가 1자 이상 포함되어야 합니다.")
                    .addConstraintViolation();
        else if (!hasDigit)
            context.buildConstraintViolationWithTemplate("비밀번호에 숫자가 1자 이상 포함되어야 합니다.")
                    .addConstraintViolation();
        else if (!hasSpecial)
            context.buildConstraintViolationWithTemplate("비밀번호에 특수문자가 1자 이상 포함되어야 합니다.")
                    .addConstraintViolation();

        return false;
    }
}

