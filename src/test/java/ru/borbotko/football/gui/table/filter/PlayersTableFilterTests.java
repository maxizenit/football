package ru.borbotko.football.gui.table.filter;

import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.borbotko.football.entity.Amplua;
import ru.borbotko.football.entity.Match;
import ru.borbotko.football.entity.Player;
import ru.borbotko.football.entity.PlayerMatchInfo;
import ru.borbotko.football.service.PlayerMatchInfoService;
import ru.borbotko.football.util.PlayerGoalsCalculator;

public class PlayersTableFilterTests {

  private static PlayerGoalsCalculator playerGoalsCalculator;
  private static PlayerMatchInfoService playerMatchInfoService;
  private static Player player;
  private static Amplua playerAmplua;
  private static Amplua otherAmplua;

  @BeforeAll
  public static void setup() {
    playerGoalsCalculator = new PlayerGoalsCalculator();
    playerMatchInfoService = Mockito.mock(PlayerMatchInfoService.class);

    player = new Player();
    playerAmplua = new Amplua();
    playerAmplua.setId(1);
    player.setAmplua(playerAmplua);

    otherAmplua = new Amplua();
    otherAmplua.setId(2);

    Mockito.when(playerMatchInfoService.getInfoByPlayer(player)).thenReturn(getPlayerMatchInfos());
  }

  private static List<PlayerMatchInfo> getPlayerMatchInfos() {
    Match match1 = new Match();
    Match match2 = new Match();

    PlayerMatchInfo info1 = new PlayerMatchInfo();
    info1.setPlayer(player);
    info1.setMatch(match1);
    info1.setGoalsCount(1);

    PlayerMatchInfo info2 = new PlayerMatchInfo();
    info2.setPlayer(player);
    info2.setMatch(match2);
    info2.setGoalsCount(3);

    return List.of(info1, info2);
  }

  @SneakyThrows
  @Test
  public void isFilteredTests() {
    PlayersTableFilter filter =
        new PlayersTableFilter(playerGoalsCalculator, playerMatchInfoService);

    // в фильтре указана не та позиция, которая у игрока
    filter.setAmplua(otherAmplua);
    Assertions.assertFalse(filter.isFiltered(player));

    // в фильтре указана та же позиция, что и у игрока
    filter.setAmplua(playerAmplua);
    Assertions.assertTrue(filter.isFiltered(player));

    // минимум матчей в фильтре больше, чем сыграл игрок, минимум голов - нет
    filter.setMinMatchesCount(10);
    filter.setMinGoalsCount(2);
    Assertions.assertFalse(filter.isFiltered(player));

    // минимум матчей в фильтре меньше, чем сыграл игрок
    filter.setMinMatchesCount(1);
    Assertions.assertTrue(filter.isFiltered(player));

    // минимумы соответствуют фактическим значениям
    filter.setMinMatchesCount(2);
    filter.setMinGoalsCount(4);
    Assertions.assertTrue(filter.isFiltered(player));
  }

  @Test
  public void isEmptyTests() {
    PlayersTableFilter filter =
        new PlayersTableFilter(playerGoalsCalculator, playerMatchInfoService);
    // только что созданный фильтр пустой
    Assertions.assertTrue(filter.isEmpty());

    // в фильтр установлено ненулевое значение - не пуст
    filter.setMinMatchesCount(2);
    Assertions.assertFalse(filter.isEmpty());

    // в фильтр установлено нулевое значение для другого параметра, но из-за первого фильтр всё ещё
    // не пуст
    filter.setMinGoalsCount(0);
    Assertions.assertFalse(filter.isEmpty());

    // оба числовых параметра фильтра нулевые - фильтр пуст
    filter.setMinMatchesCount(0);
    Assertions.assertTrue(filter.isEmpty());

    // оба числовых параметра null - фильтр пуст
    filter.setMinMatchesCount(null);
    filter.setMinGoalsCount(null);
    Assertions.assertTrue(filter.isEmpty());
  }
}
