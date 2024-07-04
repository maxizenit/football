package ru.borbotko.football.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import lombok.SneakyThrows;
import org.bouncycastle.util.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import ru.borbotko.football.entity.Amplua;
import ru.borbotko.football.entity.Match;
import ru.borbotko.football.entity.Player;
import ru.borbotko.football.entity.PlayerMatchInfo;
import ru.borbotko.football.service.PlayerMatchInfoService;
import ru.borbotko.football.util.report.PlayerReportGenerator;

public class PlayerReportGeneratorTests {

  private static final String EXPECTED_FILE_NAME = "reportExample.html";
  private static final String ACTUAL_FILE_NAME = "report.html";

  private static Player firstPlayer;
  private static Player secondPlayer;

  private static Match firstMatch;
  private static Match secondMatch;

  private static PlayerReportGenerator reportGenerator;
  private static PlayerGoalsCalculator playerGoalsCalculator;
  private static PlayerMatchInfoService playerMatchInfoService;

  @BeforeAll
  public static void setup() {
    firstPlayer = new Player();
    Amplua firstPlayerAmplua = new Amplua();
    firstPlayerAmplua.setDescription("Форвард");
    firstPlayer.setAmplua(firstPlayerAmplua);
    firstPlayer.setLastName("Иванов");
    firstPlayer.setFirstName("Иван");

    secondPlayer = new Player();
    Amplua secondPlayerAmplua = new Amplua();
    secondPlayerAmplua.setDescription("Центральный защитник");
    secondPlayer.setAmplua(secondPlayerAmplua);
    secondPlayer.setLastName("Петров");
    secondPlayer.setFirstName("Сергей");

    firstMatch = new Match();
    secondMatch = new Match();

    playerGoalsCalculator = new PlayerGoalsCalculator();
    playerMatchInfoService = Mockito.mock(PlayerMatchInfoService.class);

    reportGenerator = new PlayerReportGenerator(playerGoalsCalculator, playerMatchInfoService);

    Mockito.when(playerMatchInfoService.getInfoByPlayer(firstPlayer))
        .thenReturn(getFirstPlayerMatchInfos());
    Mockito.when(playerMatchInfoService.getInfoByPlayer(secondPlayer))
        .thenReturn(getSecondPlayerMatchInfos());
  }

  private static List<PlayerMatchInfo> getFirstPlayerMatchInfos() {
    PlayerMatchInfo info1 = new PlayerMatchInfo();
    info1.setPlayer(firstPlayer);
    info1.setMatch(secondMatch);
    info1.setGoalsCount(3);

    return List.of(info1);
  }

  private static List<PlayerMatchInfo> getSecondPlayerMatchInfos() {
    PlayerMatchInfo info1 = new PlayerMatchInfo();
    info1.setPlayer(secondPlayer);
    info1.setMatch(firstMatch);
    info1.setGoalsCount(1);

    PlayerMatchInfo info2 = new PlayerMatchInfo();
    info2.setPlayer(secondPlayer);
    info2.setMatch(secondMatch);
    info2.setGoalsCount(0);

    return List.of(info1, info2);
  }

  @SneakyThrows
  @Test
  public void generateTests() {
    reportGenerator.generate(new File(ACTUAL_FILE_NAME), List.of(firstPlayer, secondPlayer));

    String expected =
        new String(
            StreamUtils.copyToByteArray(
                new ClassPathResource(EXPECTED_FILE_NAME).getInputStream())).replace("\r", "");
    String actual = new String(Files.readAllBytes(Paths.get(ACTUAL_FILE_NAME))).replace("\r", "");
    Files.delete(Paths.get(ACTUAL_FILE_NAME));

    Assertions.assertEquals(expected, actual);
  }
}
