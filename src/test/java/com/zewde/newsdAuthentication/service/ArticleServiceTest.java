package com.zewde.newsdAuthentication.service;

import com.zewde.newsdAuthentication.Exceptions.ArticleNotFoundException;
import com.zewde.newsdAuthentication.Exceptions.DataBaseException;
import com.zewde.newsdAuthentication.entities.Article;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.repositories.ArticleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
//@ActiveProfiles("test")
public class ArticleServiceTest {

  @InjectMocks
  public ArticleService articleService;

  @Mock
  private UserDetailsServiceImplementation userDetailsServiceImplementation;

  @Mock
  private ArticleRepository articleRepository;

  private ArrayList<Article> articles = new ArrayList<>();


  @Before
  public void setUp(){
    Article article1 = new Article("1", 1,"clean_url","some  author","some title", "some summary","some link", "some published+at", "topic", "DE", "de", "1234","all rights",true);
    Article article2 = new Article("2", 2,"other clean_url","other  author","other title", "other summary","other link", "other published+at", " other topic", "EN", "en", "12345","all rights",true);

    articles.add(article1);
    articles.add(article2);
  }

  @Test
  public void getArticlesByUserName() {
    User user = new User();
    user.setUsername("someUser");
    user.setId(1);

    when(userDetailsServiceImplementation.createUserByUsername(any(String.class))).thenReturn(user);
    when(articleRepository.findAllByUserId(any(int.class))).thenReturn(articles);

    ArrayList<Article> expectedArticles = articleService.getArticlesByUsername(user.getUsername());

    assertEquals(expectedArticles.get(0).getTitle(), articles.get(0).getTitle());

  }

  @Test(expected = UsernameNotFoundException.class)
  public void shouldThrowException_WhenUsernameNotFound(){
    when(userDetailsServiceImplementation.createUserByUsername(any(String.class))).thenThrow(new UsernameNotFoundException("no Username"));

    articleService.getArticlesByUsername("someUser");

  }

  @Test
  public void deleteUnbookmarkedArticles() {
    doNothing().when(articleRepository).deleteBy_id(anyString());
    boolean isDeleted = articleService.deleteUnbookmarkedArticle("1");
    assertTrue(isDeleted);
  }

  @Test(expected = ArticleNotFoundException.class)
  public void shouldThrowArticleNotFoundException_WhenDeleteUnbookmarkedArticles() {
    doThrow(new EmptyResultDataAccessException(1)).when(articleRepository).deleteBy_id(anyString());
    articleService.deleteUnbookmarkedArticle("1000");

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