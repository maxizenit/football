package ru.borbotko.football;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(
    classes = FootballApplication.class,
    loader = FootballApplicationTests.CustomContextLoader.class)
class FootballApplicationTests {

  public static class CustomContextLoader extends SpringBootContextLoader {

    @Override
    protected SpringApplication getSpringApplication() {
      return new SpringApplicationBuilder().headless(false).build();
    }
  }

  @Test
  void contextLoads() {
    // данный тест проверяет, что классы приложения загружаются корректно - это происходит
    // автоматически
  }
}
