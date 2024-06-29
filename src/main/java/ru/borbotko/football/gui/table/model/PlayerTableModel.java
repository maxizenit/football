package ru.borbotko.football.gui.table.model;

import java.util.Vector;
import ru.borbotko.football.entity.Player;
import ru.borbotko.football.service.PlayerMatchInfoService;
import ru.borbotko.football.util.PlayerGoalsCalculator;

public class PlayerTableModel extends EntityTableModel<Player> {

  /** Названия столбцов. */
  private static final String[] COLUMN_NAMES = {"Амплуа", "Фамилия", "Имя", "Матчи", "Голы"};

  private final PlayerGoalsCalculator playerGoalsCalculator;
  private final PlayerMatchInfoService playerMatchInfoService;

  public PlayerTableModel(
      PlayerGoalsCalculator playerGoalsCalculator, PlayerMatchInfoService playerMatchInfoService) {
    super(COLUMN_NAMES);
    this.playerGoalsCalculator = playerGoalsCalculator;
    this.playerMatchInfoService = playerMatchInfoService;
  }

  @Override
  protected void fillVectorFromEntity(Vector<Object> vector, Player entity) {
    vector.add(entity.getAmplua() != null ? entity.getAmplua().getCode() : "");
    vector.add(entity.getLastName());
    vector.add(entity.getFirstName());
    vector.add(playerMatchInfoService.getInfoByPlayer(entity).size());
    vector.add(playerGoalsCalculator.calculate(playerMatchInfoService.getInfoByPlayer(entity)));
  }
}
