package com.zewde.newsdAuthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EntityScan(basePackages = {"com.zewde.newsdAuthentication"})  // scan JPA entities manually
public class NewsdAuthenticationApplication {

	public static void main(String[] args) {
		try{
			SpringApplication.run(NewsdAuthenticationApplication.class, args);
			System.out.println("Application is running!");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}
