package com.zewde.newsdAuthentication.service;

import com.zewde.newsdAuthentication.Exceptions.EmailAlreadyExistException;
import com.zewde.newsdAuthentication.Exceptions.UserNameAlreadyExistException;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplementationTest {

  @InjectMocks
  public UserDetailsServiceImplementation userDetailsServiceImplementation;

  @Mock
  private UserRepository userRepository;

  @Test
  public void loadUserByUsername() {

  }

  @Test
  public void registerUser() {
    User u = new User();
    u.setPassword("testPass");
    u.setEmail("test@email.com");
    u.setUserName("testUser");

    when(userRepository.findAllByEmail(any(String.class))).thenReturn(null);
    when(userRepository.findByUserName(any(String.class))).thenReturn(Optional.empty());

    when(userRepository.save(any(User.class))).thenReturn(u);

    User registeredUser = userDetailsServiceImplementation.registerUser(u);


    System.out.println(registeredUser.getUserName());
    assertEquals(registeredUser.getUserName(), "testUser");

  }

  @Test(expected = EmailAlreadyExistException.class)
  public void shouldThrowEmailAlreadyExistExceptionEmailAlreadyExist() {
    User u = new User();
    u.setPassword("testPass");
    u.setEmail("test@email.com");
    u.setUserName("testUser");

    when(userRepository.findAllByEmail(any(String.class))).thenReturn(u);

    userDetailsServiceImplementation.registerUser(u);

  }

  @Test(expected = UserNameAlreadyExistException.class)
  public void shouldThrowUserNameAlreadyExistExceptionWhenUserNameExists(){
    User u = new User();
    u.setPassword("testPass");
    u.setEmail("test@email.com");
    u.setUserName("testUser");

    when(userRepository.findByUserName(any(String.class))).thenReturn(Optional.of(u));

    userDetailsServiceImplementation.registerUser(u);

  }
}