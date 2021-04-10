package com.zewde.newsdAuthentication.entities;

import com.zewde.newsdAuthentication.utils.validators.ValidEmail;
import com.zewde.newsdAuthentication.utils.validators.ValidPassword;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="users")
public class User implements Serializable {
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
  private String userName;

  @Column(name="password")
  @ValidPassword
  private String password;

  @Column(name="active")
  private boolean active;

//  @ManyToMany(cascade = CascadeType.ALL)
//  @JoinTable(name="user_roles", joinColumns = @JoinColumn(name="user_id"),
//      inverseJoinColumns = @JoinColumn(name="role_id"))
//  private List<Role> roles;
    private UserRole role = UserRole.USER;


  private Boolean locked = false;


  private Boolean enabled = false;


  public User(){};

  public User(User u){
    this.userName = u.getUserName();
    this.password = u.getPassword();
    this.email = u.getEmail();
    this.id = u.getId();
    this.role = u.getRole();
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

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = true;
  }

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }


//  public List<Role> getRoles() {
//    return roles;
//  }
//
//  public void setRoles(List<Role> roles) {
//    this.roles = roles;
//  }


  @Override
  public String toString(){
    return String.format("User [userId= %d, userName= %s, email= %s, roles='user']", id,userName, email);

  }
}