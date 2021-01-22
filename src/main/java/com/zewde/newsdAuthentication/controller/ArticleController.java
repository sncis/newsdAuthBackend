package com.zewde.newsdAuthentication.controller;

import com.zewde.newsdAuthentication.entities.Article;
import com.zewde.newsdAuthentication.service.ArticleService;
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

  @GetMapping("/dashboard/articles")
  public ResponseEntity<?> getArticlesPerUser(@RequestParam String user){
    ArrayList<Article> articles;

    try{
      articles= articleService.getArticlesByUsername(user);

    }catch(UsernameNotFoundException e){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no such userName");
    }

    return new ResponseEntity<>(articles, HttpStatus.OK);

  }
}
