package ru.borbotko.football.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.borbotko.football.entity.Match;
import ru.borbotko.football.entity.Player;
import ru.borbotko.football.entity.PlayerMatchInfo;
import ru.borbotko.football.repository.PlayerMatchInfoRepository;

/** Сервис для работы с составами на матч. */
@Service
@RequiredArgsConstructor
public class PlayerMatchInfoService {

  private final PlayerMatchInfoRepository playerMatchInfoRepository;

  /**
   * Добавляет игрока в состав на матч.
   *
   * @param player игрок
   * @param match матч
   * @return запись о составе на матч
   */
  public PlayerMatchInfo addPlayerInMatch(Player player, Match match) {
    PlayerMatchInfo info = new PlayerMatchInfo();
    info.setId(new PlayerMatchInfo.PlayerMatchInfoId(player.getId(), match.getId()));
    info.setPlayer(player);
    info.setMatch(match);
    info.setGoalsCount(0);

    return playerMatchInfoRepository.save(info);
  }

  /**
   * Добавляет гол игроку в определённом матче.
   *
   * @param player игрок
   * @param match матч
   * @return запись о составе на матч
   */
  public PlayerMatchInfo addGoal(Player player, Match match) {
    PlayerMatchInfo info =
        playerMatchInfoRepository
            .findById(new PlayerMatchInfo.PlayerMatchInfoId(player.getId(), match.getId()))
            .get();
    info.setGoalsCount(info.getGoalsCount() + 1);
    return playerMatchInfoRepository.save(info);
  }

  /**
   * Удаляет гол игрока в определённом матче.
   *
   * @param player игрок
   * @param match матч
   * @return запись о составе на матч
   */
  public PlayerMatchInfo removeGoal(Player player, Match match) {
    PlayerMatchInfo info =
        playerMatchInfoRepository
            .findById(new PlayerMatchInfo.PlayerMatchInfoId(player.getId(), match.getId()))
            .get();
    info.setGoalsCount(Math.max(info.getGoalsCount() - 1, 0));
    return playerMatchInfoRepository.save(info);
  }

  /**
   * Удаляет игрока из состава на матч.
   *
   * @param player игрок
   * @param match матч
   */
  public void removePlayerFromMatch(Player player, Match match) {
    playerMatchInfoRepository.deleteById(
        new PlayerMatchInfo.PlayerMatchInfoId(player.getId(), match.getId()));
  }

  /**
   * Возвращает записи состава по матчу.
   *
   * @param match матч
   * @return записи состава по матчу
   */
  public List<PlayerMatchInfo> getInfoByMatch(Match match) {
    return playerMatchInfoRepository.findByMatch(match);
  }

  /**
   * Возвращает записи состава по игроку.
   *
   * @param player игрок
   * @return записи состава по игроку
   */
  public List<PlayerMatchInfo> getInfoByPlayer(Player player) {
    return playerMatchInfoRepository.findByPlayer(player);
  }
}
