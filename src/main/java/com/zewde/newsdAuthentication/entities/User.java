package com.zewde.newsdAuthentication.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.zewde.newsdAuthentication.unitTests.utils.validators.ValidEmail;
import com.zewde.newsdAuthentication.unitTests.utils.validators.ValidPassword;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name="users")
public class User implements UserDetails {

  private static final long serialVersionUID = -3413465874290419237L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="user_id")
  private int id;

  @NotNull
  @NotEmpty
  @ValidEmail
  @Column(name="email")
  private String email;

  @NotNull
  @NotEmpty
  @Column(name="username")
  private String username;

  @Column(name="password")
  @ValidPassword
  private String password;


//  @ManyToMany(cascade = CascadeType.ALL)
//  @JoinTable(name="user_roles", joinColumns = @JoinColumn(name="user_id"),
//      inverseJoinColumns = @JoinColumn(name="role_id"))
//  private List<Role> roles;
  @Column(name="role")
  private String role = UserRole.USER.name();

  @Column(name="is_locked")
  private Boolean isLocked = false;


  @Column(name="is_enabled")
  private boolean isEnabled = false;

  @Column(name="active")
  private boolean active;

  @JsonDeserialize(as = GrantedAuthority.class)
  @JsonIgnore
//  @JsonProperty("authorities")
  @Transient
  private List<GrantedAuthority> authorities = new ArrayList<>();


  public User(){};

  public User(User u){
    this.id = u.getId();
    this.username = u.getUsername();
    this.password = u.getPassword();
    this.email = u.getEmail();
    this.role = u.getRole();
    this.isEnabled = u.isEnabled();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getUsername() {
    return this.username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(this.role));
//    final SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.role);
//    return Collections.singletonList(authority);
    return authorities;
  }

  public void setAuthorities(List<GrantedAuthority> authorities) {
    this.authorities = authorities;
  }

  public boolean isLocked() {
    return isLocked;
  }

  public void setLocked(boolean locked) {
    isLocked = locked;
  }

  @Override
  public boolean isEnabled() {
    return isEnabled;
  }

  public void setEnabled(boolean enabled) {
    isEnabled = enabled;
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
  public String toString(){
    return String.format("User [userId= %d, userName= %s, email= %s, roles='user']", id,username, email);

  }
}