package com.zewde.newsdAuthentication.config;

import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  BCryptPasswordEncoder passwordEncoder;

  @Autowired
  UserDetailsServiceImplementation userDetailsServiceImplementation;

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception{
    auth.userDetailsService(userDetailsServiceImplementation).passwordEncoder(passwordEncoder);
  }

  @Override
  public void configure(HttpSecurity http) throws Exception{
    http.csrf().disable().httpBasic().and().authorizeRequests().antMatchers("/home").permitAll()
    .antMatchers("/register").permitAll();
  }
}
