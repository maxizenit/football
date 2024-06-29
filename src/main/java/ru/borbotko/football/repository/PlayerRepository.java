package ru.borbotko.football.repository;

import org.springframework.data.repository.CrudRepository;
import ru.borbotko.football.entity.Player;

/** Репозиторий для игроков. */
public interface PlayerRepository extends CrudRepository<Player, Integer> {}
