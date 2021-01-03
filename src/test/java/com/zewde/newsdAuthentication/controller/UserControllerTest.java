package com.zewde.newsdAuthentication.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.results.ResultMatchers;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

  @InjectMocks
  private UserController userController;

  private MockMvc mockMvc;

  private String createJsonUser() throws JSONException {
    JSONObject user = new JSONObject();
    user.put("userName", "someUser");
    user.put("password", "pass");

    return user.toString();
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
        .andExpect(MockMvcResultMatchers.content().string("hello at home"));
  }

  @Test
  public void getRegister() throws Exception{
    mockMvc.perform(MockMvcRequestBuilders.get("/register"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("please register"));
  }

  @Test
  public void postRegister() throws Exception {
    String user = createJsonUser();
    System.out.println(user);
    mockMvc.perform(MockMvcRequestBuilders.post("/register").contentType(MediaType.APPLICATION_JSON).content(user))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("someUser"));
  }

}