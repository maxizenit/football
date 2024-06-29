package ru.borbotko.football.repository;

import org.springframework.data.repository.CrudRepository;
import ru.borbotko.football.entity.Match;

/** Репозиторий для матчей. */
public interface MatchRepository extends CrudRepository<Match, Integer> {}
