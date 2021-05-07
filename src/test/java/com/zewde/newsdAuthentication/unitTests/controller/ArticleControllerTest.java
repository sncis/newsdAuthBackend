package com.zewde.newsdAuthentication.unitTests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zewde.newsdAuthentication.Exceptions.ArticleNotFoundException;
import com.zewde.newsdAuthentication.Exceptions.MyControllerAdvice;
import com.zewde.newsdAuthentication.controller.ArticleController;
import com.zewde.newsdAuthentication.entities.Article;
import com.zewde.newsdAuthentication.service.ArticleService;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import com.zewde.newsdAuthentication.utils.JWTTokenUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.security.Principal;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class ArticleControllerTest {

  @InjectMocks
  ArticleController articleController;

  @Mock
  ArticleService articleService;


  @Mock
  UserDetailsServiceImplementation userService;

  @Mock
  private JWTTokenUtils jwtTokenUtils;

  private MockMvc mockMvc;

  private Principal mockPrinciple;

  @Before
  public void setUp(){
    mockMvc = MockMvcBuilders.standaloneSetup(articleController).setControllerAdvice(new MyControllerAdvice()).build();
    mockPrinciple = mock(Principal.class);

  }

  @Test
  public void getArticlesPerUser() throws Exception {
    Article article1 = new Article("1", 1,"clean_url","some  author","some title", "some summary","some link", "some published+at", "topic", "DE", "de", "1234","all rights",true);
    Article article2 = new Article("2", 2,"other clean_url","other  author","other title", "other summary","other link", "other published+at", " other topic", "EN", "en", "12345","all rights",true);
    ArrayList<Article> articles = new ArrayList<>();
    articles.add(article1);
    articles.add(article2);

    String token = jwtTokenUtils.generateToken("someUser");
    Cookie c = new Cookie("jwtToken", token);
    when(mockPrinciple.getName()).thenReturn("someUser");


    when(articleService.getArticlesByUsername(any(String.class))).thenReturn(articles);

    mockMvc.perform(MockMvcRequestBuilders.get("/articles").principal(mockPrinciple).contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0]._id").value("1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("some title"));


  }

  @Test
  public void saveBookmarkedArticles() throws Exception {
    Article article1 = new Article("1", 1,"clean_url","some  author","some title", "some summary","some link", "some published+at", "topic", "DE", "de", "1234","all rights",true);
    String json = new ObjectMapper().writeValueAsString(article1);
    when(mockPrinciple.getName()).thenReturn("someUser");

    when(userService.findUserIdByUsername(any(String.class))).thenReturn(1);
    when(articleService.saveBookmarkedArticle(any(Article.class))).thenReturn(article1);

    mockMvc.perform(MockMvcRequestBuilders.post("/articles").principal(mockPrinciple).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("some title"));
  }

  @Test
  public void shouldThrowException_whenUsernameNotFound() throws Exception {
    Article article1 = new Article("1", 1,"clean_url","some  author","some title", "some summary","some link", "some published+at", "topic", "DE", "de", "1234","all rights",true);

    String json = new ObjectMapper().writeValueAsString(article1);

    when(mockPrinciple.getName()).thenReturn("someUser");
    when(userService.findUserIdByUsername(any(String.class))).thenThrow(new UsernameNotFoundException(""));

    mockMvc.perform(MockMvcRequestBuilders.post("/articles").principal(mockPrinciple).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(result ->assertTrue(result.getResolvedException() instanceof UsernameNotFoundException));
  }

//  @Test
//  public void shouldReturnNewResponseStatusException_whenWrongUsername() throws Exception {
//    when(articleService.getArticlesByUsername(any(String.class))).thenThrow(new UsernameNotFoundException(""));
//    when(mockPrinciple.getName()).thenReturn("someUser");
//
//    mockMvc.perform(MockMvcRequestBuilders.get("/articles").principal(mockPrinciple).contentType(MediaType.APPLICATION_JSON))
//        .andExpect(MockMvcResultMatchers.status().isNotFound())
//        .andExpect(MockMvcResultMatchers.status().reason("no such username"));
//  }

  @Test
  public void deleteUnbookmarkedArticle() throws Exception {
    doNothing().when(articleService).deleteUnbookmarkedArticle(any(String.class));

    mockMvc.perform(MockMvcRequestBuilders.delete("/articles/article?id=1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk());

  }

  @Test
  public void shouldReturnNewResponseStatusException_whenArticleNotFound() throws Exception {
    doThrow(new ArticleNotFoundException("")).when(articleService).deleteUnbookmarkedArticle(any(String.class));

    mockMvc.perform(MockMvcRequestBuilders.delete("/articles/article?id=1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ArticleNotFoundException));
  }
}