package io.github.philliptwl.textrpg_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TextRpgSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(TextRpgSpringApplication.class, args);
        Engine.start();
	}
}
