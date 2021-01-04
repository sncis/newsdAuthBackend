package com.zewde.newsdAuthentication.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class MyUserDetails implements UserDetails {

  private String username;
  private String password;
  private boolean active;
  private List<GrantedAuthority> authorities;

  public MyUserDetails(){};

  public MyUserDetails(User u){
    this.username = u.getUserName();
    this.password = u.getPassword();
    this.active = true;
    this.authorities = new ArrayList<>();
  }


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
