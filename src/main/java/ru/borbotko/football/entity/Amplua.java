package ru.borbotko.football.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/** Позциия футболиста (специализация). */
@Entity
@Getter
@Setter
public class Amplua {

  /** Идентификатор. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** Краткий код (аббревиатура). */
  @NotNull private String code;

  /** Описание. */
  @NotNull private String description;

  @Override
  public String toString() {
    return code;
  }
}
