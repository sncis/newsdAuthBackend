package com.zewde.newsdAuthentication.controller;

import com.zewde.newsdAuthentication.entities.Article;
import com.zewde.newsdAuthentication.service.ArticleService;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import com.zewde.newsdAuthentication.utils.JWTTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;

@RestController
@RequestMapping(value="/",
    consumes= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.TEXT_PLAIN_VALUE})
public class ArticleController {

  private final static Logger logger = LoggerFactory.getLogger(ArticleController.class);


  @Autowired
  ArticleService articleService;

  @Autowired
  UserDetailsServiceImplementation userService;

  @Autowired
  JWTTokenUtils jwtTokenUtils;

  @Value("${frontend.url}")
  private String frontendUrl;

  @GetMapping("/")
  public ResponseEntity getHello(){
    return new ResponseEntity<>("Hello from newsdMe", HttpStatus.OK);
  }


  @GetMapping("/articles")
  public ResponseEntity<?> getArticlesPerUser(Principal principal){
    ArrayList<Article> articles;

    articles= articleService.getArticlesByUsername(principal.getName());

    return new ResponseEntity<>(articles, HttpStatus.OK);
  }

  @PostMapping("/articles")
  public ResponseEntity<?> saveBookmarkedArticles(Principal principal, @RequestBody Article article){

    int userId = userService.findUserIdByUsername(principal.getName());
    article.setUserId(userId);
    Article savedArticle = articleService.saveBookmarkedArticle(article);

    return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);
  }



  @DeleteMapping("/articles/article")
  public ResponseEntity<?> deleteUnbookmarkedArticle(@RequestParam("id") String id){
    articleService.deleteUnbookmarkedArticle(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/admin")
  public ResponseEntity<?> getAdminSide(){
    return new ResponseEntity<>("Hello Mrs. Admin", HttpStatus.OK);
  }

}

