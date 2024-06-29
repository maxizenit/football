package ru.borbotko.football.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import ru.borbotko.football.entity.Match;
import ru.borbotko.football.entity.Player;
import ru.borbotko.football.entity.PlayerMatchInfo;

/** Репозиторий для информации об участии игроков в матчах. */
public interface PlayerMatchInfoRepository
    extends CrudRepository<PlayerMatchInfo, PlayerMatchInfo.PlayerMatchInfoId> {

  List<PlayerMatchInfo> findByMatch(Match match);

  List<PlayerMatchInfo> findByPlayer(Player player);
}
