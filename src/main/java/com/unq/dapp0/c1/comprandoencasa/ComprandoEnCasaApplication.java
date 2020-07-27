package com.unq.dapp0.c1.comprandoencasa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.unq.dapp0.c1.comprandoencasa.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@RestController
@EnableConfigurationProperties(AppProperties.class)
public class ComprandoEnCasaApplication {

	@Autowired
	private ObjectMapper objectMapper;

	public static void main(String[] args) {
		SpringApplication.run(ComprandoEnCasaApplication.class, args);
	}

	@CrossOrigin
	@GetMapping("/")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	@PostConstruct
	public void setUp() {
		JavaTimeModule module = new JavaTimeModule();
		LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
		objectMapper.registerModule(module);
	}

}
