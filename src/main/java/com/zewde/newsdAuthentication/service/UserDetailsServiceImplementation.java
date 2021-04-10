package com.zewde.newsdAuthentication.service;

import com.zewde.newsdAuthentication.Exceptions.EmailAlreadyExistException;
import com.zewde.newsdAuthentication.Exceptions.UserNameAlreadyExistException;
import com.zewde.newsdAuthentication.entities.MyUserDetails;
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
  public MyUserDetails loadUserByUsername(String userName){
    Optional<User> user = userRepository.findByUserName(userName);

    user.orElseThrow(() -> new UsernameNotFoundException("UserName not found"));

    return user.map(MyUserDetails::new).get();
  }

  public int findUserIdByUsername(String username){
    Optional<User> user = userRepository.findByUserName(username);

    user.orElseThrow(() -> new UsernameNotFoundException("UserName not found"));

    return user.get().getId();
  }


  public MyUserDetails loginUser(User u){
    return loadUserByUsername(u.getUserName());
  }


  public User createUserByUsername(String userName){
    Optional<User> user = userRepository.findByUserName(userName);
    user.orElseThrow(() -> new UsernameNotFoundException("UserName not found"));

     return user.map(User::new).get();
  }

  public User registerUser(User u) throws EmailAlreadyExistException, UserNameAlreadyExistException {

    if(userRepository.findByUserName(u.getUserName()).isPresent()) {
      throw new UserNameAlreadyExistException();

    }if(userRepository.findAllByEmail(u.getEmail()) != null){
      throw new EmailAlreadyExistException();
    }

    User newUser = new User();
    newUser.setEmail(u.getEmail());
    newUser.setPassword(passwordEncoder.encode(u.getPassword()));
    newUser.setUserName(u.getUserName());
    newUser.setActive(true);

    RegistrationConfirmationToken registrationToken = new RegistrationConfirmationToken(u);

    registrationTokenRepo.save(registrationToken);

    return userRepository.save(newUser);

  }

  public void confimrUser(RegistrationConfirmationToken token) {
    User user = token.getUser();
    MyUserDetails userDetails = new MyUserDetails(user);

    userDetails.setEnabled(true);

    userRepository.save(user);

    registrationTokenService.deleteToken(token.getId());
  }

}
