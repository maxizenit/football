package ru.borbotko.football.gui.table;

import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.*;
import ru.borbotko.football.exception.InvalidFieldException;
import ru.borbotko.football.gui.table.filter.EntityTableFilter;
import ru.borbotko.football.gui.table.model.EntityTableModel;

public class EntityTable<T> extends JScrollPane {

  /** Таблица. */
  protected final JTable table;

  /** Модель таблицы. */
  protected final EntityTableModel<T> model;

  public EntityTable() {
    table = null;
    model = null;
  }

  public EntityTable(EntityTableModel<T> model) {
    super();
    this.model = model;
    table = new JTable(model);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setViewportView(table);
    updateUI();
  }

  /**
   * Обновляет таблицу из списка сущностей.
   *
   * @param entities список сущностей
   */
  public void updateEntities(List<T> entities) {
    model.updateEntities(entities);
    updateUI();
  }

  @Override
  public void updateUI() {
    super.updateUI();
    if (table != null) {
      table.updateUI();
    }
  }

  /** Очищает таблицу. */
  public void clear() {
    model.clear();
    updateUI();
  }

  /**
   * Применяет к таблице фильтр.
   *
   * @param filter фильтр
   * @throws InvalidFieldException если поля фильтра заполнены некорректно
   */
  public void filter(EntityTableFilter<T> filter) throws InvalidFieldException {
    if (!filter.isEmpty()) {
      model.filter(filter);
    } else {
      model.clearFilter();
    }

    updateUI();
  }

  /** Очищает фильтр. */
  public void clearFilter() {
    model.clearFilter();
    updateUI();
  }

  /**
   * Возвращает выбранную сущность в таблице.
   *
   * @return выбранная сущность
   */
  public T getSelectedEntity() {
    return model.getEntityByRow(table.getSelectedRow());
  }

  public List<T> getEntities() {
    return model.getEntities();
  }

  @Override
  public synchronized void addMouseListener(MouseListener l) {
    table.addMouseListener(l);
  }
}
