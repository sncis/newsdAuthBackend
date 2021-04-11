package com.zewde.newsdAuthentication.repositories;

import com.zewde.newsdAuthentication.entities.Article;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE) -> use for integration test when you want totest again your actuell database
public class ArticleRepositoryTest {

  @Autowired
  private ArticleRepository repository;

  private Article article1;
  private Article article2;

  @Before
  public void setUp(){
    article1 = new Article(1,"some source","some  author","some title", "some description","some url", "some url to image", "2020-04-09", "some second more content", true);
    article2 = new Article(1,"some second source","some  second author","some second title", "some second description","some second url", "some second url to image", "2020-04-09", "some second more content", true);

    repository.save(article1);
    repository.save(article2);
  }


  @Test
  public void findAllByUserId() {
    ArrayList<Article> articles = repository.findAllByUserId(1);

    assertNotNull(article1);
    assertEquals(articles.size(),2);
    assertEquals(article1.getSource(), articles.get(0).getSource());

  }
  @Test
  public void shouldReturnEmptyArray_whenNoArticlesByUserId() {
    ArrayList<Article> articles = repository.findAllByUserId(2);

    assertEquals(articles.size(),0);

  }


  @Test
  public void deleteArticleById() {
    ArrayList<Article> articles= repository.findAllByUserId(1);

    assertEquals(repository.count(),2);

    int articleToDeleteId = articles.get(0).getId();
    repository.deleteById(articleToDeleteId);

    assertEquals(repository.count(),1);
  }

  @Test
  public void shouldCatchEmptyResultAccessException_whenNoArticleWhitGivenId() {
    ArrayList<Article> articles= repository.findAllByUserId(2);

    try{
      repository.deleteById(0);
    }catch(EmptyResultDataAccessException e) {
      System.out.println("No article with such ID");
    }

    assertEquals(articles.size(), 0);
  }
}