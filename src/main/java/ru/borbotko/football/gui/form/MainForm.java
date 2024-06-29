package ru.borbotko.football.gui.form;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xml.sax.SAXException;
import ru.borbotko.football.entity.Amplua;
import ru.borbotko.football.entity.Match;
import ru.borbotko.football.entity.Player;
import ru.borbotko.football.entity.PlayerMatchInfo;
import ru.borbotko.football.exception.InvalidFieldException;
import ru.borbotko.football.gui.MouseClickedListener;
import ru.borbotko.football.gui.dialog.AmpluaDialog;
import ru.borbotko.football.gui.dialog.MatchDialog;
import ru.borbotko.football.gui.dialog.PlayerDialog;
import ru.borbotko.football.gui.table.EntityTable;
import ru.borbotko.football.gui.table.filter.PlayersTableFilter;
import ru.borbotko.football.gui.table.model.*;
import ru.borbotko.football.service.AmpluaService;
import ru.borbotko.football.service.MatchService;
import ru.borbotko.football.service.PlayerMatchInfoService;
import ru.borbotko.football.service.PlayerService;
import ru.borbotko.football.util.MatchScoreConverter;
import ru.borbotko.football.util.PlayerGoalsCalculator;
import ru.borbotko.football.util.report.PlayerReportGenerator;
import ru.borbotko.football.util.xmlhandler.AmpluaXMLHandler;

@Component
public class MainForm extends JFrame {

  // сервисы для работы с данными
  private final AmpluaService ampluaService;
  private final MatchService matchService;
  private final PlayerService playerService;
  private final PlayerMatchInfoService playerMatchInfoService;

  // генератор отчёта JasperReports
  private final PlayerReportGenerator playerReportGenerator;

  // обработчик XML
  private final AmpluaXMLHandler ampluaXmlHandler;

  // калькулятор голов
  private final PlayerGoalsCalculator playerGoalsCalculator;

  // конвертер счёта матча в строку
  private final MatchScoreConverter matchScoreConverter;

  // корневая панель
  private JPanel rootPanel;

  // таблица игроков
  private EntityTable<Player> playersTable;
  private JButton addPlayerButton;
  private JButton editPlayerButton;
  private JButton removePlayerButton;
  private JButton addAmpluaButton;
  private JButton editAmpluaButton;
  private JButton removeAmpluaButton;
  private JButton createPlayersReportButton;

  // фильтр игроков
  private JComboBox<Amplua> ampluasPlayersFilterCombo;
  private JSpinner playersFilterMatchesCountSpinner;
  private JSpinner playersFilterGoalsCountSpinner;
  private JButton playersFilterButton;
  private JButton playersFilterClearButton;

  // таблица матчей
  private EntityTable<Match> matchesTable;
  private JButton addMatchButton;
  private JButton editMatchButton;
  private JButton removeMatchButton;
  private EntityTable<PlayerMatchInfo> playersInMatchTable;
  private JButton removePlayerFromMatchButton;
  private JButton addGoalButton;
  private JButton removeGoalButton;
  private EntityTable<Player> playersNotInMatchTable;
  private JButton addPlayerInMatchButton;

  // таблица позиций
  private EntityTable<Amplua> ampluasTable;
  private JButton addAmpluasFromXmlButton;
  private JButton saveAmpluasToXmlButton;
  private JButton replaceAmpluasFromXmlButton;

  @Autowired
  public MainForm(
      AmpluaService ampluaService,
      MatchService matchService,
      PlayerService playerService,
      PlayerMatchInfoService playerMatchInfoService,
      PlayerReportGenerator playerReportGenerator,
      AmpluaXMLHandler ampluaXmlHandler,
      PlayerGoalsCalculator playerGoalsCalculator,
      MatchScoreConverter matchScoreConverter) {
    this.ampluaService = ampluaService;
    this.matchService = matchService;
    this.playerService = playerService;
    this.playerMatchInfoService = playerMatchInfoService;
    this.playerReportGenerator = playerReportGenerator;
    this.ampluaXmlHandler = ampluaXmlHandler;
    this.playerGoalsCalculator = playerGoalsCalculator;
    this.matchScoreConverter = matchScoreConverter;

    $$$setupUI$$$();
    addButtonsListeners();
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setContentPane(rootPanel);
    setMinimumSize(new Dimension(600, 400));
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private void createUIComponents() {
    playersTable =
        new EntityTable<>(new PlayerTableModel(playerGoalsCalculator, playerMatchInfoService));
    updatePlayersTable();

    matchesTable = new EntityTable<>(new MatchTableModel(matchScoreConverter));
    matchesTable.addMouseListener(
        new MouseClickedListener() {
          @Override
          public void mouseClicked(MouseEvent e) {
            updatePlayersInMatchTable();
            updatePlayersNotInMatchTable();
          }
        });
    updateMatchesTable();

    playersInMatchTable = new EntityTable<>(new PlayerMatchInfoTableModel());

    playersNotInMatchTable = new EntityTable<>(new PlayerShortTableModel());

    ampluasTable = new EntityTable<>(new AmpluaTableModel());
    updateAmpluasTable();

    ampluasPlayersFilterCombo = new JComboBox<>();
    updateAmpluasCombo();

    playersFilterMatchesCountSpinner =
        new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
    playersFilterGoalsCountSpinner =
        new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
  }

  private void showMessageDialog(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  private void addButtonsListeners() {
    addPlayersTableButtonsListeners();
    addPlayersFilterButtonsListeners();
    addMatchesTableButtonsListeners();
    addAmpluasTableButtonsListeners();
  }

  private void addPlayersTableButtonsListeners() {
    addPlayerButton.addActionListener(
        a ->
            new PlayerDialog(
                this,
                playerService::savePlayer,
                () -> {
                  updatePlayersTable();
                  updatePlayersInMatchTable();
                  updatePlayersNotInMatchTable();
                },
                null,
                ampluaService.getAllAmpluas()));

    editPlayerButton.addActionListener(
        a -> {
          Player player = playersTable.getSelectedEntity();
          if (player != null) {
            new PlayerDialog(
                this,
                playerService::savePlayer,
                () -> {
                  updatePlayersTable();
                  updatePlayersInMatchTable();
                  updatePlayersNotInMatchTable();
                },
                player,
                ampluaService.getAllAmpluas());
          }
        });

    removePlayerButton.addActionListener(
        a -> {
          Player player = playersTable.getSelectedEntity();
          if (player != null) {
            playerService.removePlayer(player);
            updatePlayersTable();
            updatePlayersInMatchTable();
            updatePlayersNotInMatchTable();
          }
        });

    JFileChooser chooser = new JFileChooser();
    createPlayersReportButton.addActionListener(
        a -> {
          int result = chooser.showSaveDialog(this);
          if (result == JFileChooser.APPROVE_OPTION) {
            try {
              playerReportGenerator.generate(chooser.getSelectedFile(), playersTable.getEntities());
            } catch (JRException | IOException e) {
              showMessageDialog(e.getMessage());
            }
          }
        });
  }

  private void addPlayersFilterButtonsListeners() {
    playersFilterButton.addActionListener(
        a -> {
          PlayersTableFilter filter =
              new PlayersTableFilter(playerGoalsCalculator, playerMatchInfoService);
          filter.setAmplua((Amplua) ampluasPlayersFilterCombo.getSelectedItem());
          filter.setMinMatchesCount((Integer) playersFilterMatchesCountSpinner.getValue());
          filter.setMinGoalsCount((Integer) playersFilterGoalsCountSpinner.getValue());

          try {
            playersTable.filter(filter);
          } catch (InvalidFieldException e) {
            showMessageDialog(e.getMessage());
          }
        });

    playersFilterClearButton.addActionListener(a -> clearPlayersFilter());
  }

  private void addMatchesTableButtonsListeners() {
    addMatchButton.addActionListener(
        a -> new MatchDialog(this, matchService::saveMatch, this::updateMatchesTable, null));

    editMatchButton.addActionListener(
        a -> {
          Match match = matchesTable.getSelectedEntity();

          if (match != null) {
            new MatchDialog(this, matchService::saveMatch, this::updateMatchesTable, match);
          }
        });

    removeMatchButton.addActionListener(
        a -> {
          Match match = matchesTable.getSelectedEntity();

          if (match != null) {
            matchService.removeMatch(match);
            updateMatchesTable();
            updatePlayersTable();
            updatePlayersInMatchTable();
            updatePlayersNotInMatchTable();
          }
        });

    removePlayerFromMatchButton.addActionListener(
        a -> {
          Match match = matchesTable.getSelectedEntity();
          PlayerMatchInfo info = playersInMatchTable.getSelectedEntity();

          if (match != null && info != null) {
            playerMatchInfoService.removePlayerFromMatch(info.getPlayer(), info.getMatch());

            updatePlayersTable();
            updatePlayersInMatchTable();
            updatePlayersNotInMatchTable();
          }
        });

    addGoalButton.addActionListener(
        a -> {
          Match match = matchesTable.getSelectedEntity();
          PlayerMatchInfo info = playersInMatchTable.getSelectedEntity();

          if (match != null && info != null) {
            playerMatchInfoService.addGoal(info.getPlayer(), info.getMatch());

            updatePlayersTable();
            updatePlayersInMatchTable();
          }
        });

    removeGoalButton.addActionListener(
        a -> {
          Match match = matchesTable.getSelectedEntity();
          PlayerMatchInfo info = playersInMatchTable.getSelectedEntity();

          if (match != null && info != null) {
            playerMatchInfoService.removeGoal(info.getPlayer(), info.getMatch());

            updatePlayersTable();
            updatePlayersInMatchTable();
          }
        });

    addPlayerInMatchButton.addActionListener(
        a -> {
          Match match = matchesTable.getSelectedEntity();
          Player player = playersNotInMatchTable.getSelectedEntity();

          if (match != null && player != null) {
            playerMatchInfoService.addPlayerInMatch(player, match);

            updatePlayersTable();
            updatePlayersInMatchTable();
            updatePlayersNotInMatchTable();
          }
        });
  }

  private void addAmpluasTableButtonsListeners() {
    addAmpluaButton.addActionListener(
        a ->
            new AmpluaDialog(
                this,
                ampluaService::saveAmplua,
                () -> {
                  updateAmpluasTable();
                  updateAmpluasCombo();
                },
                null));

    editAmpluaButton.addActionListener(
        a -> {
          Amplua amplua = ampluasTable.getSelectedEntity();

          if (amplua != null) {
            new AmpluaDialog(
                this,
                ampluaService::saveAmplua,
                () -> {
                  updateAmpluasTable();
                  updateAmpluasCombo();
                  updatePlayersTable();
                  updatePlayersInMatchTable();
                  updatePlayersNotInMatchTable();
                },
                amplua);
          }
        });

    removeAmpluaButton.addActionListener(
        a -> {
          Amplua amplua = ampluasTable.getSelectedEntity();

          if (amplua != null) {
            ampluaService.removeAmplua(amplua);
            updateAmpluasTable();
            updateAmpluasCombo();
            updatePlayersTable();
            updatePlayersInMatchTable();
            updatePlayersNotInMatchTable();
          }
        });

    JFileChooser chooser = new JFileChooser();
    addAmpluasFromXmlButton.addActionListener(
        a -> {
          int result = chooser.showOpenDialog(this);
          if (result == JFileChooser.APPROVE_OPTION) {
            try {
              List<Amplua> ampluasFromXml = ampluaXmlHandler.loadFromXML(chooser.getSelectedFile());
              if (!CollectionUtils.isEmpty(ampluasFromXml)) {
                ampluaService.saveAllAmpluas(ampluasFromXml);
                updateAmpluasTable();
                updateAmpluasCombo();
              }
            } catch (IOException | SAXException e) {
              showMessageDialog(e.getMessage());
            }
          }
        });

    saveAmpluasToXmlButton.addActionListener(
        a -> {
          int result = chooser.showSaveDialog(this);
          if (result == JFileChooser.APPROVE_OPTION) {
            ampluaXmlHandler.saveToXML(chooser.getSelectedFile(), ampluaService.getAllAmpluas());
          }
        });

    replaceAmpluasFromXmlButton.addActionListener(
        a -> {
          int result = chooser.showOpenDialog(this);
          if (result == JFileChooser.APPROVE_OPTION) {
            List<Amplua> ampluas = new ArrayList<>();

            Thread removeThread = new Thread(ampluaService::removeAllAmpluas);
            Thread loadThread =
                new Thread(
                    () -> {
                      try {
                        ampluas.addAll(ampluaXmlHandler.loadFromXML(chooser.getSelectedFile()));
                      } catch (IOException | SAXException e) {
                        SwingUtilities.invokeLater(() -> showMessageDialog(e.getMessage()));
                      }
                    });
            Thread saveThread = new Thread(() -> ampluaService.saveAllAmpluas(ampluas));

            try {
              removeThread.start();
              loadThread.start();

              removeThread.join();
              loadThread.join();

              saveThread.start();
              saveThread.join();
            } catch (InterruptedException e) {
              removeThread.interrupt();
              loadThread.interrupt();
              saveThread.interrupt();

              SwingUtilities.invokeLater(() -> showMessageDialog(e.getMessage()));
            } finally {
              updateAmpluasTable();
              updateAmpluasCombo();
              updatePlayersTable();
              updatePlayersInMatchTable();
              updatePlayersNotInMatchTable();
            }
          }
        });
  }

  private void updatePlayersTable() {
    playersTable.updateEntities(playerService.getAllPlayers());
  }

  private void updateMatchesTable() {
    matchesTable.updateEntities(matchService.getAllMatches());
  }

  private void updatePlayersInMatchTable() {
    Match match = matchesTable.getSelectedEntity();

    if (match != null) {
      playersInMatchTable.updateEntities(playerMatchInfoService.getInfoByMatch(match));
    } else {
      playersInMatchTable.clear();
    }
  }

  private void updatePlayersNotInMatchTable() {
    Match match = matchesTable.getSelectedEntity();

    if (match != null) {
      List<Player> allPlayers = playerService.getAllPlayers();
      List<Player> playersInMatch =
          playerMatchInfoService.getInfoByMatch(match).stream()
              .map(PlayerMatchInfo::getPlayer)
              .toList();

      playersNotInMatchTable.updateEntities(
          allPlayers.stream()
              .filter(p -> playersInMatch.stream().noneMatch(pim -> p.getId().equals(pim.getId())))
              .toList());
    } else {
      playersNotInMatchTable.clear();
    }
  }

  private void updateAmpluasTable() {
    ampluasTable.updateEntities(ampluaService.getAllAmpluas());
  }

  private void updateAmpluasCombo() {
    DefaultComboBoxModel<Amplua> model = new DefaultComboBoxModel<>();
    model.addAll(ampluaService.getAllAmpluas());
    ampluasPlayersFilterCombo.setModel(model);
  }

  private void clearPlayersFilter() {
    ampluasPlayersFilterCombo.setSelectedItem(null);
    playersFilterMatchesCountSpinner.setValue(0);
    playersFilterGoalsCountSpinner.setValue(0);
  }

    /** Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.setMinimumSize(new Dimension(600, 400));
        rootPanel.setPreferredSize(new Dimension(600, 400));
        final JTabbedPane tabbedPane1 = new JTabbedPane();
        rootPanel.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Игроки", panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Список игроков", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panel2.add(playersTable, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addPlayerButton = new JButton();
        addPlayerButton.setText("Добавить");
        panel3.add(addPlayerButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editPlayerButton = new JButton();
        editPlayerButton.setText("Изменить");
        panel3.add(editPlayerButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removePlayerButton = new JButton();
        removePlayerButton.setText("Удалить");
        panel3.add(removePlayerButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        createPlayersReportButton = new JButton();
        createPlayersReportButton.setText("Выгрузить отчёт в HTML");
        panel3.add(createPlayersReportButton, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Фильтр", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panel4.add(ampluasPlayersFilterCombo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Позиция");
        panel4.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Минимум матчей");
        panel4.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel4.add(playersFilterMatchesCountSpinner, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Минимум голов");
        panel4.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel4.add(playersFilterGoalsCountSpinner, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        playersFilterButton = new JButton();
        playersFilterButton.setText("Применить");
        panel5.add(playersFilterButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        playersFilterClearButton = new JButton();
        playersFilterClearButton.setText("Очистить");
        panel5.add(playersFilterClearButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Матчи", panel6);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel7, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Список матчей", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel8, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editMatchButton = new JButton();
        editMatchButton.setText("Изменить");
        panel8.add(editMatchButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeMatchButton = new JButton();
        removeMatchButton.setText("Удалить");
        panel8.add(removeMatchButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addMatchButton = new JButton();
        addMatchButton.setText("Добавить");
        panel8.add(addMatchButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel7.add(matchesTable, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel9, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel9.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Игроки, сыгравшие в матче", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panel9.add(playersInMatchTable, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel9.add(panel10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removePlayerFromMatchButton = new JButton();
        removePlayerFromMatchButton.setText("Удалить из состава");
        panel10.add(removePlayerFromMatchButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addGoalButton = new JButton();
        addGoalButton.setText("+ гол");
        panel10.add(addGoalButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeGoalButton = new JButton();
        removeGoalButton.setText("- гол");
        panel10.add(removeGoalButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel11, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Игроки, не сыгравшие в матче", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panel11.add(playersNotInMatchTable, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        addPlayerInMatchButton = new JButton();
        addPlayerInMatchButton.setText("Добавить в состав на матч");
        panel11.add(addPlayerInMatchButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Позиции", panel12);
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel12.add(panel13, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addAmpluaButton = new JButton();
        addAmpluaButton.setText("Добавить");
        panel13.add(addAmpluaButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editAmpluaButton = new JButton();
        editAmpluaButton.setText("Изменить");
        panel13.add(editAmpluaButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeAmpluaButton = new JButton();
        removeAmpluaButton.setText("Удалить");
        panel13.add(removeAmpluaButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addAmpluasFromXmlButton = new JButton();
        addAmpluasFromXmlButton.setText("Добавить из XML");
        panel13.add(addAmpluasFromXmlButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        replaceAmpluasFromXmlButton = new JButton();
        replaceAmpluasFromXmlButton.setText("Заменить из XML");
        panel13.add(replaceAmpluasFromXmlButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        saveAmpluasToXmlButton = new JButton();
        saveAmpluasToXmlButton.setText("Сохранить в XML");
        panel13.add(saveAmpluasToXmlButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel12.add(ampluasTable, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /** @noinspection ALL */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

}
