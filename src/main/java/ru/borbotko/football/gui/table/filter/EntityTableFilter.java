package ru.borbotko.football.gui.table.filter;

import ru.borbotko.football.exception.InvalidFieldException;

public interface EntityTableFilter<T> {

  boolean isFiltered(T t) throws InvalidFieldException;

  boolean isEmpty();
}
