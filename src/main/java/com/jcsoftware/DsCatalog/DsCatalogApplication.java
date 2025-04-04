package com.jcsoftware.DsCatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//public class DsCatalogApplication implements CommandLineRunner {
public class DsCatalogApplication  {

	/*
	@Autowired
	private PasswordEncoder passwordEncoder;
	*/
	public static void main(String[] args) {
		SpringApplication.run(DsCatalogApplication.class, args);
	}
/*
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("ENCODE= " + passwordEncoder.encode("123456"));
	}
*/
}
