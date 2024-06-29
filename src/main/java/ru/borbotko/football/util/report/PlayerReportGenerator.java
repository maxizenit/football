package ru.borbotko.football.util.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.borbotko.football.entity.Player;
import ru.borbotko.football.service.PlayerMatchInfoService;
import ru.borbotko.football.util.PlayerGoalsCalculator;

/** Генератор HTML-отчёта JasperReports для игроков. */
@Component
public class PlayerReportGenerator extends ReportGenerator<Player, PlayerDataBean> {

  /** Файл с шаблоном отчёта. */
  private static final Resource REPORT_PATTERN = new ClassPathResource("playerreport.jrxml");

  private final PlayerGoalsCalculator playerGoalsCalculator;
  private final PlayerMatchInfoService playerMatchInfoService;

  @Autowired
  public PlayerReportGenerator(
      PlayerGoalsCalculator playerGoalsCalculator, PlayerMatchInfoService playerMatchInfoService) {
    super(REPORT_PATTERN);
    this.playerGoalsCalculator = playerGoalsCalculator;
    this.playerMatchInfoService = playerMatchInfoService;
  }

  @Override
  protected PlayerDataBean createDataBeanFromEntity(Player entity) {
    PlayerDataBean bean = new PlayerDataBean();

    bean.setAmplua(entity.getAmplua() != null ? entity.getAmplua().getDescription() : "");
    bean.setLastName(entity.getLastName());
    bean.setFirstName(entity.getFirstName());
    bean.setMatchesCount(String.valueOf(playerMatchInfoService.getInfoByPlayer(entity).size()));
    bean.setGoalsCount(
        String.valueOf(
            playerGoalsCalculator.calculate(playerMatchInfoService.getInfoByPlayer(entity))));

    return bean;
  }
}
