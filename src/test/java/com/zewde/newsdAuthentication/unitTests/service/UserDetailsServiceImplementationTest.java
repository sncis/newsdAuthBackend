package com.zewde.newsdAuthentication.unitTests.service;

import com.zewde.newsdAuthentication.Exceptions.EmailAlreadyExistException;
import com.zewde.newsdAuthentication.Exceptions.UserNameAlreadyExistException;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.repositories.RegistrationConfirmationTokenRepo;
import com.zewde.newsdAuthentication.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserDetailsServiceImplementationTest {

  @InjectMocks
  public UserDetailsServiceImplementation userDetailsServiceImplementation;

  @Mock
  private UserRepository userRepository;

  @Mock
  private BCryptPasswordEncoder passwordEncoder;

  @Mock
  private RegistrationConfirmationTokenRepo registrationTokenRepo;

  private User createUser(){
    User u = new User();
    u.setPassword("testPass");
    u.setEmail("test@email.com");
    u.setUsername("testUser");

    return u;
  }

  @Test
  public void loadUserByUsername() {
    User u = createUser();
    when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(u));

    User userDetails = userDetailsServiceImplementation.loadUserByUsername(u.getUsername());

    assertEquals(userDetails.getUsername(),"testUser");

  }

//  @Test
//  public void registerUser() {
//    User u = createUser();
//    RegistrationConfirmationToken token = new RegistrationConfirmationToken();
//
//    when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
////    when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
////    when(userRepository.save(any(User.class))).thenReturn(u);
////    when(registrationTokenRepo.save(any(RegistrationConfirmationToken.class))).thenReturn(token);
//
////    User registeredUser = userDetailsServiceImplementation.registerUser(u);
//
//
////    System.out.println(registeredUser.getUserName());
////    assertEquals(registeredUser.getUserName(), "testUser");
//
//  }

  @Test(expected = EmailAlreadyExistException.class)
  public void register_shouldThrowEmailAlreadyExistException_WhenEmailAlreadyExist() {
    User u = createUser();
    when(userRepository.findAllByEmail(any(String.class))).thenReturn(u);

    userDetailsServiceImplementation.registerUser(u);

  }

  @Test(expected = UserNameAlreadyExistException.class)
  public void register_shouldThrowUserNameAlreadyExistException_WhenUserNameExists(){
    User u = createUser();
    when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(u));

    userDetailsServiceImplementation.registerUser(u);

  }

  @Test
  public void loginUser(){
    User u = createUser();

    when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(u));

    User loginUser = userDetailsServiceImplementation.loginUser(u);

    assertEquals(loginUser.getUsername(), "testUser");

  }

  @Test(expected = UsernameNotFoundException.class)
  public void login_shouldThrowUsernameNotFoundException_WhenWrongUsername(){
    when(userRepository.findByUsername(any(String.class))).thenThrow(new UsernameNotFoundException("userName not found"));

    userDetailsServiceImplementation.loginUser(createUser());

  }

  @Test
  public void createUserByUsername(){
    User u = createUser();
    when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(u));

    User createdUser = userDetailsServiceImplementation.createUserByUsername(u.getUsername());
    assertEquals(createdUser.getUsername(), u.getUsername());
  }

  @Test(expected = UsernameNotFoundException.class)
  public void create_ShouldThrowUsernameNotFoundException(){
    when(userRepository.findByUsername(any(String.class))).thenThrow(new UsernameNotFoundException("userName not found"));

    userDetailsServiceImplementation.loginUser(createUser());

  }

}