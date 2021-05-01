package com.zewde.newsdAuthentication.service;

import com.zewde.newsdAuthentication.Exceptions.EmailAlreadyExistException;
import com.zewde.newsdAuthentication.Exceptions.RegistrationConfirmationTokenNotFoundException;
import com.zewde.newsdAuthentication.Exceptions.UserLoginBlockedException;
import com.zewde.newsdAuthentication.Exceptions.UserNameAlreadyExistException;
import com.zewde.newsdAuthentication.entities.RegistrationConfirmationToken;
import com.zewde.newsdAuthentication.entities.User;
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
import static org.junit.Assert.assertNotEquals;
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
  private LoginFailureService loginFailureService;

  @Mock
  private BCryptPasswordEncoder passwordEncoder;

  @Mock
  private RegistrationConfirmationTokenService registrationTokenService;

  private User createUser(){
    User u = new User();
    u.setPassword("testPass");
    u.setEmail("test@email.com");
    u.setUsername("testUser");
    u.setId(1);

    return u;
  }

  @Test
  public void loadUserByUsername() {
    User u = createUser();
    when(loginFailureService.getIpAddress()).thenReturn("12345");
    when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(u));

    User userDetails = userDetailsServiceImplementation.loadUserByUsername(u.getUsername());

    assertEquals(userDetails.getUsername(),"testUser");

  }


  @Test(expected = UserLoginBlockedException.class)
  public void loadUserByUsername_shouldThrowUserIsBlockedExpection_whenUserIsblocke(){
    User u = createUser();
    when(loginFailureService.getIpAddress()).thenReturn("1234");
    when(loginFailureService.isBlocked(any(String.class))).thenReturn(true);

    userDetailsServiceImplementation.loadUserByUsername(u.getUsername());

  }

  @Test(expected = UsernameNotFoundException.class)
  public void loadUserByUsername_shouldThrowUsernameNotFoundException_whenUserNotFound(){
    User u = createUser();
    when(loginFailureService.getIpAddress()).thenReturn("1234");
    when(userRepository.findByUsername(any(String.class))).thenThrow(UsernameNotFoundException.class);


    userDetailsServiceImplementation.loadUserByUsername(u.getUsername());

  }

  @Test
  public void findUserIdByUsername(){
    User u = createUser();
    when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(u));

    assertEquals(userDetailsServiceImplementation.findUserIdByUsername(u.getUsername()),1);

  }

  @Test(expected = UsernameNotFoundException.class)
  public void findUserIdByUsername_shouldThrowUsernameNotFoundException(){
    User u = createUser();
    when(userRepository.findByUsername(any(String.class))).thenThrow((UsernameNotFoundException.class));
    userDetailsServiceImplementation.findUserIdByUsername(u.getUsername());

  }

  @Test
  public void createUserByUsername(){
    User u = createUser();
    when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(u));

    User createdUser = userDetailsServiceImplementation.createUserByUsername(u.getUsername());
    assertEquals(createdUser.getUsername(), u.getUsername());
  }

  @Test(expected = UsernameNotFoundException.class)
  public void createUserByUsername_shouldThrowUsernameNotFoundException(){
    User u = createUser();
    when(userRepository.findByUsername(any(String.class))).thenThrow((UsernameNotFoundException.class));

   userDetailsServiceImplementation.createUserByUsername(u.getUsername());
  }

  @Test(expected = EmailAlreadyExistException.class)
  public void registerUser_shouldThrowEmailAlreadyExistException() {
    User u = createUser();
    when(userRepository.findAllByEmail(any(String.class))).thenReturn(Optional.of(u));

    userDetailsServiceImplementation.registerUserAndReturnToken(u);

  }

  @Test(expected = UserNameAlreadyExistException.class)
  public void registerUser_shouldThrowUserNameAlreadyExistException(){
    User u = createUser();
    when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(u));

    userDetailsServiceImplementation.registerUserAndReturnToken(u);
  }

  @Test
  public void registerUserAndReturnToken(){
    User u = createUser();
    when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
    when(userRepository.findAllByEmail(any(String.class))).thenReturn(Optional.empty());

    assertNotEquals(userDetailsServiceImplementation.registerUserAndReturnToken(u),  null);
  }

  @Test(expected =RegistrationConfirmationTokenNotFoundException.class)
  public void confirmUser_shouldThrowRegisterConfirmationException(){
    User u = createUser();
    when(registrationTokenService.getToken(any(String.class))).thenThrow(new RegistrationConfirmationTokenNotFoundException());

    userDetailsServiceImplementation.confirmUser("sometoken");

  }
  @Test
  public void confirmUser(){
    User u = createUser();
    RegistrationConfirmationToken token = new RegistrationConfirmationToken(u);

    when(registrationTokenService.getToken(any(String.class))).thenReturn(token);

    userDetailsServiceImplementation.confirmUser("sometoken");

  }

  @Test
  public void findTokenByUserEmail(){
    User u = createUser();

    when(userRepository.findUserByEmail(any(String.class))).thenReturn(Optional.of(u));
    RegistrationConfirmationToken token = new RegistrationConfirmationToken(u);

    when(registrationTokenService.findTokenByUser(any(Optional.class))).thenReturn(token);

    assertEquals(userDetailsServiceImplementation.findTokenByUserEmail(u.getEmail()), token);


  }

  @Test(expected =UsernameNotFoundException.class)
  public void findTokenByUserEmail_shouldThrowUsernameNotfoundException(){
    User u = createUser();

    when(userRepository.findUserByEmail(any(String.class))).thenThrow(new UsernameNotFoundException(""));
//    RegistrationConfirmationToken token = new RegistrationConfirmationToken(u);
//
//    when(registrationTokenService.findTokenByUser(any(Optional.class))).thenReturn(token);

    userDetailsServiceImplementation.findTokenByUserEmail(u.getEmail());


  }

//  @Test
//  public void createUserByUsername(){
//    User u = createUser();
//    when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(u));
//
//    assertEquals(userDetailsServiceImplementation.createUserByUsername(u.getUsername()),u);
//
//  }
//
//  @Test(expected = UsernameNotFoundException.class)
//  public void findUserIdByUsername_shouldThrowUsernameNotFoundException(){
//    User u = createUser();
//    when(userRepository.findByUsername(any(String.class))).thenThrow((UsernameNotFoundException.class));
//    userDetailsServiceImplementation.findUserIdByUsername(u.getUsername());
//
//  }




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

//  @Test(expected = EmailAlreadyExistException.class)
//  public void register_shouldThrowEmailAlreadyExistException_WhenEmailAlreadyExist() {
//    User u = createUser();
//    when(userRepository.findAllByEmail(any(String.class))).thenReturn(Optional.of(u));
//
//    userDetailsServiceImplementation.registerUser(u);
//
//  }

//  @Test(expected = UserNameAlreadyExistException.class)
//  public void register_shouldThrowUserNameAlreadyExistException_WhenUserNameExists(){
//    User u = createUser();
//    when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(u));
//
//    userDetailsServiceImplementation.registerUser(u);
//
//  }




//  @Test
//  public void loginUser(){
//    User u = createUser();
//    when(loginFailureService.getIpAddress()).thenReturn("12345");
//
//    when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(u));
//
//    String loginUser = userDetailsServiceImplementation.loginUser(u);
//
//    assertEquals(loginUser, "testUser");
//
//  }

//  @Test(expected = UsernameNotFoundException.class)
//  public void login_shouldThrowUsernameNotFoundException_WhenWrongUsername(){
//    when(loginFailureService.getIpAddress()).thenReturn("12345");
//
//    when(userRepository.findByUsername(any(String.class))).thenThrow(new UsernameNotFoundException("userName not found"));
//
//    userDetailsServiceImplementation.loginUser(createUser());
//
//  }

//  @Test
//  public void createUserByUsername(){
//    User u = createUser();
//    when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(u));
//
//    User createdUser = userDetailsServiceImplementation.createUserByUsername(u.getUsername());
//    assertEquals(createdUser.getUsername(), u.getUsername());
//  }

//  @Test(expected = UsernameNotFoundException.class)
//  public void create_ShouldThrowUsernameNotFoundException(){
//    when(loginFailureService.getIpAddress()).thenReturn("12345");
//
//    when(userRepository.findByUsername(any(String.class))).thenThrow(new UsernameNotFoundException("userName not found"));
//
//    userDetailsServiceImplementation.loginUser(createUser());
//
//  }

}