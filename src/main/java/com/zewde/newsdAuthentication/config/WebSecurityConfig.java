package com.zewde.newsdAuthentication.config;

import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import com.zewde.newsdAuthentication.utils.JWTTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  BCryptPasswordEncoder passwordEncoder;

  @Autowired
  UserDetailsServiceImplementation userDetailsServiceImplementation;

  @Autowired
  JWTTokenUtils jwtTokenUtils;

  @Autowired
  CustomAuthenticationFilter authenticationFilter;

  @Autowired
  CustomeJWTEntryPoint customeJWTEntryPoint;

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }


  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception{
    auth.userDetailsService(userDetailsServiceImplementation).passwordEncoder(passwordEncoder);
  }

  @Override
  public void configure(HttpSecurity http) throws Exception{
    http.csrf()
        .disable()
        .httpBasic()
        .and().authorizeRequests()
        .antMatchers("/home").permitAll()
        .antMatchers("/register").permitAll()
        .antMatchers("/login").permitAll()
        .anyRequest()
        .authenticated().and()
        .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(customeJWTEntryPoint);

  }
}
