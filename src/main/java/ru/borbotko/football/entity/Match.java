package ru.borbotko.football.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

/** Матч. */
@Entity
@Getter
@Setter
public class Match {

  /** Идентификатор. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** Дата и время проведения. */
  @NotNull private Timestamp timestamp;

  /** Название команды соперника. */
  @NotNull private String opponentName;

  /** Голы команды администратора. */
  private Integer ownTeamGoals;

  /** Голы команды соперника. */
  private Integer opponentTeamGoals;
}
