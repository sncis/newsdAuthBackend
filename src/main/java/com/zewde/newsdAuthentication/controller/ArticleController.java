package com.zewde.newsdAuthentication.controller;

import com.zewde.newsdAuthentication.Exceptions.ArticleNotFoundException;
import com.zewde.newsdAuthentication.entities.Article;
import com.zewde.newsdAuthentication.unitTests.service.ArticleService;
import com.zewde.newsdAuthentication.unitTests.service.UserDetailsServiceImplementation;
import com.zewde.newsdAuthentication.unitTests.utils.JWTTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@RequestMapping("/")
public class ArticleController {

  @Autowired
  ArticleService articleService;

  @Autowired
  UserDetailsServiceImplementation userService;

  @Autowired
  JWTTokenUtils jwtTokenUtils;

  @GetMapping("/articles")
  public ResponseEntity<?> getArticlesPerUser(@RequestParam("username") String user, HttpServletRequest req){
    ArrayList<Article> articles;
    Cookie[] cookies =  req.getCookies();
    if(cookies != null){
      for(Cookie c : cookies){
        System.out.println("cookies from request");
        System.out.println(c.getName());
        }
    }

    try{
      articles= articleService.getArticlesByUsername(user);

    }catch(UsernameNotFoundException e){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no such username",e);
    }catch(Exception e ){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong",e);

    }
    System.out.println("artcile in controler");
    System.out.println(articles);
    return new ResponseEntity<>(articles, HttpStatus.OK);

  }

  @PostMapping("/articles")
  public ResponseEntity<?> saveBookmarkedArticles(@RequestParam("username") String username, @RequestBody Article article){
    Article savedArticle;
//    ArrayList<Article> allArticles;
    try{
    int userId = userService.findUserIdByUsername(username);
    article.setUserId(userId);
    savedArticle = articleService.saveBookmarkedArticle(article);
//      articleService.saveBookmarkedArticle(article);
//    allArticles = articleService.getArticlesByUsername(username);

    }catch(UsernameNotFoundException e){
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no user with such username found", e);
    }
//    return new ResponseEntity<>(allArticles, HttpStatus.CREATED);
    return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);

  }



  @DeleteMapping("/articles/article")
  public ResponseEntity<?> deleteUnbookmarkedArticle(@RequestParam("id") int id){
    ArrayList<Article> articles;
    try{
      articleService.deleteUnbookmarkedArticle(id);
    }catch(ArticleNotFoundException e){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format("No Article with id=%s",id),e);
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }

}

