package com.zewde.newsdAuthentication.repositories;

import com.zewde.newsdAuthentication.entities.RegistrationConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RegistrationConfirmationTokenRepo extends JpaRepository<RegistrationConfirmationToken, Long> {


}
