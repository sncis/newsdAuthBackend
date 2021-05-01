package com.zewde.newsdAuthentication.unitTests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zewde.newsdAuthentication.Exceptions.*;
import com.zewde.newsdAuthentication.controller.UserController;
import com.zewde.newsdAuthentication.entities.RegistrationConfirmationToken;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.service.EmailService;
import com.zewde.newsdAuthentication.service.RegistrationConfirmationTokenService;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
@WebMvcTest
@ActiveProfiles("test")
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

  @Mock
  private RegistrationConfirmationTokenService registrationConfirmationTokenService;

  @Mock
  private EmailService emailService;

  @Mock
  private SimpleMailMessage mail;


  private MockMvc mockMvc;

  private User user;
  private String jsonUser;


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
    mockMvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(new MyControllerAdvice())
        .build();

    user = createUser();
    jsonUser = createUserJson(user);

  }



  @Test
  public void getRegister() throws Exception{
    mockMvc.perform(get("/auth/register").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string("please register"));
  }

  @Test
  public void postRegister() throws Exception {
    RegistrationConfirmationToken token = new RegistrationConfirmationToken(user);
    when(userService.registerUserAndReturnToken(any(User.class))).thenReturn(token);
    when(mail.getText()).thenReturn("some tex");

    mockMvc.perform(post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE).content(jsonUser).with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isCreated());
//        .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("someUser"));
  }

  @Test
  public void resendConfirmationToken() throws Exception {
    RegistrationConfirmationToken token = new RegistrationConfirmationToken(user);
    when(userService.findTokenByUserEmail(any(String.class))).thenReturn(token);
    when(mail.getText()).thenReturn("some tex");

    mockMvc.perform(post("/auth/resendConfirmationToken").contentType(MediaType.APPLICATION_JSON_VALUE).content("email: email@some.de").with(csrf()))
      .andExpect(MockMvcResultMatchers.status().isOk());

  }

  @Test
  public void resendConfirmationTokeShouldThrowError_whenInvalidEmail() throws Exception {
    when(userService.findTokenByUserEmail(any(String.class))).thenThrow(new UsernameNotFoundException("some error"));

    mockMvc.perform(post("/auth/resendConfirmationToken").contentType(MediaType.APPLICATION_JSON_VALUE).content("email: email@some.de").with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isNotFound());

  }

  @Test
  public void shouldThrowErrorWhenNoValidCredentials()throws Exception {

    mockMvc.perform(post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(jsonUser).with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(result ->assertTrue(result.getResolvedException() instanceof ResponseStatusException ));
  }

  @Test
  public void shouldThrowEmailAlreadyExistException_WhenUserTryToRegisterWithExistingEmail() throws Exception {
    when(userService.registerUserAndReturnToken(any(User.class))).thenThrow(new EmailAlreadyExistException());

//    User user = createUser();
//    String userJSON = createUserJson(user);

    mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(jsonUser).with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.status().reason("Email already exists"))
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));

  }

  @Test
  public void shouldThrowUserNameAlreadyExistsException_WhenUsernameAlreadyExists() throws Exception{
    when(userService.registerUserAndReturnToken(any(User.class))).thenThrow(new UserNameAlreadyExistException());

    mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(jsonUser).with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.status().reason("Username already exists"))
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
  }

  @Test
  public void postLogin() throws Exception {
    Authentication auth = mock(Authentication.class);
    Cookie cookie = new Cookie("jwtToken", "some value");

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
    when(auth.getName()).thenReturn("soemuser");
    when(jwtTokenUtils.generateToken(any(String.class))).thenReturn("some token");
    when(cookiesUtils.createCookie(any(String.class), any(String.class))).thenReturn(cookie);

    mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(jsonUser).with(csrf())).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.cookie().exists("jwtToken"));
  }


//  @Test(expected = BadCredentialsException.class)
  @Test
  public void shouldThrowBadCredentialsException_WhenCredentialsAreWrong() throws Exception {
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Bad creds"));

    mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(jsonUser).with(csrf())).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(result -> assertEquals(result.getResponse().getContentAsString(),"Wrong username or password"));

  }

  @Test
  public void shouldThrowDisabledException_WhenUserIsDisabled() throws Exception {
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new DisabledException(""));

    mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(jsonUser).with(csrf())).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andExpect(result -> assertEquals(result.getResponse().getContentAsString(),"Your account is disabled."));
  }

  @Test
  public void shouldThrowUserLoginBlockedException_WhenUserIsBlocked() throws Exception {
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new UserLoginBlockedException());

    mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(jsonUser).with(csrf())).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isTooManyRequests())
        .andExpect(result -> assertEquals(result.getResponse().getContentAsString(),"Your account is blocked due to too many failed login attempts. You can try it again in 5 minutes"));
  }

  @Test
  public void confirmUser() throws Exception {
    doNothing().when(userService).confirmUser(any(String.class));

    mockMvc.perform(MockMvcRequestBuilders.get("/auth/confirm?token=someToken")
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).with(csrf())).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void shouldThrowURegistrationConfirmationTokenNotFoundException_WhenWrongToken() throws Exception {
    doThrow( new RegistrationConfirmationTokenNotFoundException()).when(userService).confirmUser(any(String.class));

    mockMvc.perform(MockMvcRequestBuilders.get("/auth/confirm?token=InvalidToken")
        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).with(csrf())).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(result -> assertEquals(result.getResponse().getContentAsString(),"Your account is disabled. Please confirm your Registration."));
  }

//  @Test
//  public void resendConfirmationToken() throws Exception {
//    doThrow( new RegistrationConfirmationTokenNotFoundException()).when(userService).confirmUser(any(String.class));
//
//    mockMvc.perform(MockMvcRequestBuilders.get("/auth/confirm?token=InvalidToken")
//        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).with(csrf())).andDo(print())
//        .andExpect(MockMvcResultMatchers.status().isBadRequest())
//        .andExpect(result -> assertEquals(result.getResponse().getContentAsString(),"Your account is disabled. Please confirm your Registration."));
//  }

}