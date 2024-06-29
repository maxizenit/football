package ru.borbotko.football.util;

import org.springframework.stereotype.Component;
import ru.borbotko.football.entity.Match;

/** Конвертер счёта матча. */
@Component
public class MatchScoreConverter {

  /**
   * Возвращает счёт матча в виде строки "голы_команды_администратора:голы_соперника".
   *
   * @param match матч
   * @return счёт матча в виде строки
   */
  public String convert(Match match) {
    if (match.getOwnTeamGoals() == null || match.getOpponentTeamGoals() == null) {
      return "-:-";
    }

    return String.format("%d:%d", match.getOwnTeamGoals(), match.getOpponentTeamGoals());
  }
}
