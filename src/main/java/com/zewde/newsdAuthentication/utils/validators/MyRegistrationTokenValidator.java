package com.zewde.newsdAuthentication.utils.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyRegistrationTokenValidator implements ConstraintValidator<ValidRegistrationToken, String> {
  private static final String VALID_REGISTRATION_TOKEN_PATTERN = "^[a-z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}$";
  private static final Pattern PATTERN = Pattern.compile(VALID_REGISTRATION_TOKEN_PATTERN);

  @Override
  public void initialize(ValidRegistrationToken constraintAnnotation) {
  }

  @Override
  public boolean isValid(final String username, final ConstraintValidatorContext context) {
    return isValidPassword(username);
  }

  private boolean isValidPassword(String username){
    Matcher matcher = PATTERN.matcher(username);
    return matcher.matches();
  }

}
