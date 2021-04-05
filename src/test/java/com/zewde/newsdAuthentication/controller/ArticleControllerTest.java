package com.zewde.newsdAuthentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zewde.newsdAuthentication.Exceptions.ArticleNotFoundException;
import com.zewde.newsdAuthentication.entities.Article;
import com.zewde.newsdAuthentication.service.ArticleService;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import org.apache.coyote.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ArticleControllerTest {

  @InjectMocks
  ArticleController articleController;

  @Mock
  ArticleService articleService;

  @Mock
  UserDetailsServiceImplementation userService;

  private MockMvc mockMvc;


  @Before
  public void setUp(){
    mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();

  }
  @Test
  public void getArticlesPerUser() throws Exception {
    Article article1 = new Article(1,"first source","first author","first Article", "first description","first url", "first url to image", "2020-04-09", "first  content", true);
    Article article2 = new Article(2,"some second source","some  second author","some second title", "some second description","some second url", "some second url to image", "2020-04-09", "some second more content", true);

    ArrayList<Article> articles = new ArrayList<>();
    articles.add(article1);
    articles.add(article2);


    when(articleService.getArticlesByUsername(any(String.class))).thenReturn(articles);

    mockMvc.perform(MockMvcRequestBuilders.get("/articles?username=someUser").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(0))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("first Article"));
    ;

  }

  @Test
  public void saveBookmarkedArticles() throws Exception {
    Article article1 = new Article(1,"some source","some  author","some title", "some description","some url", "some url to image", "2020-04-09", "some second more content", true);
    String json = new ObjectMapper().writeValueAsString(article1);

    when(userService.findUserIdByUsername(any(String.class))).thenReturn(1);
    when(articleService.saveBookmarkedArticle(any(Article.class))).thenReturn(article1);

    mockMvc.perform(MockMvcRequestBuilders.post("/articles?username=someUser").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("some title"));
  }

  @Test
  public void shouldThrowException_whenUsernameNotFound() throws Exception {
    Article article1 = new Article(1,"some source","some  author","some title", "some description","some url", "some url to image", "2020-04-09", "some second more content", true);
    String json = new ObjectMapper().writeValueAsString(article1);

    when(userService.findUserIdByUsername(any(String.class))).thenThrow(new UsernameNotFoundException(""));

    mockMvc.perform(MockMvcRequestBuilders.post("/articles?username=someUser").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.status().reason("no user with such username found"));
  }

  @Test
  public void shouldReturnNewResponseStatusException_whenWrongUsername() throws Exception {
    when(articleService.getArticlesByUsername(any(String.class))).thenThrow(new UsernameNotFoundException(""));

    mockMvc.perform(MockMvcRequestBuilders.get("/articles?username=someUser").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.status().reason("no such username"));
  }

  @Test
  public void deleteUnbookmarkedArticle() throws Exception {
    when(articleService.deleteUnbookmarkedArticle(any(Integer.class))).thenReturn(true);

    mockMvc.perform(MockMvcRequestBuilders.delete("/articles/article?id=1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk());

  }

  @Test
  public void shouldReturnNewResponseStatusException_whenArticleNotFound() throws Exception {
    when(articleService.deleteUnbookmarkedArticle(any(Integer.class))).thenThrow(new ArticleNotFoundException(""));
    mockMvc.perform(MockMvcRequestBuilders.delete("/articles/article?id=1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
          .andExpect(MockMvcResultMatchers.status().reason("No Article with id=1"));

  }



}