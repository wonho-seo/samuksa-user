package com.samuksa.user.annotation.anno;

import com.samuksa.user.annotation.validator.UserIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserIdValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserId {

    String regexp() default "^[a-zA-Z0-9]{4,12}$";

    boolean nullable() default false;

    String message() default  "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
