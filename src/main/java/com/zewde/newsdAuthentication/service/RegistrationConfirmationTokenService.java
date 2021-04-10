package com.zewde.newsdAuthentication.service;

import com.zewde.newsdAuthentication.entities.RegistrationConfirmationToken;
import com.zewde.newsdAuthentication.repositories.RegistrationConfirmationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationConfirmationTokenService {

  @Autowired
  private RegistrationConfirmationTokenRepo tokenRepo;

  public  void saveToken(RegistrationConfirmationToken token){
    tokenRepo.save(token);
  }

  public void deleteToken(Long id){
    tokenRepo.deleteById(id);

  }



}
