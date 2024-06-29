package ru.borbotko.football.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.borbotko.football.entity.Player;
import ru.borbotko.football.repository.PlayerRepository;

/** Сервис для работы с игроками. */
@Service
@RequiredArgsConstructor
public class PlayerService {

  private final PlayerRepository playerRepository;

  /**
   * Сохраняет игрока.
   *
   * @param player игрок
   * @return сохранённый игрок
   */
  public Player savePlayer(Player player) {
    return playerRepository.save(player);
  }

  /**
   * Возвращает всех игроков.
   *
   * @return все игроки
   */
  public List<Player> getAllPlayers() {
    return (List<Player>) playerRepository.findAll();
  }

  /**
   * Удаляет игрока.
   *
   * @param player удаляемый игрок
   */
  public void removePlayer(Player player) {
    playerRepository.delete(player);
  }
}
