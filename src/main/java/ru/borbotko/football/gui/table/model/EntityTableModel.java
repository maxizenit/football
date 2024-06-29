package ru.borbotko.football.gui.table.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.table.DefaultTableModel;
import org.springframework.util.CollectionUtils;
import ru.borbotko.football.exception.InvalidFieldException;
import ru.borbotko.football.gui.table.filter.EntityTableFilter;

public abstract class EntityTableModel<T> extends DefaultTableModel {

  /** Текущий список сущностей. */
  protected List<T> entities;

  /** Список сущностей до фильтрации. */
  private List<T> oldEntities;

  public EntityTableModel(String[] columnNames) {
    super(null, columnNames);
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    return false;
  }

  /** Заполняет вектор модели векторами объектов для сущности. */
  private void updateDataVector() {
    dataVector.clear();
    entities.forEach(
        e -> {
          Vector<Object> vector = new Vector<>();
          fillVectorFromEntity(vector, e);
          dataVector.add(vector);
        });
  }

  /**
   * Заполняет вектор объектов полями сущности.
   *
   * @param vector вектор
   * @param entity сущность
   */
  protected abstract void fillVectorFromEntity(Vector<Object> vector, T entity);

  /**
   * Возвращает сущность по её индексу.
   *
   * @param index индекс
   * @return сущность
   */
  public T getEntityByRow(int index) {
    try {
      return entities.get(index);
    } catch (NullPointerException | IndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * Обновляет список сущностей.
   *
   * @param entities список сущностей
   */
  public void updateEntities(List<T> entities) {
    this.entities = entities;
    oldEntities = null;
    updateDataVector();
  }

  /** Очищает модель. */
  public void clear() {
    entities = null;
    oldEntities = null;
    dataVector.clear();
  }

  /**
   * Применяет фильтр к модели.
   *
   * @param filter фильтр
   * @throws InvalidFieldException если поля фильтра заполнены некорректно
   */
  public void filter(EntityTableFilter<T> filter) throws InvalidFieldException {
    clearFilter();

    if (CollectionUtils.isEmpty(entities)) {
      return;
    }

    List<T> newEntities = new CopyOnWriteArrayList<>();

    entities.parallelStream()
        .forEach(
            e -> {
              try {
                if (filter.isFiltered(e)) {
                  newEntities.add(e);
                }
              } catch (InvalidFieldException ex) {
                throw new RuntimeException(ex);
              }
            });

    oldEntities = new ArrayList<>(entities);
    entities = newEntities;
    updateDataVector();
  }

  /** Очищает фильтр. */
  public void clearFilter() {
    if (oldEntities != null) {
      updateEntities(oldEntities);
    }
  }

  public List<T> getEntities() {
    return new ArrayList<>(entities);
  }
}
