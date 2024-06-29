package ru.borbotko.football.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import ru.borbotko.football.entity.Amplua;
import ru.borbotko.football.util.xmlhandler.AmpluaXMLHandler;

public class AmpluaXMLHandlerTests {

  private static AmpluaXMLHandler ampluaXMLHandler;

  private static final String EXAMPLE_FILE_NAME = "xmlExample.xml";
  private static final String ACTUAL_FILE_NAME = "xml.xml";

  @BeforeAll
  public static void setup() {
    ampluaXMLHandler = new AmpluaXMLHandler();
  }

  private List<Amplua> getExampleAmpluas() {
    Amplua amplua1 = new Amplua();
    amplua1.setCode("ФРВ");
    amplua1.setDescription("Форвард");

    Amplua amplua2 = new Amplua();
    amplua2.setCode("ЦЗ");
    amplua2.setDescription("Центральный защитник");

    return List.of(amplua1, amplua2);
  }

  @SneakyThrows
  @Test
  public void loadFromXMLTests() {
    List<Amplua> expected = getExampleAmpluas();
    List<Amplua> actual =
        ampluaXMLHandler.loadFromXML(new ClassPathResource(EXAMPLE_FILE_NAME).getFile());

    Assertions.assertAll(
        () -> {
          Amplua expectedAmplua1 = expected.get(0);
          Amplua actualAmplua1 = actual.get(0);

          Assertions.assertEquals(expectedAmplua1.getCode(), actualAmplua1.getCode());
          Assertions.assertEquals(expectedAmplua1.getDescription(), actualAmplua1.getDescription());
        },
        () -> {
          Amplua expectedAmplua2 = expected.get(1);
          Amplua actualAmplua2 = actual.get(1);

          Assertions.assertEquals(expectedAmplua2.getCode(), actualAmplua2.getCode());
          Assertions.assertEquals(expectedAmplua2.getDescription(), actualAmplua2.getDescription());
        });
  }

  @SneakyThrows
  @Test
  public void saveToXMLTests() {
    ampluaXMLHandler.saveToXML(new File(ACTUAL_FILE_NAME), getExampleAmpluas());

    String expected =
        new String(
            StreamUtils.copyToByteArray(new ClassPathResource(EXAMPLE_FILE_NAME).getInputStream()));
    String actual = new String(Files.readAllBytes(Paths.get(ACTUAL_FILE_NAME)));
    Files.delete(Paths.get(ACTUAL_FILE_NAME));

    Assertions.assertEquals(expected, actual);
  }
}
