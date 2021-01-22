package com.zewde.newsdAuthentication.controller;

import com.zewde.newsdAuthentication.Exceptions.ArticleNotFoundException;
import com.zewde.newsdAuthentication.entities.Article;
import com.zewde.newsdAuthentication.service.ArticleService;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@CrossOrigin(origins = { "http://localhost:3000" ,"http://192.168.2.105:3000"})
@RestController
@RequestMapping("/")
public class ArticleController {

  @Autowired
  ArticleService articleService;

  @Autowired
  UserDetailsServiceImplementation userService;

  @GetMapping("/articles")
  public ResponseEntity<?> getArticlesPerUser(@RequestParam("username") String user){
    ArrayList<Article> articles;

    try{
      articles= articleService.getArticlesByUsername(user);

    }catch(UsernameNotFoundException e){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no such username",e);
    }

    return new ResponseEntity<>(articles, HttpStatus.OK);

  }

  @PostMapping("/articles")
  public ResponseEntity<?> saveBookmarkedArticles(@RequestParam("username") String username, @RequestBody Article article){
    Article savedArticle;
    try{
    int userId = userService.findUserIdByUsername(username);
    article.setUserId(userId);
    savedArticle = articleService.saveBookmarkedArticle(article);
   }catch(UsernameNotFoundException e){
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no user with such username found", e);
    }
  return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);
  }



  @DeleteMapping("/articles/article")
  public ResponseEntity<?> deleteUnbookmarkedArticle(@RequestParam("id") int id){
    try{
      articleService.deleteUnbookmarkedArticle(id);
    }catch(ArticleNotFoundException e){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format("No Article with id=%s",id),e);
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }



}

