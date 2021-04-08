package com.zewde.newsdAuthentication.service;

import com.zewde.newsdAuthentication.Exceptions.ArticleNotFoundException;
import com.zewde.newsdAuthentication.Exceptions.DataBaseException;
import com.zewde.newsdAuthentication.entities.Article;
import com.zewde.newsdAuthentication.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class ArticleService {

  public ArticleService(){}

  @Autowired
  private UserDetailsServiceImplementation userService;

  @Autowired
  private ArticleRepository articleRepository;


  public ArrayList<Article> getArticlesByUsername(String userName) {
    int userId;
    ArrayList<Article> articles = null;

    try{
      userId = userService.createUserByUsername(userName).getId();
      articles = articleRepository.findAllByUserId(userId);
      System.out.println("*******************");
      System.out.println(articles);
    }catch(UsernameNotFoundException e){
      throw new UsernameNotFoundException("no such user");
    }catch(Exception e){
      System.out.println(e.getMessage());
    }

    System.out.println(articles);

    return articles;

  }

  public boolean deleteUnbookmarkedArticle(int articleId) throws ArticleNotFoundException{

    try {
//      articleRepository.deleteArticleByTitle(title);
      articleRepository.deleteById(articleId);
      return true;
    } catch(EmptyResultDataAccessException e) {
      throw new ArticleNotFoundException("No such article");
    }
  }


  public Article saveBookmarkedArticle(Article article){
    try{
      return articleRepository.saveAndFlush(article);
    }catch(Exception e){
      throw new DataBaseException("something went wrong");
    }

  }
}
