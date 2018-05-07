package com.power.banksy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource({"file:/Users/jaydenwu/Documents/Home/app-dev.properties"})
public class BanksyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BanksyApplication.class, args);
	}
}
