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
@Constraint(validatedBy = MyRegistrationTokenValidator.class)
@Documented
public @interface ValidRegistrationToken {
  String message() default "Invalid RegistrationToken";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
