package com.zewde.newsdAuthentication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zewde.newsdAuthentication.Exceptions.EmailAlreadyExistException;
import com.zewde.newsdAuthentication.Exceptions.UserNameAlreadyExistException;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import com.zewde.newsdAuthentication.utils.CookiesUtils;
import com.zewde.newsdAuthentication.utils.JWTTokenUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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

  @Mock
  private CookiesUtils cookiesUtils;

  private MockMvc mockMvc;

  private String createUserJson(User u) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(u);
    return json;

  }

  private User createUser(){
    User u = new User();
    u.setUsername("someUser");
    u.setPassword("Pass123!");
    u.setEmail("some@email.com");
    u.setActive(true);
    return u;
  }



  @Before
  public void setUp() throws Exception {
    mockMvc = MockMvcBuilders.standaloneSetup(userController)
        .build();
  }


  @Test
  public void getRegister() throws Exception{
    mockMvc.perform(MockMvcRequestBuilders.get("/register").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string("please register"));
  }

  @Test
  public void postRegister() throws Exception {
    User user = createUser();

    String userJSON = createUserJson(user);

//    when(userService.registerUser(any(User.class))).thenReturn();

    mockMvc.perform(MockMvcRequestBuilders.post("/register")
        .contentType(MediaType.APPLICATION_JSON).content(userJSON))
        .andExpect(MockMvcResultMatchers.status().isCreated());
//        .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("someUser"));
  }

  @Test
  public void shouldThrowErrorWhenNoValidCredentials()throws Exception {
    User user = new User();
    user.setUsername("");
    user.setPassword("somepass");
    user.setEmail("");
    String userJSON = createUserJson(user);

    mockMvc.perform(MockMvcRequestBuilders.post("/register")
        .contentType(MediaType.APPLICATION_JSON).content(userJSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(result ->assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException ));
  }

  @Test
  public void shouldThrowEmailAlreadyExistException_WhenUserTryToRegisterWithExistingEmail() throws Exception {
    when(userService.registerUser(any(User.class))).thenThrow(new EmailAlreadyExistException());

    User user = createUser();
    String userJSON = createUserJson(user);

    mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(userJSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.status().reason("Email already exists"))
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));

  }

  @Test
  public void shouldThrowUserNameAlreadyExistsException_WhenUsernameAlreadyExists() throws Exception{
    when(userService.registerUser(any(User.class))).thenThrow(new UserNameAlreadyExistException());

    User user = createUser();
    String userJSON = createUserJson(user);

    mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(userJSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.status().reason("Username already exists"))
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));

  }

  @Test
  public void postLogin() throws Exception {
    User u = createUser();
    String json = createUserJson(u);
    Authentication auth = mock(Authentication.class);
    Cookie cookie = new Cookie("jwtToken", "some value");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
    when(userService.loginUser(any(User.class))).thenReturn(u);
    when(jwtTokenUtils.generateToken(any(String.class))).thenReturn("some token");
    when(cookiesUtils.createCookie(any(String.class), any(String.class))).thenReturn(cookie);

    mockMvc.perform(MockMvcRequestBuilders.post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.cookie().exists("jwtToken"));
  }


  @Test(expected = ResponseStatusException.class)
  public void shouldThrowBadCredentialsException_WhenCredentialsAreWrong(){
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Bad creds"));
    User user = createUser();
    MockHttpServletResponse mockResponse = new MockHttpServletResponse();

    userController.loginUser(user, mockResponse);

    verify(userService, never()).loginUser(user);

  }

  @Test(expected = ResponseStatusException.class)
  public void shouldThrowDisabledException_WhenUserIsDisabled(){
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new DisabledException("User disabled"));
    User user = createUser();
    MockHttpServletResponse mockResponse = new MockHttpServletResponse();

    userController.loginUser(user, mockResponse);

    verify(userService, never()).loginUser(user);

  }

}