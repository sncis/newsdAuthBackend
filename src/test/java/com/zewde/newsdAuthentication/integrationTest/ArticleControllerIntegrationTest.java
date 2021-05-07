package com.zewde.newsdAuthentication.integrationTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zewde.newsdAuthentication.entities.Article;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.repositories.ArticleRepository;
import com.zewde.newsdAuthentication.repositories.UserRepository;
import com.zewde.newsdAuthentication.utils.JWTTokenUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.security.Principal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArticleControllerIntegrationTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ArticleRepository articleRepository;

  @Autowired
  private JWTTokenUtils jwtTokenUtils;

  private String USERNAME = "someTestUser";

  private Principal mockPrinciple;


  @BeforeAll
  public void setUpUserForArticle(){
    User user = new User();
    user.setEmail("testEmailS@email.com");
    user.setUsername(USERNAME);
    user.setPassword("somePass1234!");
    user.setEnabled(true);

    userRepository.save(user);

  }
  @BeforeEach
  public void setUp(){
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(this.context)
        .apply(springSecurity())
        .build();

    mockPrinciple = mock(Principal.class);
  }

  @Test
  public void shouldReturnAllArticlesForOneUser() throws Exception{
    User user = userRepository.findByUsername(USERNAME).get();
    articleRepository.save(new Article("1", user.getId(),"clean_url","some  author","some title", "some summary","some link", "some published+at", "topic", "DE", "de", "1234","all rights",true));
    articleRepository.save(new Article("2", user.getId(),"other clean_url","other  author","other title", "other summary","other link", "other published+at", " other topic", "EN", "en", "12345","all rights",true));
    String token = jwtTokenUtils.generateToken(USERNAME);

    when(mockPrinciple.getName()).thenReturn(USERNAME);

    this.mockMvc.perform(MockMvcRequestBuilders.get("/articles").contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE).with(csrf()).cookie(new Cookie("jwtToken", token))).andDo(print())
    .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$[0]._id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("some title"));
  }
  @Test
  public void shouldThrowError_WhenPostingInvalidArticle() throws Exception{
    User user = userRepository.findByUsername(USERNAME).get();
    String token = jwtTokenUtils.generateToken(USERNAME);

    Article article = new Article("2", user.getId(),"kitv","other  author","other title", "other summary","ht://www.kitv.com/story/42196143/to-cap-off-his-amazing-week-", "2020-06-01 16:11:00", "tpoic", "EN", "en", "12345","Copyright 2000 - ",true);
    ObjectMapper om = new ObjectMapper();
    String jsonArticle = om.writeValueAsString(article);

    this.mockMvc.perform(post("/articles").contentType(MediaType.APPLICATION_JSON).content(jsonArticle).with(csrf()).cookie(new Cookie("jwtToken", token))).andDo(print())
        .andExpect(status().isBadRequest()).andExpect(status().reason("Article Data are not valid! Article cannot be stored"));

  }
}
