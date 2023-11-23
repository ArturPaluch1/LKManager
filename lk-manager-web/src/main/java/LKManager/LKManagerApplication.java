package LKManager;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


@SpringBootApplication
public class LKManagerApplication {


	private static long delay=14l;

	protected static void startTimer()
	{



 LocalDateTime now;
		now= LocalDateTime.now();

		Timer timer = new Timer();

		// long delay = 1000L;



		TimerTask task = new TimerTask()
		{
			int i =0;
			public void run() {
				System.out.println("Task performed on: " + new Date() + "n" +
						"Thread's name: " + Thread.currentThread().getName());
i++;
				URL url = null;
				try {
					url = new URL("https://lkm-fgim.onrender.com/");


				//	url = new URL("https://www.developer.com/java/java-event-listeners/");
				} catch (MalformedURLException e) {
				//	throw new RuntimeException(e);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				URLConnection urlConnection = null;
				try {

					urlConnection = url.openConnection();
					urlConnection.connect();


						//	System.out.println("content= "+	urlConnection.getContent()+"\n");

					BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),
							Charset.forName("UTF-8")));



					System.out.println("i="+ i +"refreshed site ");
				} catch (IOException e) {
				//	throw new RuntimeException(e);
					System.out.println("refresh site failed ");
				}

			/*	Object c= null;
				try {
					c = urlConnection.getContent();
				} catch (IOException e) {
				//	throw new RuntimeException(e);
				}*/

















			}
		};



		//	timer.schedule(task,0,750000);
		timer.schedule(task,0,750000);
		//timer.schedule(task, Date.from(now.plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant()) );










	}


	public static void main(String[] args) {

		SpringApplication.run(LKManagerApplication.class, args);

startTimer();








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
