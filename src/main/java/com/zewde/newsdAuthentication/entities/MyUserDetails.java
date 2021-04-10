package com.zewde.newsdAuthentication.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class MyUserDetails implements UserDetails {

  private String username;

  private String password;

  private boolean active;

  private String role;

  private boolean isLocked = false;

  private boolean isEnabled = false;

  private List<GrantedAuthority> authorities;

  public MyUserDetails(){};

  public MyUserDetails(User u){
    this.username = u.getUserName();
    this.password = u.getPassword();
    this.active = true;
    this.authorities = new ArrayList<>();
    this.role = u.getRole().name();

  }


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    final SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.role);
    return Collections.singletonList(authority);
  }


  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public boolean isLocked() {
    return isLocked;
  }

  public void setLocked(boolean locked) {
    isLocked = locked;
  }

  public void setEnabled(boolean enabled) {
    isEnabled = enabled;
  }

  public void setAuthorities(List<GrantedAuthority> authorities) {
    this.authorities = authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !isLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return isEnabled;
  }
}
