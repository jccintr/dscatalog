package com.jcsoftware.DsCatalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//public class DsCatalogApplication implements CommandLineRunner {
public class DsCatalogApplication  {

	/*
	@Autowired
	private PasswordEncoder passwordEncoder;
	*/
	@Autowired
	//private S3Service s3service;
	
	public static void main(String[] args) {
		SpringApplication.run(DsCatalogApplication.class, args);
	}

	/*
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println("ENCODE= " + passwordEncoder.encode("123456"));
		s3service.uploadTest("c:\\temp\\teste.jpg");
	}
*/
}
