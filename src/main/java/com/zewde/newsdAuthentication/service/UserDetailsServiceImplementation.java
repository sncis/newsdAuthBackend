package com.zewde.newsdAuthentication.service;

import com.zewde.newsdAuthentication.Exceptions.EmailAlreadyExistException;
import com.zewde.newsdAuthentication.Exceptions.UserLoginBlockedException;
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

//import com.zewde.newsdAuthentication.Exceptions.UserLoginBlockedException;

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

  @Autowired
  private LoginFailureService loginFailureService;



  @Override
  public User loadUserByUsername(String username)throws UsernameNotFoundException, UserLoginBlockedException{


    String ipAddress = loginFailureService.getIpAddress();

    if(loginFailureService.isBlocked(ipAddress)){
      throw new UserLoginBlockedException();
    }
      Optional<User> user = userRepository.findByUsername(username);
      user.orElseThrow(() -> new UsernameNotFoundException("UserName not found"));

      return user.map(User::new).get();

  }


  public int findUserIdByUsername(String username) throws UsernameNotFoundException{
    Optional<User> user = userRepository.findByUsername(username);

    user.orElseThrow(() -> new UsernameNotFoundException("UserName not found"));

    return user.get().getId();
  }


  public User createUserByUsername(String username) throws UsernameNotFoundException{
    Optional<User> user = userRepository.findByUsername(username);
    user.orElseThrow(() -> new UsernameNotFoundException("UserName not found"));

     return user.map(User::new).get();
  }



  public RegistrationConfirmationToken registerUserAndReturnToken(User u) throws EmailAlreadyExistException, UserNameAlreadyExistException {
    RegistrationConfirmationToken registrationToken = null;

    if(userRepository.findByUsername(u.getUsername()).isPresent()) {
      throw new UserNameAlreadyExistException();

    }else if(userRepository.findAllByEmail(u.getEmail()).isPresent()){
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


  public void confirmUser(String token) {

    RegistrationConfirmationToken userToken = registrationTokenService.getToken(token);
    User user = userToken.getUser();
    user.setEnabled(true);
    userRepository.save(user);
    registrationTokenService.deleteToken(userToken.getId());
  }



  public RegistrationConfirmationToken findTokenByUserEmail(String email){

    Optional<User> user = userRepository.findUserByEmail(email);
    user.orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return registrationTokenService.findTokenByUser(user);
  }
}
