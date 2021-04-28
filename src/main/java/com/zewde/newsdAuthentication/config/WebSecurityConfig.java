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
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
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
  LoggingFilter loggingFilter;

  @Autowired
  ArticleJsonFilter articleJsonFilter;

  @Autowired
  CsrfCookieFilter csrfCookieFilter;

  @Autowired
  CustomJwtExceptionHandlerForEntryPoint customJWTEntryPoint;



  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults("");
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
            .antMatchers("/articles/admin").hasAuthority("ADMIN")
            .antMatchers("/articles/*").hasAnyAuthority("USER","ADMIN")
            .antMatchers("/auth/*", "/", "/favicon", "/error", "/favicon.ico").permitAll()
            .and().authorizeRequests().anyRequest().authenticated().and()
            .addFilterBefore(loggingFilter, SecurityContextPersistenceFilter.class)
            .addFilterBefore(articleJsonFilter, SecurityContextPersistenceFilter.class)
            .addFilterBefore(authenticationFilter, BasicAuthenticationFilter.class)
            .addFilterAfter(csrfCookieFilter,BasicAuthenticationFilter.class)
            .exceptionHandling().authenticationEntryPoint(customJWTEntryPoint)
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
           .and().httpBasic().and()
        .headers()
        .contentTypeOptions()
        .and().xssProtection()
        .and().cacheControl()
        .and().httpStrictTransportSecurity()
        .and().frameOptions()
        .and().contentSecurityPolicy("default-src 'self' https://newsdme.herokuapp.com/ https://localhost:3000; script-src 'self'; img-src 'self'"); //only permit resoruces from the same origine
    //only allow secured requests
    //<---------- disable this following 3 lines if you don't want https locally -------------->
    http.requiresChannel()
        .requestMatchers(matcher -> matcher.getHeader("X-Forwarded-Proto") !=null)
        .requiresSecure();


  }

    // ntry point is for defining waht to send back when error occures


  @Bean
  public CorsConfigurationSource corsConfigurationSource(){
    final CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOrigins(Arrays.asList("https://localhost:3000" ,"https://newsdme.herokuapp.com"));
    config.setAllowCredentials(true);
    config.setAllowedMethods(Arrays.asList("HEAD",
        "GET", "POST", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(Arrays.asList(
        "Authorization",
        "Accept",
        "Cache-Control",
        "Content-Type",
        "Access-Control-Allow-Origin",
        "Access-Control-Allow-Headers",
        "Access-Control-Allow-Credentials",
        "Access-Control-Allow-Methods",
        "x-frame-options",
        "x-xsrf-token",
        "Strict-Transport-Security",
        "Content-Security-Policy",
        "X-Content-Type-Options",
        "X-XSS-Protection",
        "X-Forwarded-Proto"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
  }



}
