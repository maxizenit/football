package ru.borbotko.football.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.borbotko.football.entity.Match;

public class MatchScoreConverterTests {

  private static MatchScoreConverter converter;

  @BeforeAll
  public static void setup() {
    converter = new MatchScoreConverter();
  }

  @Test
  public void convertTests() {
    Match match = new Match();
    Assertions.assertEquals("-:-", converter.convert(match));

    match.setOpponentTeamGoals(2);
    Assertions.assertEquals("-:-", converter.convert(match));

    match.setOwnTeamGoals(3);
    Assertions.assertEquals("3:2", converter.convert(match));

    match.setOpponentTeamGoals(null);
    Assertions.assertEquals("-:-", converter.convert(match));
  }
}
