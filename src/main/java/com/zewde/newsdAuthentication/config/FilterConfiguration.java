package com.zewde.newsdAuthentication.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

//@Configuration
public class FilterConfiguration {

//  @Bean
  public FilterRegistrationBean<CustomAuthenticationFilter> authenticationFilter(){
    FilterRegistrationBean<CustomAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new CustomAuthenticationFilter());
    registrationBean.addUrlPatterns("/articles");
    return registrationBean;
  }

//
//  @Bean
  public FilterRegistrationBean<LoggingFilter> loggingFilter(){
    FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new LoggingFilter());
    registrationBean.addUrlPatterns("/*");
    return registrationBean;
  }
}
