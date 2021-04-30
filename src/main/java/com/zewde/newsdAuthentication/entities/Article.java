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
  @Column(name="_id", unique=true)
  private String _id;

  
  @Column(name="user_id")
  private int userId;

  @Column(name="clean_url")
  private String clean_url;

  @Column(name="author")
  private String author;

  @Column(name="title")
  private String title;

  @Column(name="summary", columnDefinition="TEXT")
  private String summary;

  @Column(name="url")
  private String link;

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

  @Column(name="bookmarked")
  private boolean bookmarked;

  public Article(){
  }

  public Article(String _id, int userId, String clean_url, String author, String title, String summary, String link, String published_date, String topic, String country, String language, String rank, String rights, boolean bookmarked){
    this._id = _id;
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
    this.bookmarked = bookmarked;
  }
  public Article(String _id, String clean_url, String author, String title, String summary, String link, String published_date, String topic, String country, String language, String rank, String rights, boolean bookmarked){
    this._id = _id;
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
    this.bookmarked = bookmarked;
  }

  public String get_id() {
    return _id;
  }

  public void set_id(String id) {
    this._id = id;
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
    return bookmarked;
  }

  public void setBookmarked(boolean bookmarked) {
    this.bookmarked = bookmarked;
  }

  @Override
  public String toString(){
    return String.format("Article [articleId= %s, author= %s, description= %s, title=%s, published= %s]", _id,author, summary, title, published_date);

  }
}
