package com.zewde.newsdAuthentication.service;

import com.zewde.newsdAuthentication.entities.Article;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.repositories.ArticleRepository;
import com.zewde.newsdAuthentication.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ArticleServiceTest {

  @InjectMocks
  public ArticleService articleService;

  @Mock
  private UserDetailsServiceImplementation userDetailsServiceImplementation;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ArticleRepository articleRepository;

  @Test
  public void getArticlesByUserName() {
    Article article = new Article(1,"test Article", "test description");
    Article article1 = new Article(1,"test Article", "test description");

    ArrayList<Article> articles = new ArrayList<>();
    articles.add(article);
    articles.add(article1);

    User user = new User();
    user.setUserName("someUser");
    user.setId(1);

    when(userDetailsServiceImplementation.createUserByUsername(any(String.class))).thenReturn(user);
    when(articleRepository.findAllByUserId(any(int.class))).thenReturn(articles);
//    when(articleRepository.findAllArticlesByUserId(any(int.class))).thenReturn(articles);


    ArrayList<Article> expectedArticles = articleService.getArticlesByUsername(user.getUserName());

    assertEquals(expectedArticles.get(0).getTitle(), articles.get(0).getTitle());

  }

  @Test(expected = UsernameNotFoundException.class)
  public void shouldThrowException_WhenUsernameNotFound(){
    when(userDetailsServiceImplementation.createUserByUsername(any(String.class))).thenThrow(new UsernameNotFoundException("no Username"));

    articleService.getArticlesByUsername("someUser");

  }
}