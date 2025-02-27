package ru.borbotko.football.gui.dialog;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.springframework.util.StringUtils;
import ru.borbotko.football.entity.Amplua;
import ru.borbotko.football.exception.InvalidFieldException;

/** Диалог для позиций. */
public class AmpluaDialog extends EntityDialog<Amplua> {

  private JPanel mainPanel;

  private JButton buttonOK;

  private JButton buttonCancel;

  /** Поле кода. */
  private JTextField codeField;

  /** Поле описания. */
  private JTextField descriptionField;

  public AmpluaDialog(
      JFrame parent, Consumer<Amplua> saveFunction, Runnable refreshGUIFunction, Amplua entity) {
    super(parent, saveFunction, refreshGUIFunction, entity);
    init(mainPanel, buttonOK, buttonCancel);
    pack();
    setVisible(true);
  }

  @Override
  protected void fillFields(Amplua entity) {
    codeField.setText(entity.getCode());
    descriptionField.setText(entity.getDescription());
  }

  @Override
  protected boolean isFieldsValid() throws InvalidFieldException {
    List<String> invalidFields = new ArrayList<>();

    if (!StringUtils.hasText(codeField.getText())) {
      invalidFields.add("код");
    }
    if (!StringUtils.hasText(descriptionField.getText())) {
      invalidFields.add("описание");
    }

    if (!invalidFields.isEmpty()) {
      throw new InvalidFieldException(invalidFields);
    }

    return true;
  }

  @Override
  protected Amplua createNewEntity() {
    return new Amplua();
  }

  @Override
  protected void fillEntityFromFields(Amplua entity) {
    entity.setCode(codeField.getText());
    entity.setDescription(descriptionField.getText());
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
    mainPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
    mainPanel.add(panel1, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    buttonOK = new JButton();
    buttonOK.setText("ОК");
    panel1.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    buttonCancel = new JButton();
    buttonCancel.setText("Отменить");
    panel1.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label1 = new JLabel();
    label1.setText("Код");
    mainPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label2 = new JLabel();
    label2.setText("Описание");
    mainPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    codeField = new JTextField();
    mainPanel.add(codeField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    descriptionField = new JTextField();
    mainPanel.add(descriptionField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
  }

  /** @noinspection ALL */
  public JComponent $$$getRootComponent$$$() {
    return mainPanel;
  }

}
