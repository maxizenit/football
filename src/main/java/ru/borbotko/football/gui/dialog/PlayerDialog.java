package ru.borbotko.football.gui.dialog;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.springframework.util.StringUtils;
import ru.borbotko.football.entity.Amplua;
import ru.borbotko.football.entity.Player;
import ru.borbotko.football.exception.InvalidFieldException;

/** Диалог для игроков. */
public class PlayerDialog extends EntityDialog<Player> {

  private JPanel mainPanel;

  private JButton buttonOK;

  private JButton buttonCancel;

  /** Поле позиции. */
  private JComboBox<Amplua> ampluaField;

  /** Поле фамилии. */
  private JTextField lastNameField;

  /** Поле имени. */
  private JTextField firstNameField;

  public PlayerDialog(
      JFrame parent,
      Consumer<Player> saveFunction,
      Runnable refreshGUIFunction,
      Player entity,
      List<Amplua> ampluas) {
    super(parent, saveFunction, refreshGUIFunction, entity);
    initAmpluaCombo(ampluas);
    init(mainPanel, buttonOK, buttonCancel);
    pack();
    setVisible(true);
  }

  /**
   * Инициализирует комбинированный списки для поля позиции.
   *
   * @param ampluas список позиций
   */
  private void initAmpluaCombo(List<Amplua> ampluas) {
    DefaultComboBoxModel<Amplua> model = new DefaultComboBoxModel<>();
    model.addAll(ampluas);
    ampluaField.setModel(model);
  }

  @Override
  protected void fillFields(Player entity) {
    ampluaField.setSelectedItem(entity.getAmplua());
    lastNameField.setText(entity.getLastName());
    firstNameField.setText(entity.getFirstName());
  }

  @Override
  protected boolean isFieldsValid() throws InvalidFieldException {
    List<String> invalidFields = new ArrayList<>();

    if (!StringUtils.hasText(lastNameField.getText())) {
      invalidFields.add("фамилия");
    }
    if (!StringUtils.hasText(firstNameField.getText())) {
      invalidFields.add("имя");
    }

    if (!invalidFields.isEmpty()) {
      throw new InvalidFieldException(invalidFields);
    }

    return true;
  }

  @Override
  protected Player createNewEntity() {
    return new Player();
  }

  @Override
  protected void fillEntityFromFields(Player entity) {
    entity.setAmplua((Amplua) ampluaField.getSelectedItem());
    entity.setLastName(lastNameField.getText());
    entity.setFirstName(firstNameField.getText());
  }

  {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /** Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    mainPanel = new JPanel();
    mainPanel.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
    mainPanel.add(panel1, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    buttonOK = new JButton();
    buttonOK.setText("ОК");
    panel1.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    buttonCancel = new JButton();
    buttonCancel.setText("Отменить");
    panel1.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label1 = new JLabel();
    label1.setText("Позиция");
    mainPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label2 = new JLabel();
    label2.setText("Фамилия");
    mainPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label3 = new JLabel();
    label3.setText("Имя");
    mainPanel.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    ampluaField = new JComboBox();
    mainPanel.add(ampluaField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    lastNameField = new JTextField();
    mainPanel.add(lastNameField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    firstNameField = new JTextField();
    mainPanel.add(firstNameField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
  }

  /** @noinspection ALL */
  public JComponent $$$getRootComponent$$$() {
    return mainPanel;
  }

}
