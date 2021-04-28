package com.zewde.newsdAuthentication.controller;

import com.zewde.newsdAuthentication.Exceptions.ArticleNotFoundException;
import com.zewde.newsdAuthentication.entities.Article;
import com.zewde.newsdAuthentication.service.ArticleService;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import com.zewde.newsdAuthentication.utils.JWTTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

  @GetMapping("/articles")
  public ResponseEntity<?> getArticlesPerUser(Principal principal){
    ArrayList<Article> articles;

    try{
      articles= articleService.getArticlesByUsername(principal.getName());
    }catch(UsernameNotFoundException e){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no such username",e);
    }catch(Exception e ){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong",e);
    }
    return new ResponseEntity<>(articles, HttpStatus.OK);
  }

  @PostMapping("/articles")
  public ResponseEntity<?> saveBookmarkedArticles(Principal principal, @RequestBody Article article){
    Article savedArticle = null;

    try{
    int userId = userService.findUserIdByUsername(principal.getName());
    article.setUserId(userId);
    savedArticle = articleService.saveBookmarkedArticle(article);

    }catch(UsernameNotFoundException e){
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no user with such username found", e);
    }
    catch(Exception e){
      logger.warn("**************************************");
      logger.error(e.getMessage());
      logger.error(String.valueOf(e.getCause()));
    }
//    return new ResponseEntity<>(allArticles, HttpStatus.CREATED);
    return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);

  }



  @DeleteMapping("/articles/article")
  public ResponseEntity<?> deleteUnbookmarkedArticle(@RequestParam("id") String id){
    ArrayList<Article> articles;
    try{
      articleService.deleteUnbookmarkedArticle(id);
    }catch(ArticleNotFoundException e){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format("No Article with id=%s",id),e);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/admin")
  public ResponseEntity<?> getAdminSide(){
    return new ResponseEntity<>("hello Mrs. Admin", HttpStatus.OK);
  }

}

