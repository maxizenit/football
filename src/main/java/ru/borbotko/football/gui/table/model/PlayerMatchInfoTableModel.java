package ru.borbotko.football.gui.table.model;

import java.util.Vector;
import ru.borbotko.football.entity.PlayerMatchInfo;

public class PlayerMatchInfoTableModel extends EntityTableModel<PlayerMatchInfo> {

  /** Названия столбцов. */
  private static final String[] COLUMN_NAMES = {"Амплуа", "Фамилия", "Имя", "Голы"};

  public PlayerMatchInfoTableModel() {
    super(COLUMN_NAMES);
  }

  @Override
  protected void fillVectorFromEntity(Vector<Object> vector, PlayerMatchInfo entity) {
    vector.add(
        entity.getPlayer().getAmplua() != null ? entity.getPlayer().getAmplua().getCode() : "");
    vector.add(entity.getPlayer().getLastName());
    vector.add(entity.getPlayer().getFirstName());
    vector.add(entity.getGoalsCount());
  }
}
