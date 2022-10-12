package com.samuksa.user.annotation.validator;

import com.samuksa.user.annotation.anno.UserPassword;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<UserPassword, String> {

    private String regexp;

    private boolean nullable;

    @Override
    public void initialize(UserPassword password) {
        this.nullable = password.nullable();
        this.regexp = password.regexp();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null && nullable)
            return true;
        if (!StringUtils.hasText(password)) {
            addConstraintViolation(context, "not found password");
            return false;
        }
        if(!Pattern.matches(regexp, password)) {
            addConstraintViolation(context, "not valid password");
            return false;
        }

            return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String msg){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}
