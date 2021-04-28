package com.zewde.newsdAuthentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NewsdAuthenticationApplication {
	private final static Logger logger = LoggerFactory.getLogger(NewsdAuthenticationApplication.class);

	public static void main(String[] args) {
		try{
			SpringApplication.run(NewsdAuthenticationApplication.class, args);
			System.out.println("-----------------------NewsdAuthenticationApplication---------------------------\n");
			System.out.println("                        APPLICATION IS RUNNING!\n");
			System.out.println("--------------------------------------------------------------------------------\n");

		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}


}
