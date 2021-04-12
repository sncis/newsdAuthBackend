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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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

        http.cors().and()
        .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
        .authorizeRequests()
        .antMatchers("/login","/register","/confirmUser","/logout").permitAll().and().authorizeRequests().anyRequest().authenticated().and()
        .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling().authenticationEntryPoint(customeJWTEntryPoint)
           .and().httpBasic()
        ;
    ;
    // ntry point is for defining waht to send back when error occures

  }
  @Bean
  public CorsConfigurationSource corsConfigurationSource(){
    final CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOrigins(Arrays.asList("http://localhost:3000" ,"http://192.168.2.105:3000"));
    config.setAllowCredentials(true);
    config.setAllowedMethods(Arrays.asList("HEAD",
        "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
    config.setAllowedHeaders(Arrays.asList("Authorization",
        "Cache-Control", "Content-Type",
        "Access-Control-Allow-Origin",
        "Access-Control-Allow-Headers",
        "Access-Control-Allow-Credentials",
        "Access-Control-Allow-Methods",
        "x-frame-options",
        "x-xsrf-token"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
  }


//  @Bean
//  public FilterRegistrationBean<CustomAuthenticationFilter> authFilter(){
//    FilterRegistrationBean<CustomAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
//    registrationBean.setFilter(authenticationFilter);
//    registrationBean.addUrlPatterns("/articles**");
//    return registrationBean;
//  }


}
