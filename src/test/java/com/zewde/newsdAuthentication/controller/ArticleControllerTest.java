package com.zewde.newsdAuthentication.controller;

import com.zewde.newsdAuthentication.entities.Article;
import com.zewde.newsdAuthentication.service.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArticleControllerTest {

  @InjectMocks
  ArticleController articleController;

  @Mock
  ArticleService articleService;


  @Test
  public void getArticlesPerUser(){
    Article article = new Article(0,"first Article", "first discription");
    Article article1 = new Article(1,"second Article", "second discription");

    ArrayList<Article> articles = new ArrayList<>();
    articles.add(article);
    articles.add(article1);


    when(articleService.getArticlesByUsername(any(String.class))).thenReturn(articles);

    ResponseEntity<?> expectedArticles = articleController.getArticlesPerUser("someUser");
    assertTrue(expectedArticles.getStatusCode().is2xxSuccessful());
    assertEquals(expectedArticles.getBody(), articles);

  }


}