package com.zewde.newsdAuthentication.integrationTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zewde.newsdAuthentication.Exceptions.EmailAlreadyExistException;
import com.zewde.newsdAuthentication.Exceptions.UserNameAlreadyExistException;
import com.zewde.newsdAuthentication.entities.RegistrationConfirmationToken;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {


  @Autowired
  private UserDetailsServiceImplementation userService;


  @Autowired
  private MockMvc mockMvc;

  private String createUserJson(User u) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(u);
    return json;

  }

  private User createUser(){
    User u = new User();
    u.setUsername("someUser6543");
    u.setPassword("Pass123!");
    u.setEmail("some@email.com");
    return u;
  }

  private User createUser(String username, String password, String email){
    User u = new User();
    u.setUsername(username);
    u.setPassword(password);
    u.setEmail(email);
    return u;
  }


  private void registrationSetupForLogin(User u) {
      RegistrationConfirmationToken token =  userService.registerUserAndReturnToken(u);
      userService.confirmUser(token.getToken());
  }

  @Test
  public void getRegister() throws Exception{
    mockMvc.perform(get("/auth/register").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string("please register"));
  }


  @Test
  public void postRegister() throws Exception {
    User user = createUser();

    String userJSON = createUserJson(user);

    mockMvc.perform(post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE).content(userJSON).with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isCreated());
  }

  @Test
  public void registerShouldFail_WhenInvalidCredentials() throws Exception {
    User user = createUser("stesds","somepas","some@email.com");

    String userJson = createUserJson(user);


    mockMvc.perform(post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE).content(userJson).with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(result ->assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException ));

  }

  @Test
  public void registerShouldFail_WhenUsernameAlreadyExist() throws Exception {
    User u = createUser("someUser123","somePass1234!","email@email.com");
    userService.registerUserAndReturnToken(u);

    String jsonU = createUserJson(createUser("someUser123","tererE345@!","email34@ermai.com"));


      mockMvc.perform(post("/auth/register")
          .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE).content(jsonU).with(csrf()))
          .andExpect(MockMvcResultMatchers.status().isBadRequest())
          .andExpect(MockMvcResultMatchers.status().reason("Username already exists"))
          .andExpect(result ->assertTrue(result.getResolvedException() instanceof UserNameAlreadyExistException));
  }

  @Test
  public void registerShouldFail_WhenEmailAlreadyExist() throws Exception {
    User u = createUser("userValue","somePass1234!","some234@email.com");
    userService.registerUserAndReturnToken(u);

    String jsonU = createUserJson(createUser("someusername", "somePassword1234!","some234@email.com"));


    mockMvc.perform(post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE).content(jsonU).with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.status().reason("Email already exists"))
        .andExpect(result ->assertTrue(result.getResolvedException() instanceof EmailAlreadyExistException));
  }


  @Test
  public void postLoginShouldFail_WhenWrongCredentials() throws Exception {
    User u = createUser("testuser","tesR123456!","testUser@email.com");
    registrationSetupForLogin(u);
    User loginUser = new User();
    loginUser.setUsername("testuser");
    loginUser.setPassword("tesR12");
    String jsonUser = createUserJson(loginUser);

    mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE).content(jsonUser).with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadCredentialsException));

  }

  @Test
  public void loginUser() throws Exception {
    User u = createUser("userTest12","test12341!","validTestEmai@email.com");
    String jsonUser = createUserJson(u);
    registrationSetupForLogin(u);

    mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE).content(jsonUser).with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isOk());

  }

}