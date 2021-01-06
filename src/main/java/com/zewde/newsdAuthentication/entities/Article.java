package com.zewde.newsdAuthentication.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Article implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  private String title;
  private String description;


  public Article(){
  }

  public Article(String title, String description){
    this.title = title;
    this.description = description;
  }

  public Article(int id, String title, String description){
    this.title = title;
    this.description = description;
    this.id =id;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
