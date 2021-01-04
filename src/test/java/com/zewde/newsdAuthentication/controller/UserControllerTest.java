package com.zewde.newsdAuthentication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zewde.newsdAuthentication.Exceptions.EmailAlreadyExistException;
import com.zewde.newsdAuthentication.Exceptions.UserNameAlreadyExistException;
import com.zewde.newsdAuthentication.entities.MyUserDetails;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;

import com.zewde.newsdAuthentication.utils.JWTTokenUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

  @InjectMocks
  private UserController userController;

  @Mock
  private UserDetailsServiceImplementation userService;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private JWTTokenUtils jwtTokenUtils;

  private MockMvc mockMvc;

  private String createUserJson(User u) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(u);
    return json;

  }

  private User createUser(){
    User u = new User();
    u.setUserName("someUser");
    u.setPassword("pass");
    u.setActive(true);
    return u;
  }



  @Before
  public void setUp() throws Exception {
    mockMvc = MockMvcBuilders.standaloneSetup(userController)
        .build();
  }

  @Test
  public void home() throws Exception{
    mockMvc.perform(MockMvcRequestBuilders.get("/home"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string("hello at home"));
  }

  @Test
  public void getRegister() throws Exception{
    mockMvc.perform(MockMvcRequestBuilders.get("/register"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string("please register"));
  }

  @Test
  public void postRegister() throws Exception {
    User user = createUser();

    String userJSON = createUserJson(user);

    when(userService.registerUser(any(User.class))).thenReturn(user);

    mockMvc.perform(MockMvcRequestBuilders.post("/register")
        .contentType(MediaType.APPLICATION_JSON).content(userJSON))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("someUser"));
  }

  @Test
  public void shouldThrowEmailAlreadyExistExceptionIfUserTryToRegisterWithExistingEmail() throws Exception {
    when(userService.registerUser(any(User.class))).thenThrow(new EmailAlreadyExistException());

    User user = createUser();
    String userJSON = createUserJson(user);

    mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(userJSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.status().reason("Email already exists"))
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof EmailAlreadyExistException));

  }

  @Test
  public void shouldThrowUserNameAlreadyExistsExceptionWhenUserNameAlreadyExists() throws Exception{
    when(userService.registerUser(any(User.class))).thenThrow(new UserNameAlreadyExistException());

    User user = createUser();
    String userJSON = createUserJson(user);

    mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(userJSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.status().reason("Username already exists"))
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNameAlreadyExistException));

  }

  @Test
  public void postLogin() throws Exception {
    User u = createUser();
    String json = createUserJson(u);
    Authentication auth = mock(Authentication.class);
    MyUserDetails userDetails = new MyUserDetails(u);

    when(authenticationManager.authenticate(Matchers.any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
    when(userService.loginUser(any(User.class))).thenReturn(userDetails);
    when(jwtTokenUtils.generateToken(any(String.class))).thenReturn("some token");

    mockMvc.perform(MockMvcRequestBuilders.post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.jwtToken").value("some token"));
  }


  @Test(expected = BadCredentialsException.class)
  public void shouldThrowBadCredentialsExceptionIfCredentialsAreWrong(){
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Bad creds"));
    User user = createUser();

    userController.loginUser(user);

    verify(userService, never()).loginUser(user);

  }

  @Test(expected = DisabledException.class)
  public void shouldThrowDisabledExceptionIfUserIsDisabled(){
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new DisabledException("User disabled"));
    User user = createUser();

    userController.loginUser(user);

    verify(userService, never()).loginUser(user);

  }

}