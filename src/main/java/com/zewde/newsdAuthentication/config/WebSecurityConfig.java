package com.zewde.newsdAuthentication.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(HttpSecurity http) throws Exception{
    http.csrf().disable().httpBasic().and().authorizeRequests().antMatchers("/home").permitAll()
    .antMatchers("/register").permitAll();
  }
}
