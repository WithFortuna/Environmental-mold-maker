package com.example.moldMaker;

import com.example.moldMaker.service.StlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class MoldMakerApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(MoldMakerApplication.class, args);
/*
		StlService stlService=new StlService();
		stlService.processTest();
*/

	}

}
