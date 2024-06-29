package ru.borbotko.football.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/** Игрок (футболист). */
@Entity
@Getter
@Setter
public class Player {

  /** Идентификатор. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** Фамилия. */
  @NotNull private String lastName;

  /** Имя. */
  @NotNull private String firstName;

  /** Позиция футболиста (специализация). */
  @ManyToOne
  @JoinColumn(name = "amplua_id")
  private Amplua amplua;
}
