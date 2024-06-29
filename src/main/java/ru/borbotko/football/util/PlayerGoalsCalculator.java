package ru.borbotko.football.util;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.borbotko.football.entity.PlayerMatchInfo;

/** Калькулятор голов игрока в матче. */
@Component
@RequiredArgsConstructor
public class PlayerGoalsCalculator {

  /**
   * Возвращает количество голов игрока в матче.
   *
   * @param info запись о составе на матч
   * @return количество голов игрока
   */
  public int calculate(List<PlayerMatchInfo> info) {
    return info.stream().mapToInt(PlayerMatchInfo::getGoalsCount).sum();
  }
}
