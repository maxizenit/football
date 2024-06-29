package ru.borbotko.football.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.borbotko.football.entity.Match;
import ru.borbotko.football.repository.MatchRepository;

/** Сервис для работы с матчами. */
@Service
@RequiredArgsConstructor
public class MatchService {

  private final MatchRepository matchRepository;

  /**
   * Сохраняет матч.
   *
   * @param match матч
   * @return сохранённый матч
   */
  public Match saveMatch(Match match) {
    return matchRepository.save(match);
  }

  /**
   * Возвращает все матчи.
   *
   * @return все матчи
   */
  public List<Match> getAllMatches() {
    return (List<Match>) matchRepository.findAll();
  }

  /**
   * Удаляет матч.
   *
   * @param match удаляемый матч
   */
  public void removeMatch(Match match) {
    matchRepository.delete(match);
  }
}
