package org.example;


import org.example.service.ParsingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(Main.class, args);

        ParsingService parsingService = context.getBean(ParsingService.class);
        try{
            parsingService.parseAndSaveAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            context.close();
        }
    }
}