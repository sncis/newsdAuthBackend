package com.zewde.newsdAuthentication.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="registrationConfirmationToken")
public class RegistrationConfirmationToken {

  public RegistrationConfirmationToken(){}

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;


//  @ValidRegistrationToken
  @NotNull
  @NotEmpty
  private String token;

  private LocalDate date;

  @OneToOne(targetEntity = User.class,fetch = FetchType.EAGER)
  @JoinColumn(nullable=false, name="user_id")
  private User user;

  public RegistrationConfirmationToken(User u){
    this.user = u;
    this.date = LocalDate.now();
    this.token = UUID.randomUUID().toString();

  }

  public RegistrationConfirmationToken(RegistrationConfirmationToken token) {
  this.user = token.getUser();
  this.date = token.getDate();
  this.token = token.getToken();
  this.id = token.getId();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
