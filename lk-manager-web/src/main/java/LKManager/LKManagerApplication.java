package LKManager;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


@SpringBootApplication
public class LKManagerApplication {

	public static void main(String[] args) {

		SpringApplication.run(LKManagerApplication.class, args);

	}

	@Bean
	ApplicationRunner applicationRunner (Environment environment)
	{
		return  args -> {
			System.out.println(environment.getProperty("msg"));
		};
	}
	@PostConstruct
	public void init() {
		System.out.println("Initialized...");
	}
	@PreDestroy
	public void destroy() {
		System.out.println("Destroying...");
	}

	/*
properties np dla baz danych
https://www.udemy.com/course/spring-framework-5-beginner-to-guru/learn/lecture/25220804#content
https://www.udemy.com/course/spring-framework-5-beginner-to-guru/learn/lecture/25220792#content


	 */

}
