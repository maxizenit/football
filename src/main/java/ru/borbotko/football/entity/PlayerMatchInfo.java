package ru.borbotko.football.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Информация об участии игрока в матче. */
@Entity
@Getter
@Setter
public class PlayerMatchInfo {

  /** Составной ключ (из идентификатора игрока и идентификатора матча). */
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Embeddable
  public static class PlayerMatchInfoId implements Serializable {

    /** Идентификатор игрока. */
    private Integer playerId;

    /** Идентификатор матча. */
    private Integer matchId;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      PlayerMatchInfoId that = (PlayerMatchInfoId) o;
      return Objects.equals(playerId, that.playerId) && Objects.equals(matchId, that.matchId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(playerId, matchId);
    }
  }

  /** Идентификатор. */
  @EmbeddedId private PlayerMatchInfoId id;

  /** Игрок. */
  @MapsId("playerId")
  @ManyToOne
  @JoinColumn(name = "player_id")
  private Player player;

  /** Матч. */
  @MapsId("matchId")
  @ManyToOne
  @JoinColumn(name = "match_id")
  private Match match;

  /** Количество голов, забитых игроком в матче. */
  @NotNull private Integer goalsCount;
}
