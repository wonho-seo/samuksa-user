package com.samuksa.user.annotation.validator;

import com.samuksa.user.annotation.anno.UserNickName;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class UserNickNameValidator implements ConstraintValidator<UserNickName, String> {

    private String regexp;

    private boolean nullable;

    @Override
    public void initialize(UserNickName userNickName) {
        this.nullable = userNickName.nullable();
        this.regexp = userNickName.regexp();
    }

    @Override
    public boolean isValid(String nickName, ConstraintValidatorContext context) {
        if (nickName == null && nullable)
            return true;
        if (!StringUtils.hasText(nickName)) {
            addConstraintViolation(context, "not found nickName");
            return false;
        }
        if(!Pattern.matches(regexp, nickName)) {
            addConstraintViolation(context, "not valid nickName");
            return false;
        }

        return true;
    }
    private void addConstraintViolation(ConstraintValidatorContext context, String msg){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    }
}
