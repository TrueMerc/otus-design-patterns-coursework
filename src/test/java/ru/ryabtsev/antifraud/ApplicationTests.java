package ru.ryabtsev.antifraud;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = DatabaseContextInitializer.class)
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
