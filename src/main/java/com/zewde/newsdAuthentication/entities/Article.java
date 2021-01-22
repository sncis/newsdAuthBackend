package com.zewde.newsdAuthentication.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="articles")
public class Article implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="article_id")
  private int id;

  @Column(name="user_id")
  private int userId;

  @Column(name="source")
  private String source;

  @Column(name="author")
  private String author;

  @Column(name="title")
  private String title;

  @Column(name="description")
  private String description;

  @Column(name="url")
  private String url;

  @Column(name="urltoimage")
  private String urlToImage;

  @Column(name="publishedat")
  private String publishedAt;


  @Column(name="content")
  private String content;


  public Article(){
  }

  public Article(int id, String title, String description){
    this.title = title;
    this.description = description;
    this.id =id;
  }

  public Article (int userId,String source, String author, String title, String description, String url, String urlToImage,String publishedAt, String content){
    this.userId = userId;
    this.source = source;
    this.author = author;
    this.title = title;
    this.description = description;
    this.url = url;
    this.urlToImage = urlToImage;
    this.publishedAt = publishedAt;
    this.content = content;
  }


  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrlToImage() {
    return urlToImage;
  }

  public void setUrlToImage(String urlToImage) {
    this.urlToImage = urlToImage;
  }

  public String getPublishedAt() {
    return publishedAt;
  }

  public void setPublishedAt(String publishedAt) {
    this.publishedAt = publishedAt;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
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

  @Override
  public String toString(){
    return String.format("Article [articleId= %d, author= %s, description= %s, title=%s, published= %s]", id,author, description, title, publishedAt);

  }
}
