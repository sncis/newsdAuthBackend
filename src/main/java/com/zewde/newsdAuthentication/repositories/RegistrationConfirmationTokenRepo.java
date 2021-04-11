package com.zewde.newsdAuthentication.repositories;

import com.zewde.newsdAuthentication.entities.RegistrationConfirmationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RegistrationConfirmationTokenRepo extends CrudRepository<RegistrationConfirmationToken, Long> {

  Optional<RegistrationConfirmationToken> findRegistrationConfirmationTokenByToken(String token);

}
