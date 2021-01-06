package com.zewde.newsdAuthentication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zewde.newsdAuthentication.entities.Article;
import com.zewde.newsdAuthentication.entities.MyUserDetails;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ArticleService {

  public ArticleService(){}

  @Autowired
  private UserDetailsServiceImplementation userService;

  @Autowired
  private ArticleRepository articleRepository;


  public ArrayList<Article> getArticlesByUsername(String userName) {
    int userId;
    ArrayList<Article> articles;

    try{
      userId = userService.createUserByUsername(userName).getId();
      articles = articleRepository.loadArticleByUserId(userId);
    }catch(UsernameNotFoundException e){
      throw new UsernameNotFoundException("no such user");
    }

    return articles;

  }

}
