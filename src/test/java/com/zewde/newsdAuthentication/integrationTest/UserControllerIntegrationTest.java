package com.zewde.newsdAuthentication.integrationTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zewde.newsdAuthentication.entities.RegistrationConfirmationToken;
import com.zewde.newsdAuthentication.entities.User;
import com.zewde.newsdAuthentication.service.UserDetailsServiceImplementation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

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


  private void registrationSetupForLogin(User u) throws Exception {
      RegistrationConfirmationToken token =  userService.registerUserAndReturnToken(u);
      userService.confirmUser(token);
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

//    when(userService.registerUser(any(User.class))).thenReturn();

    mockMvc.perform(post("/auth/register")
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE).content(userJSON).with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isCreated());
//        .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("someUser"));
  }

  @Test
  public void registerShouldFail_WhenInvalidCredentials() throws Exception {
    User user = createUser("stesds","somepas","some@email.com");
//    user.setUsername("someUserName");
//    user.setPassword("somepas");
//    user.setEmail("some@email.com");

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
          .andExpect(result ->assertTrue(result.getResolvedException() instanceof ResponseStatusException));
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
        .andExpect(result ->assertTrue(result.getResolvedException() instanceof ResponseStatusException));
  }

  @Test
  public void postLogin() throws Exception {
    User u = createUser("testuser123","tesR123456!","testUser123@email.com");

    try{
      registrationSetupForLogin(u);
    }catch(Exception e){
      System.out.println(e.getMessage());
    }

    String jsonUser = createUserJson(u);

    mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE).content(jsonUser).with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isOk());

  }

  @Test
  public void postLoginShouldFail_WhenWrongCredentials() throws Exception {
    User u = createUser("testuser","tesR123456!","testUser@email.com");

    try{
      registrationSetupForLogin(u);
    }catch(Exception e){
      System.out.println(e.getMessage());
    }

    String jsonUser = createUserJson(createUser("testuser","tesR12","testUser@email.com"));

    mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE).content(jsonUser).with(csrf()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.status().reason("Wrong username or password"));

  }

}