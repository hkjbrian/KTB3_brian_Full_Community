package com.community.global.validation.validator;

import com.community.global.validation.annotation.AtLeastOneField;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.web.multipart.MultipartFile;

public class AtLeastOneFieldValidator implements ConstraintValidator<AtLeastOneField, Object> {

    private String[] fields;

    @Override
    public void initialize(AtLeastOneField constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);

        for (String field : fields) {
            Object fieldValue = beanWrapper.getPropertyValue(field);
            if (hasValue(fieldValue)) {
                return true;
            }
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addConstraintViolation();
        return false;
    }

    private boolean hasValue(Object fieldValue) {
        if (fieldValue == null) {
            return false;
        }
        if (fieldValue instanceof String str) {
            return !str.isBlank();
        }
        if (fieldValue instanceof MultipartFile file) {
            return !file.isEmpty();
        }
        return true;
    }
}
