package com.zewde.newsdAuthentication.entities;

import com.zewde.newsdAuthentication.utils.validators.ValidPassword;
import com.zewde.newsdAuthentication.utils.validators.ValidUsername;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LoginUser implements Serializable {
  private static final long serialVersionUID = 7519288840064088841L;

  @ValidUsername
  private String username;

  @ValidPassword
  private String password;

  private String role = UserRole.USER.name();


//  @JsonDeserialize(as = GrantedAuthority.class)
//  @JsonIgnore
//  @Transient
  private List<GrantedAuthority> authorities = new ArrayList<>();

  public LoginUser(){};

  public LoginUser(String username, String password){
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(this.role));
    return authorities;
  }
  public void setAuthorities(List<GrantedAuthority> authorities) {
    this.authorities = authorities;
  }

}
