package com.zewde.newsdAuthentication.config;

import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import com.zewde.newsdAuthentication.utils.JWTTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.web.header.writers.StaticHeadersWriter;
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
  ApplicationEntryPointLoggingFilter applicationEntryPointLoggingFilter;

  @Autowired
  ArticleJsonFilter articleJsonFilter;


  @Autowired
  CustomJwtExceptionHandlerForEntryPoint customJWTEntryPoint;

  @Value("${frontend.url}")
  private String frontendUrl;

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

        http.cors()
            .and().csrf().disable()
//        .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
        .authorizeRequests()
            .antMatchers("/admin").hasAuthority("ADMIN")
            .antMatchers("/articles/*").hasAnyAuthority("USER","ADMIN")
            .antMatchers("/auth/*", "/", "/favicon", "/error", "/favicon.ico").permitAll()
            .and().authorizeRequests().anyRequest().authenticated().and()
            .addFilterBefore(applicationEntryPointLoggingFilter, SecurityContextPersistenceFilter.class)
            .addFilterBefore(articleJsonFilter, SecurityContextPersistenceFilter.class)
            .addFilterBefore(authenticationFilter, BasicAuthenticationFilter.class)
            .exceptionHandling().authenticationEntryPoint(customJWTEntryPoint)
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
           .and().httpBasic();
         http.headers()
            .contentSecurityPolicy("default-src 'self' " + frontendUrl+ "; connect-src 'self' https://newsdme.herokuapp.com/ " + frontendUrl+ " ; img-src 'self'; script-src 'self' " + frontendUrl+ "; style-src 'self';  manifest-src 'self' " + frontendUrl)
             .and().addHeaderWriter(new StaticHeadersWriter("Permissions-Policy", "camera=('none'), autoplay= ('none'),fullscreen=('self'),geolocation=('self'),gyroscope=('self'),magnetometer=('self'),microphone=('none'),payment=('none'),picture-in-picture=('none'),publickey-credentials-get=('none'),sync-xhr=('self'), usb=('none'),xr-spatial-tracking=('none')"))
             .addHeaderWriter(new StaticHeadersWriter( "Referrer-Policy", "strict-origin-when-cross-origin"));



//       .contentSecurityPolicy("default-src 'self' "+ frontendUrl) //only permit resoruces from the same origine
//        .and().frameOptions().sameOrigin().disable();
//        .headers()
//        .contentTypeOptions()
//        .and().xssProtection()
//        .and().cacheControl()
//        .and().httpStrictTransportSecurity().and().contentSecurityPolicy("default-src 'self' "+ frontendUrl) //only permit resoruces from the same origine
//        .and().frameOptions().sameOrigin().disable();
    //only allow secured requests
    //<---------- disable this following 3 lines if you don't want https locally -------------->
    http.requiresChannel()
        .requestMatchers(matcher -> matcher.getHeader("X-Forwarded-Proto") !=null)
        .requiresSecure();


  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource(){

    final CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOrigins(Arrays.asList(frontendUrl));
    config.setAllowCredentials(true);
    config.setAllowedMethods(Arrays.asList("HEAD",
        "GET", "POST", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(Arrays.asList(
        "Authorization",
        "Accept",
        "Cache-Control",
        "Content-Type",
        "Access-Control-Allow-Origins",
        "Access-Control-Allow-Headers",
        "Access-Control-Allow-Credentials",
        "Access-Control-Allow-Methods",
        "x-frame-options",
        "x-xsrf-token",
        "Strict-Transport-Security",
        "Content-Security-Policy",
        "X-Content-Type-Options",
        "X-XSS-Protection",
        "X-Forwarded-Proto"
));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
  }



}
