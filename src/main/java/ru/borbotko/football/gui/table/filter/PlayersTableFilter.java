package ru.borbotko.football.gui.table.filter;

import java.util.ArrayList;
import java.util.List;
import lombok.Setter;
import ru.borbotko.football.entity.Amplua;
import ru.borbotko.football.entity.Player;
import ru.borbotko.football.exception.InvalidFieldException;
import ru.borbotko.football.service.PlayerMatchInfoService;
import ru.borbotko.football.util.PlayerGoalsCalculator;

@Setter
public class PlayersTableFilter implements EntityTableFilter<Player> {

  private final PlayerGoalsCalculator playerGoalsCalculator;
  private final PlayerMatchInfoService playerMatchInfoService;

  private Amplua amplua;
  private Integer minMatchesCount;
  private Integer minGoalsCount;

  public PlayersTableFilter(
      PlayerGoalsCalculator playerGoalsCalculator, PlayerMatchInfoService playerMatchInfoService) {
    this.playerGoalsCalculator = playerGoalsCalculator;
    this.playerMatchInfoService = playerMatchInfoService;
  }

  @Override
  public boolean isFiltered(Player player) throws InvalidFieldException {
    List<String> invalidFields = new ArrayList<>();

    if (minMatchesCount != null && minMatchesCount < 0) {
      invalidFields.add("минимум матчей");
    }
    if (minGoalsCount != null && minGoalsCount < 0) {
      invalidFields.add("минимум голов");
    }

    if (!invalidFields.isEmpty()) {
      throw new InvalidFieldException(invalidFields);
    }

    if (minMatchesCount != null
        && playerMatchInfoService.getInfoByPlayer(player).size() < minMatchesCount) {
      return false;
    }
    if (minGoalsCount != null
        && playerGoalsCalculator.calculate(playerMatchInfoService.getInfoByPlayer(player))
            < minGoalsCount) {
      return false;
    }
    if (amplua != null) {
      if (player.getAmplua() == null) {
        return false;
      } else {
        return amplua.getId().equals(player.getAmplua().getId());
      }
    }

    return true;
  }

  @Override
  public boolean isEmpty() {
    return amplua == null
        && (minMatchesCount == null || minMatchesCount == 0)
        && (minGoalsCount == null || minGoalsCount == 0);
  }
}
