package ru.borbotko.football.gui.table.model;

import java.text.SimpleDateFormat;
import java.util.Vector;
import ru.borbotko.football.entity.Match;
import ru.borbotko.football.util.MatchScoreConverter;

public class MatchTableModel extends EntityTableModel<Match> {

  /** Названия столбцов. */
  private static final String[] COLUMN_NAMES = {"Дата и время", "Соперник", "Счёт"};

  private final MatchScoreConverter matchScoreConverter;

  public MatchTableModel(MatchScoreConverter matchScoreConverter) {
    super(COLUMN_NAMES);
    this.matchScoreConverter = matchScoreConverter;
  }

  @Override
  protected void fillVectorFromEntity(Vector<Object> vector, Match entity) {
    vector.add(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(entity.getTimestamp()));
    vector.add(entity.getOpponentName());
    vector.add(matchScoreConverter.convert(entity));
  }
}
