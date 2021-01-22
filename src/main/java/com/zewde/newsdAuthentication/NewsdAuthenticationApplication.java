package com.zewde.newsdAuthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
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
