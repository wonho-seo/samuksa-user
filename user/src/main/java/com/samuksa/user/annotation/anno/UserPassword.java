package com.samuksa.user.annotation.anno;

import com.samuksa.user.annotation.validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserPassword {

    String regexp() default "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}";

    boolean nullable() default false;

    String message() default  "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
