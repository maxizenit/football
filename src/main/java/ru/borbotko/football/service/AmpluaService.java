package ru.borbotko.football.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.borbotko.football.entity.Amplua;
import ru.borbotko.football.repository.AmpluaRepository;

/** Сервис для работы с позициями. */
@Service
@RequiredArgsConstructor
public class AmpluaService {

  private final AmpluaRepository ampluaRepository;

  /**
   * Сохраняет позицию.
   *
   * @param amplua позиция
   * @return сохранённая позиция
   */
  public Amplua saveAmplua(Amplua amplua) {
    return ampluaRepository.save(amplua);
  }

  /**
   * Сохраняет позиции.
   *
   * @param ampluas позиции
   * @return сохранённые позиции
   */
  public List<Amplua> saveAllAmpluas(List<Amplua> ampluas) {
    return (List<Amplua>) ampluaRepository.saveAll(ampluas);
  }

  /**
   * Возвращает все позиции.
   *
   * @return все позиции
   */
  public List<Amplua> getAllAmpluas() {
    return (List<Amplua>) ampluaRepository.findAll();
  }

  /**
   * Удаляет позицию.
   *
   * @param amplua удаляемая позиция
   */
  public void removeAmplua(Amplua amplua) {
    ampluaRepository.delete(amplua);
  }

  /** Удаляет все позиции. */
  public void removeAllAmpluas() {
    ampluaRepository.deleteAll();
  }
}
