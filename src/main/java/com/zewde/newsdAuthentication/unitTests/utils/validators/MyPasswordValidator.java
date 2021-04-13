package com.zewde.newsdAuthentication.unitTests.utils.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyPasswordValidator implements ConstraintValidator<ValidPassword, String> {

  private static final String VALID_PASSWORD_PATTER ="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
  private static final Pattern PATTERN = Pattern.compile(VALID_PASSWORD_PATTER);

  @Override
  public void initialize(ValidPassword constraintAnnotation) {
  }

  @Override
  public boolean isValid(final String password, final ConstraintValidatorContext context) {
    return isValidPassword(password);
  }

  private boolean isValidPassword(String password){
    Matcher matcher = PATTERN.matcher(password);
    return matcher.matches();
  }



}
