package com.zewde.newsdAuthentication;

import com.zewde.newsdAuthentication.controller.ArticleController;
import com.zewde.newsdAuthentication.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

//applicaiton context is set by default to the NewsdAuthenticationApplication with the properties
//defined in application-test.properties -> by adding the ActiveProfile
@SpringBootTest
@ActiveProfiles("test")
public class NewsdAuthenticationApplicationTests {

	@Autowired
	private UserController userController;

	@Autowired
	private ArticleController articleController;


	@Test
	void contextLoads() {
		assertThat(userController).isNotNull();
		assertThat(articleController).isNotNull();
	}



}
