package LKManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@SpringBootApplication
public class LKManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LKManagerApplication.class, args);

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
