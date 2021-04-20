package com.zewde.newsdAuthentication.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="articles")
public class Article implements Serializable {

  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="article_id", unique=true)
  private int id;


  @Column(name="user_id")
  private int userId;

  @Column(name="clean_url")
  private String clean_url;

  @Column(name="author")
  private String author;

  @Column(name="title")
  private String title;

  @Column(name="summary")
  private String summary;

  @Column(name="url")
  private String link;

//  @Column(name="urltoimage")
//  private String urlToImage;

  @Column(name="published_date")
  private String published_date;


  @Column(name="topic")
  private String topic;

  @Column(name="country")
  private String country;

  @Column(name="language")
  private String language;

  @Column(name="rank")
  private String rank;

  @Column(name="rights")
  private String rights;

  @Column(name="isbookmarked")
  private boolean isBookmarked;


  public Article(){
  }

  public Article(int id, int userId, String clean_url, String author, String title, String summary, String link, String published_date, String topic, String country, String language, String rank, String rights, boolean isBookmarked){
    this.id = id;
    this.userId = userId;
    this.clean_url = clean_url;
    this.author = author;
    this.title = title;
    this.summary = summary;
    this.link = link;
    this.published_date = published_date;
    this.topic = topic;
    this.country = country;
    this.language = language;
    this.rank = rank;
    this.rights = rights;
    this.isBookmarked= isBookmarked;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getClean_url() {
    return clean_url;
  }

  public void setClean_url(String clean_url) {
    this.clean_url = clean_url;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }


  public String getPublished_date() {
    return published_date;
  }

  public void setPublished_date(String published_date) {
    this.published_date = published_date;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getRank() {
    return rank;
  }

  public void setRank(String rank) {
    this.rank = rank;
  }

  public String getRights() {
    return rights;
  }

  public void setRights(String rights) {
    this.rights = rights;
  }

  public boolean isBookmarked() {
    return isBookmarked;
  }

  public void setBookmarked(boolean bookmarked) {
    isBookmarked = bookmarked;
  }
  //  public Article(int id, String title, String description){
//    this.title = title;
//    this.description = description;
//    this.id =id;
//  }
//
//  public Article (Long id, int userId, String clean_url, String author, String title, String summary, String link, String urlToImage, String published_date, String topic, boolean isBookmarked){
//    this.id = id;
//    this.userId = userId;
//    this.clean_url = clean_url;
//    this.author = author;
//    this.title = title;
//    this.summary = summary;
//    this.link = link;
//    this.urlToImage = urlToImage;
//    this.published_date = published_date;
//    this.topic = topic;
//    this.isBookmarked = isBookmarked;
//  }
//
//
//  public void setId(Long id) {
//    this.id = id;
//  }
//
//  public Long getId() {
//    return id;
//  }
//
//  public boolean isBookmarked() {
//    return isBookmarked;
//  }
//
//  public int getUserId() {
//    return userId;
//  }
//
//  public void setUserId(int userId) {
//    this.userId = userId;
//  }
//
//  public String getClean_url() {
//    return clean_url;
//  }
//
//  public void setClean_url(String clean_url) {
//    this.clean_url = clean_url;
//  }
//
//  public String getAuthor() {
//    return author;
//  }
//
//  public void setAuthor(String author) {
//    this.author = author;
//  }
//
//  public String getLink() {
//    return link;
//  }
//
//  public void setLink(String link) {
//    this.link = link;
//  }
//
//  public String getUrlToImage() {
//    return urlToImage;
//  }
//
//  public void setUrlToImage(String urlToImage) {
//    this.urlToImage = urlToImage;
//  }
//
//  public String getPublished_date() {
//    return published_date;
//  }
//
//  public void setPublished_date(String published_date) {
//    this.published_date = published_date;
//  }
//
//  public String getContent() {
//    return topic;
//  }
//
//  public void setContent(String content) {
//    this.topic = content;
//  }
//
//  public boolean getIsBookmarked() {
//    return isBookmarked;
//  }
//
//  public void setBookmarked(boolean isBookmarked) {
//    this.isBookmarked = isBookmarked;
//  }
//
//
//  public String getTitle() {
//    return title;
//  }
//
//  public void setTitle(String title) {
//    this.title = title;
//  }
//
//  public String getSummary() {
//    return summary;
//  }
//
//  public void setSummary(String description) {
//    this.summary = description;
//  }

  @Override
  public String toString(){
    return String.format("Article [articleId= %d, author= %s, description= %s, title=%s, published= %s]", id,author, summary, title, published_date);

  }
}
