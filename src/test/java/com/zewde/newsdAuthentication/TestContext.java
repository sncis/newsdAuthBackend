package com.zewde.newsdAuthentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(value="com.zewde.newsdAuthentication.repositories")
public class TestContext {

}
