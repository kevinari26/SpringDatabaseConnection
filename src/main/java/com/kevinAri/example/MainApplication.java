package com.kevinAri.example;

import com.kevinAri.example.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Autowired
	AppService appService;

	@Override
	public void run(String... args) {
		appService.execute();
//		appService.execute2();
	}

}
