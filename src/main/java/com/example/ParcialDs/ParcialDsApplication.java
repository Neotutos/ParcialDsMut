package com.example.ParcialDs;

import com.example.ParcialDs.Repository.DnaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ParcialDsApplication {
	private static final Logger logger = LoggerFactory.getLogger(ParcialDsApplication.class);

	@Autowired
	private DnaRepository mutantRepository;

	public static void main(String[] args) {
		SpringApplication.run(ParcialDsApplication.class, args);
		System.out.println("funcionando");
	}
}