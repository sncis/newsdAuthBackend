package com.zewde.newsdAuthentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EntityScan(basePackages = {"com.zewde.newsdAuthentication"})  // scan JPA entities manually
public class NewsdAuthenticationApplication {
	private final static Logger logger = LoggerFactory.getLogger(NewsdAuthenticationApplication.class);

	public static void main(String[] args) {
		try{
			SpringApplication.run(NewsdAuthenticationApplication.class, args);
			logger.info("-----------------------NewsdAuthenticationApplication---------------------------");
			logger.info("APPLICATION IS RUNNING!");

			logger.info("--------------------------------------------------------------------------------");

		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}

}
