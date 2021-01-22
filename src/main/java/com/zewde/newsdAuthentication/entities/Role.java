package com.zewde.newsdAuthentication.entities;


import javax.persistence.*;

@Entity
@Table(name="roles")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="role_id")
  private int id;

  @Column(name="role_name")
  private String name;

  @Column(name="role_description")
  private String desc;

  public Role(){};

  public Role(String name, String desc){
    this.name= name;
    this.desc = desc;
  }


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  @Override
  public String toString(){
    return String.format("Role [roleId= %d, name= %s, description= %s", id,name,desc);
  }
}