package com.zewde.newsdAuthentication.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginFailureService {

  @Autowired
  HttpServletRequest request;

  private final int MAX_ATTEMPTS = 5;
  private LoadingCache<String, Integer> cachedAttempts;


  public LoginFailureService(){
    super();
    cachedAttempts = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
      @Override
      public Integer load(String s) throws Exception {
        return 0;
      }
    });

  }

  public void loginSucceeded(String ipAddress){
    cachedAttempts.invalidate(ipAddress);
  }

  public void loginFailed(String ipAddress){
    int tries = 0;
    try{
      tries =  cachedAttempts.get(ipAddress);
    }catch(ExecutionException e){
      tries = 0;
    }
    tries ++;
    cachedAttempts.put(ipAddress, tries);
  }

  public boolean isBlocked(String ipAddresse){
    try{
      return cachedAttempts.get(ipAddresse) >= MAX_ATTEMPTS;
    }catch(ExecutionException e){
      return false;
    }
  }

  public String getIpAddress(){
    final String xForwardHeader = request.getHeader("X-Forwarded-For");

    if(xForwardHeader != null){
      return xForwardHeader.split(",")[0];
    }else
      return request.getRemoteAddr();

  }

}
