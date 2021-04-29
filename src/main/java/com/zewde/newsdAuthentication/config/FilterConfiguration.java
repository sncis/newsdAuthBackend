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
  public FilterRegistrationBean<ApplicationEntryPointLoggingFilter> loggingFilter(){
    FilterRegistrationBean<ApplicationEntryPointLoggingFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new ApplicationEntryPointLoggingFilter());
    registrationBean.addUrlPatterns("/*");
    return registrationBean;
  }
}
