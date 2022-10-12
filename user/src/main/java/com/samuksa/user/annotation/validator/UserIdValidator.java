package com.samuksa.user.annotation.validator;

import com.samuksa.user.annotation.anno.UserId;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class UserIdValidator implements ConstraintValidator<UserId, String> {

    private String regexp;

    private boolean nullable;

    @Override
    public void initialize(UserId userId) {
        this.regexp = userId.regexp();
        this.nullable = userId.nullable();
    }

    @Override
    public boolean isValid(String userId, ConstraintValidatorContext context) {
        if (userId == null && nullable)
            return true;
        if (!StringUtils.hasText(userId)) {
            addConstraintViolation(context, "not found Id");
            return false;
        }
        if(!Pattern.matches(regexp, userId)) {
            addConstraintViolation(context, "not valid Id");
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String msg){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}
