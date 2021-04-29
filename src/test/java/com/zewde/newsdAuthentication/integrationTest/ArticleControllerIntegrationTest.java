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

//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@WebMvcTest(ArticleController.class)
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArticleControllerIntegrationTest {

  //  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

//  @Autowired
//  private WebApplicationContext context;

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

//
//    mockMvc = MockMvcBuilders
//        .webAppContextSetup(context)
//        .apply(springSecurity())
//        .build();
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
        .andExpect(status().isBadRequest()).andExpect(status().reason("Article Data are not valid! Article cannot stored"));

  }
//  @Test
//  public void shouldReturn201_WhenPostingValidArticle() throws Exception{
//    User user = userRepository.findByUsername(USERNAME).get();
//    String token = jwtTokenUtils.generateToken(USERNAME);
//
//    Article article = new Article("2", user.getId(),"kitv.com","other  author","other title", "other summary","http://www.kitv.com/story/42196143/to-cap-off-his-amazing-week-", "2020-06-01 16:11:00", "tpoic", "EN", "en", "12345","Copyright 2000 - ",true);
//
//    ObjectMapper om = new ObjectMapper();
//    String jsonArticle = om.writeValueAsString(article);
//
//    this.mockMvc.perform(post("/articles?username="+USERNAME).content(jsonArticle).contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8").with(csrf()).cookie(new Cookie("jwtToken", token))).andDo(print())
//        .andExpect(status().isOk());
//
//  }


//     Article article = new Article("2", user.getId(),"kitv.com","other  author","other title", "other summary","http://www.kitv.com/story/42196143/to-cap-off-his-amazing-week-", "2020-06-01 16:11:00", "tpoic", "EN", "en", "12345","Copyright 2000 - ",true);



//
//  @Test
//  public void getArticlesPerUser() throws Exception {
//    User user = userRepository.findByUsername(USERNAME).get();
//    articleRepository.save(new Article("1", user.getId(),"clean_url","some  author","some title", "some summary","some link", "some published+at", "topic", "DE", "de", "1234","all rights",true));
//    articleRepository.save(new Article("2", user.getId(),"other clean_url","other  author","other title", "other summary","other link", "other published+at", " other topic", "EN", "en", "12345","all rights",true));
//
//
//    String token = jwtTokenUtils.generateToken(USERNAME);
//
//    ArrayList<Article> art = articleRepository.findAllByUserId(user.getId());
//    System.out.println(art);
//
//    mockMvc.perform(MockMvcRequestBuilders.get("/articles?username="+USERNAME).contentType(MediaType.APPLICATION_JSON).with(csrf()).cookie(new Cookie("jwtToken", token)))
//        .andExpect(MockMvcResultMatchers.status().isOk())
//        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect(MockMvcResultMatchers.jsonPath("$[0]._id").value(1))
//        .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("some title"));
//
//
//  }
//
//  @Test
//  public void postArticleForUser() throws Exception {
//    User user = userRepository.findByUsername(USERNAME).get();
//
//    Article article = new Article("4", user.getId(),"kitv.com","author","some title", "By Chris Isidore, CNN Business Elon..","http://www.kitv.com/story/42196143/to-cap-", "2020-06-01 16:11:00", "NA", "DE", "de", "1234","Copyright 2000 - 2020 WorldNow and KITV",true);
//
//    ObjectMapper mapper = new ObjectMapper();
//    String json = mapper.writeValueAsString(article);
//    String token = jwtTokenUtils.generateToken(USERNAME);
//
//
//      mockMvc.perform(post("/articles?username="+USERNAME).content(json)
//          .with(csrf()).cookie(new Cookie("jwtToken", token))).andDo(print()).andExpect(status().isOk());


//    mockMvc.perform(post("/articles?username="+USERNAME).content(json).contentType(MediaType.APPLICATION_JSON_VALUE).characterEncoding("utf-8")
//        .with(csrf()).cookie(new Cookie("jwtToken", token))).andDo(print()).andExpect(status().isOk());
//          .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//          .andExpect(MockMvcResultMatchers.jsonPath("$[0]._id").value(1))
//          .andExpect(MockMvcResultMatchers.jsonPath("$[0].clean_url").value("kitv.com"));

//  }

//  @Test
//  public void shouldThrowError_whenArticleDataIsInvalid() throws Exception {
//
//    Article article = new Article("5", 1,"clean_url","some  author","some title", "some summary","some link", "some published+at", "topic", "DE", "de", "1234","all rights",true);
//    ObjectMapper mapper = new ObjectMapper();
//    String json = mapper.writeValueAsString(article);
//    String token = jwtTokenUtils.generateToken(USERNAME);
//
//    mockMvc.perform(post("/articles?username="+USERNAME).contentType(MediaType.APPLICATION_JSON).content(json).with(csrf()).cookie(new Cookie("jwtToken", token)))
//        .andExpect(MockMvcResultMatchers.status().isBadRequest());
//
//  }


}
