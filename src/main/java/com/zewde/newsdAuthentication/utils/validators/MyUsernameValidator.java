package com.zewde.newsdAuthentication.utils.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUsernameValidator implements ConstraintValidator<ValidUsername, String> {
  private static final String VALID_USERNAME_PATTERN = "^([A-Za-z0-9!&#@_\\-]){1,20}$";
  private static final Pattern PATTERN = Pattern.compile(VALID_USERNAME_PATTERN);

  @Override
  public void initialize(ValidUsername constraintAnnotation) {
  }

  @Override
  public boolean isValid(final String username, final ConstraintValidatorContext context) {
    return isValidUsername(username);
  }

  private boolean isValidUsername(String username){
    Matcher matcher = PATTERN.matcher(username);
    return matcher.matches();
  }

}
