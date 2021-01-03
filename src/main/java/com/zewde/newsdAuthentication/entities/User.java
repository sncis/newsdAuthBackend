package com.zewde.newsdAuthentication.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="user_id")
  private int id;

  @Column(name="email")
  private String email;

  @Column(name="username")
  private String userName;

  @Column(name="password")
  private String password;

  @Column(name="active")
  private boolean active;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name="user_roles", joinColumns = @JoinColumn(name="user_id"),
      inverseJoinColumns = @JoinColumn(name="role_id"))
  private List<Role> roles;

  public User(){};

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

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  @Override
  public String toString(){
    return String.format("User [userId= %d, userName= %s, email= %s, roles='user']", id,userName, email);

  }
}
