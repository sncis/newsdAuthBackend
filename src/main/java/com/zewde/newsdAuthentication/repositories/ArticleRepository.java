package com.zewde.newsdAuthentication.repositories;

import com.zewde.newsdAuthentication.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer>{

//  @Query(value = "select * from Articles where user_id = ?", nativeQuery = true)
//  ArrayList<Article> findAllArticlesByUserId(int userId);

  void deleteById(int id);

  ArrayList<Article> findAllByUserId(int userId);

  void deleteArticleByTitle(String title);
}