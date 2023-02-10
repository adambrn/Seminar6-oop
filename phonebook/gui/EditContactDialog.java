package phonebook.gui;

import javax.swing.*;

import phonebook.model.Contact;
import phonebook.model.TelephoneDirectory;

import java.awt.*;
import java.awt.event.*;

public class EditContactDialog extends JDialog implements ActionListener {
    private JTextField nameField;
    private JTextField phoneField;
    private JButton saveButton;
    private JButton cancelButton;
    private Contact contact;

    public EditContactDialog(Contact contact) {
        this.contact = contact;

        // инициализация интерфейса
        nameField = new JTextField(contact.getName());
        phoneField = new JTextField(contact.getPhoneNumber());
        saveButton = new JButton("Сохранить");
        cancelButton = new JButton("Отмена");

        // добавление слушателей событий кнопок
        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);

    

         // добавление компонентов на диалоговое окно
         add(new JLabel("Наименование: "));
         add(nameField);
         add(new JLabel("Телефон: "));
         add(phoneField);
         add(saveButton);
         add(cancelButton);

         // установка других свойств диалогового окна
        setTitle("Редактировать");
        setModal(true);
        setLocationRelativeTo(null);
        setSize(300, 150);
        setLayout(new GridLayout(3, 2));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            contact.setName(nameField.getText());
            contact.setPhone(phoneField.getText());
            TelephoneDirectory.logAction("update",nameField.getText(), phoneField.getText());
            setVisible(false);
        } else if (e.getSource() == cancelButton) {
            setVisible(false);
        }
    }
}

