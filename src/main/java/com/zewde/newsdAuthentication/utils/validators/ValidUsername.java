package com.zewde.newsdAuthentication.utils.validators;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = MyUsernameValidator.class)
@Documented
public @interface ValidUsername {

  String message() default "Invalid Username";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
