package ru.borbotko.football.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.borbotko.football.entity.Match;
import ru.borbotko.football.entity.Player;
import ru.borbotko.football.entity.PlayerMatchInfo;

public class PlayerGoalsCalculatorTests {

  private static PlayerGoalsCalculator calculator;

  @BeforeAll
  public static void setup() {
    calculator = new PlayerGoalsCalculator();
  }

  @Test
  public void calculateTests() {
    Match match1 = new Match();
    match1.setTimestamp(new Timestamp(System.currentTimeMillis()));
    match1.setOpponentName("Оппонент1");

    Match match2 = new Match();
    match2.setTimestamp(new Timestamp(System.currentTimeMillis()));
    match2.setOpponentName("Оппонент2");

    Match match3 = new Match();
    match2.setTimestamp(new Timestamp(System.currentTimeMillis()));
    match2.setOpponentName("Оппонент3");

    Player player = new Player();
    player.setLastName("Иванов");
    player.setFirstName("Иван");

    List<PlayerMatchInfo> infos = new ArrayList<>();

    // игрок сыграл в первом матче и не забил голов
    PlayerMatchInfo info1 = new PlayerMatchInfo();
    info1.setPlayer(player);
    info1.setMatch(match1);
    info1.setGoalsCount(0);
    infos.add(info1);
    Assertions.assertEquals(0, calculator.calculate(infos));

    // игрок сыграл во втором матче и забил 2 гола (2 в итоге)
    PlayerMatchInfo info2 = new PlayerMatchInfo();
    info2.setPlayer(player);
    info2.setMatch(match2);
    info2.setGoalsCount(2);
    infos.add(info2);
    Assertions.assertEquals(2, calculator.calculate(infos));

    // игрок сыграл в третьем матче и забил 3 гола (5 в итоге)
    PlayerMatchInfo info3 = new PlayerMatchInfo();
    info3.setPlayer(player);
    info3.setMatch(match3);
    info3.setGoalsCount(3);
    infos.add(info3);
    Assertions.assertEquals(5, calculator.calculate(infos));
  }
}
