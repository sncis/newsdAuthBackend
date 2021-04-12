package com.zewde.newsdAuthentication.service;

import com.zewde.newsdAuthentication.Exceptions.RegistrationConfirmationTokenNotFoundException;
import com.zewde.newsdAuthentication.entities.RegistrationConfirmationToken;
import com.zewde.newsdAuthentication.repositories.RegistrationConfirmationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationConfirmationTokenService {

  @Autowired
  private RegistrationConfirmationTokenRepo tokenRepo;


  public  void saveToken(RegistrationConfirmationToken token){
    tokenRepo.save(token);
  }


  public RegistrationConfirmationToken getToken(String token){
    Optional<RegistrationConfirmationToken> confirmationToken = tokenRepo.findRegistrationConfirmationTokenByToken(token);
    confirmationToken.orElseThrow(RegistrationConfirmationTokenNotFoundException::new);

    return confirmationToken.map(RegistrationConfirmationToken::new).get();

  }

  public void deleteToken(Long id){
    tokenRepo.deleteById(id);
  }
}
