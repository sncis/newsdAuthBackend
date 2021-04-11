package com.zewde.newsdAuthentication.service;

import com.zewde.newsdAuthentication.Exceptions.ArticleNotFoundException;
import com.zewde.newsdAuthentication.Exceptions.DataBaseException;
import com.zewde.newsdAuthentication.entities.Article;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.repositories.ArticleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ArticleServiceTest {

  @Autowired
  public ArticleService articleService;

  @MockBean
  private UserDetailsServiceImplementation userDetailsServiceImplementation;

  @MockBean
  private ArticleRepository articleRepository;

  private ArrayList<Article> articles = new ArrayList<>();


  @Before
  public void setUp(){
    Article article1 = new Article(1,"some source","some  author","some title", "some description","some url", "some url to image", "2020-04-09", "some second more content", true);
    Article article2 = new Article(1,"some second source","some  second author","some second title", "some second description","some second url", "some second url to image", "2020-04-09", "some second more content", true);

    articles.add(article1);
    articles.add(article2);
  }

  @Test
  public void getArticlesByUserName() {
    User user = new User();
    user.setUserName("someUser");
    user.setId(1);

    when(userDetailsServiceImplementation.createUserByUsername(any(String.class))).thenReturn(user);
    when(articleRepository.findAllByUserId(any(int.class))).thenReturn(articles);

    ArrayList<Article> expectedArticles = articleService.getArticlesByUsername(user.getUserName());

    assertEquals(expectedArticles.get(0).getTitle(), articles.get(0).getTitle());

  }

  @Test(expected = UsernameNotFoundException.class)
  public void shouldThrowException_WhenUsernameNotFound(){
    when(userDetailsServiceImplementation.createUserByUsername(any(String.class))).thenThrow(new UsernameNotFoundException("no Username"));

    articleService.getArticlesByUsername("someUser");

  }

  @Test
  public void deleteUnbookmarkedArticles() {
    doNothing().when(articleRepository).deleteById(anyInt());
    boolean isDeleted = articleService.deleteUnbookmarkedArticle(1);
    assertTrue(isDeleted);
  }

  @Test(expected = ArticleNotFoundException.class)
  public void shouldTrowArticleNotFoundException_WhendeleteUnbookmarkedArticles() {
    doThrow(new EmptyResultDataAccessException(1)).when(articleRepository).deleteById(anyInt());
    articleService.deleteUnbookmarkedArticle(1000);

  }

  @Test
  public void saveBookmarkedArticle() {
    when(articleRepository.saveAndFlush(any(Article.class))).thenReturn(articles.get(0));

    articleService.saveBookmarkedArticle(articles.get(0));
    assertEquals(articles.get(0).getTitle(), "some title");
  }

  @Test(expected = DataBaseException.class)
  public void shouldreturnNull_whenDataBaseExpetion() {
   when(articleRepository.saveAndFlush(any(Article.class))).thenThrow(new DataBaseException("something went wrong"));

   articleService.saveBookmarkedArticle(articles.get(0));

  }
}