package ru.borbotko.football;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class FootballApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(FootballApplication.class).headless(false).run(args);
  }
}
