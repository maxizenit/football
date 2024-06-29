package ru.borbotko.football.gui.table.model;

import java.util.Vector;
import ru.borbotko.football.entity.Amplua;

public class AmpluaTableModel extends EntityTableModel<Amplua> {

  /** Названия столбцов. */
  private static final String[] COLUMN_NAMES = {"Код", "Описание"};

  public AmpluaTableModel() {
    super(COLUMN_NAMES);
  }

  @Override
  protected void fillVectorFromEntity(Vector<Object> vector, Amplua entity) {
    vector.add(entity.getCode());
    vector.add(entity.getDescription());
  }
}
