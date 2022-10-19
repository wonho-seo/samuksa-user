package com.samuksa.user.annotation.anno;

import com.samuksa.user.annotation.validator.UserNickNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserNickNameValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserNickName {

    String regexp() default "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{3,12}$";

    boolean nullable() default false;

    String message() default  "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};}
