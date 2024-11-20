package com.wintux.j20241119;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafkaStreams
public class J20241119Application {

	public static void main(String[] args) {
		SpringApplication.run(J20241119Application.class, args);
	}

}
