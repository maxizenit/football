package ru.borbotko.football.util.report;

import lombok.Getter;
import lombok.Setter;

/** Датабин для отчёта игроков. */
@Getter
@Setter
public class PlayerDataBean {

  /** Позиция. */
  private String amplua;

  /** Фамилия. */
  private String lastName;

  /** Имя. */
  private String firstName;

  /** Количество матчей. */
  private String matchesCount;

  /** Количество голов. */
  private String goalsCount;
}
