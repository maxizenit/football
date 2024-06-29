package ru.borbotko.football.gui.dialog;

import jakarta.validation.ConstraintViolationException;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import ru.borbotko.football.exception.InvalidFieldException;

/**
 * Класс, представляющий диалог для добавления новой или изменения существующей сущности.
 *
 * @param <T> класс сущности
 */
public abstract class EntityDialog<T> extends CommonDialog {

  /** Указатель на метод сохранения сущности. */
  private final Consumer<T> saveFunction;

  /** Указатель на метод обновления таблицы сущностей. */
  private final Runnable refreshGUIFunction;

  /** Сущность. */
  private T entity;

  public EntityDialog(
      JFrame parent, Consumer<T> saveFunction, Runnable refreshGUIFunction, T entity) {
    super(parent);
    this.saveFunction = saveFunction;
    this.refreshGUIFunction = refreshGUIFunction;
    this.entity = entity;
  }

  /**
   * Инициализирует окно диалога и добавляет обработчики событий для кнопок.
   *
   * @param contentPanel главная панель диалога
   * @param buttonOK кнопка "ОК"
   * @param buttonCancel кнопка "Отмена"
   */
  protected void init(JPanel contentPanel, JButton buttonOK, JButton buttonCancel) {
    super.init(contentPanel, buttonOK);
    buttonCancel.addActionListener(e -> onCancel());
    if (entity != null) {
      fillFields(entity);
    } else {
      entity = createNewEntity();
    }
  }

  @Override
  protected void onOK() {
    try {
      saveEntity();
      onCancel();
    } catch (ConstraintViolationException | InvalidFieldException e) {
      showMessageDialog(e.getMessage());
      e.printStackTrace();
    }
  }

  /** Добавляет в результат новую или изменяет существующую сущность. */
  private void saveEntity() throws InvalidFieldException {
    if (isFieldsValid()) {
      fillEntityFromFields(entity);
      saveFunction.accept(entity);
      refreshGUIFunction.run();
    }
  }

  /**
   * Отображает диалог с заданным сообщением.
   *
   * @param message сообщение
   */
  private void showMessageDialog(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  /**
   * Заполняет поля диалога значениями из сущности.
   *
   * @param entity сущность
   */
  protected abstract void fillFields(T entity);

  /**
   * Проверяет корректность заполнения полей.
   *
   * @return {@code true}, если все поля заполнены корректно
   */
  protected abstract boolean isFieldsValid() throws InvalidFieldException;

  /**
   * Возвращает новый экземпляр класса сущности.
   *
   * @return новый экземпляр класса сущности
   */
  protected abstract T createNewEntity();

  /**
   * Заполняет поля сущности значениями из полей диалога.
   *
   * @param entity сущность
   */
  protected abstract void fillEntityFromFields(T entity);
}
