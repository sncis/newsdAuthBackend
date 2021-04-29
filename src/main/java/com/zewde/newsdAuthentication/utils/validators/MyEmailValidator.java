package com.zewde.newsdAuthentication.utils.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyEmailValidator implements ConstraintValidator<ValidEmail, String> {

  private static final String VALID_EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
  private static final Pattern PATTERN = Pattern.compile(VALID_EMAIL_PATTERN);

  @Override
  public void initialize(ValidEmail constraintAnnotation) {
  }

  @Override
  public boolean isValid(final String email, final ConstraintValidatorContext context) {
    return isValidEmail(email);
  }

  private boolean isValidEmail(String email){
    Matcher matcher = PATTERN.matcher(email);
    return matcher.matches();
  }

}
