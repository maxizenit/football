package ru.borbotko.football.gui.table.model;

import java.util.Vector;
import ru.borbotko.football.entity.Player;

public class PlayerShortTableModel extends EntityTableModel<Player> {

  /** Названия столбцов. */
  private static final String[] COLUMN_NAMES = {"Амплуа", "Фамилия", "Имя"};

  public PlayerShortTableModel() {
    super(COLUMN_NAMES);
  }

  @Override
  protected void fillVectorFromEntity(Vector<Object> vector, Player entity) {
    vector.add(entity.getAmplua() != null ? entity.getAmplua().getCode() : "");
    vector.add(entity.getLastName());
    vector.add(entity.getFirstName());
  }
}
