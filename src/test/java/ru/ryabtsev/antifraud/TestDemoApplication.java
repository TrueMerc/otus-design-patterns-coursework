package ru.ryabtsev.antifraud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = DatabaseContextInitializer.class)
public class TestDemoApplication extends DatabaseContextInitializer {

	public static void main(String[] args) {
		SpringApplication.from(Application::main).with(TestDemoApplication.class).run(args);
	}

}
