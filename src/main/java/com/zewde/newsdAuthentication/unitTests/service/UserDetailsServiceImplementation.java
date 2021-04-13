package com.zewde.newsdAuthentication.unitTests.service;

import com.zewde.newsdAuthentication.Exceptions.EmailAlreadyExistException;
import com.zewde.newsdAuthentication.Exceptions.UserNameAlreadyExistException;
import com.zewde.newsdAuthentication.entities.RegistrationConfirmationToken;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.repositories.RegistrationConfirmationTokenRepo;
import com.zewde.newsdAuthentication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserDetailsServiceImplementation implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  private RegistrationConfirmationTokenRepo registrationTokenRepo;


  @Autowired
  private RegistrationConfirmationTokenService registrationTokenService;


  @Override
  public User loadUserByUsername(String username){
    Optional<User> user = userRepository.findByUsername(username);

    user.orElseThrow(() -> new UsernameNotFoundException("UserName not found"));

    return user.map(User::new).get();
  }

  public int findUserIdByUsername(String username){
    Optional<User> user = userRepository.findByUsername(username);

    user.orElseThrow(() -> new UsernameNotFoundException("UserName not found"));

    return user.get().getId();
  }


  public User loginUser(User u){
    return loadUserByUsername(u.getUsername());
  }


  public User createUserByUsername(String username){
    Optional<User> user = userRepository.findByUsername(username);
    user.orElseThrow(() -> new UsernameNotFoundException("UserName not found"));

     return user.map(User::new).get();
  }

  public User registerUser(User u) throws EmailAlreadyExistException, UserNameAlreadyExistException {

    if(userRepository.findByUsername(u.getUsername()).isPresent()) {
      throw new UserNameAlreadyExistException();

    }if(userRepository.findAllByEmail(u.getEmail()) != null){
      throw new EmailAlreadyExistException();
    }

    User newUser = new User();
    newUser.setEmail(u.getEmail());
    newUser.setPassword(passwordEncoder.encode(u.getPassword()));
    newUser.setUsername(u.getUsername());
    newUser.setActive(true);

    try{
      userRepository.save(newUser);
      RegistrationConfirmationToken registrationToken = new RegistrationConfirmationToken(newUser);
      System.out.println("registration user in registration ");
      System.out.println(registrationToken);

      registrationTokenRepo.save(registrationToken);
    }catch(Exception e){
      System.out.println(e.getMessage());
    }
    return newUser;
  }


  public RegistrationConfirmationToken registerUserAndReturnToken(User u) throws EmailAlreadyExistException, UserNameAlreadyExistException {
    RegistrationConfirmationToken registrationToken = null;

    if(userRepository.findByUsername(u.getUsername()).isPresent()) {
      throw new UserNameAlreadyExistException();

    }if(userRepository.findAllByEmail(u.getEmail()) != null){
      throw new EmailAlreadyExistException();
    }

    User newUser = new User();
    newUser.setEmail(u.getEmail());
    newUser.setPassword(passwordEncoder.encode(u.getPassword()));
    newUser.setUsername(u.getUsername());
    newUser.setActive(true);

    try{
      userRepository.save(newUser);
      registrationToken = new RegistrationConfirmationToken(newUser);
      System.out.println("registration user in registration ");
      System.out.println(registrationToken);

      registrationTokenRepo.save(registrationToken);
      return registrationToken;
    }catch(Exception e){
      System.out.println(e.getMessage());
    }
    return registrationToken;
  }

  public void confirmUser(RegistrationConfirmationToken token) {
    User user = token.getUser();

    user.setEnabled(true);

    userRepository.save(user);
    registrationTokenService.deleteToken(token.getId());
  }

}
