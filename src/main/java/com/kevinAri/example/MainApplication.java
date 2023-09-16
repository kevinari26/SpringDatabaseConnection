package com.kevinAri.example;

import com.kevinAri.example.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

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
	}

}
