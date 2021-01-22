package com.zewde.newsdAuthentication.repositories;

import com.zewde.newsdAuthentication.entities.Article;
import com.zewde.newsdAuthentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

  Optional<User> findByUserName(String userName);

  User findAllByEmail(String email);
}
//
//  ArrayList<Article> articles = new ArrayList<>();
//
//    articles.add(new Article(0,"first Art", "first description"));
//        articles.add(new Article(1,"second Art", "second description"));
//        articles.add(new Article(2,"third Art", "third description"));
//
//        return articles;
