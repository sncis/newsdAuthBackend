package com.zewde.newsdAuthentication.repositories;

import com.zewde.newsdAuthentication.entities.Article;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
//@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE) -> use for integration test when you want totest again your actuell database
public class ArticleRepositoryTest {

  @Autowired
  private ArticleRepository repository;

  private Article article1;
  private Article article2;

  @Before
  public void setUp(){
    article1 = new Article(1,"some source","some  author","some title", "some description","some url", "some url to image", "2020-04-09", "some second more content");
    article2 = new Article(1,"some second source","some  second author","some second title", "some second description","some second url", "some second url to image", "2020-04-09", "some second more content");

    repository.save(article1);
    repository.save(article2);
  }


  @Test
  public void findAllByUserId() {
    ArrayList<Article> articles = repository.findAllByUserId(1);
//    System.out.println("****************");
//    System.out.println(articles.get(0).toString());
//    System.out.println("****************");

//    assertEquals(articles.get(0).getSource(), "some source");
    assertNotNull(article1);

    assertEquals(articles.size(),2);
    assertEquals(article1.getSource(), articles.get(0).getSource());


  }

  @Test
  public void deleteArticleById() {
    ArrayList<Article> articles = repository.findAllByUserId(1);
    assertEquals(articles.size(),2);

    int articleToDeleteId = articles.get(0).getId();

    repository.deleteById(articleToDeleteId);

    assertEquals(repository.count(),1);
  }
}