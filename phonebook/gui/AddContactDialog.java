package phonebook.gui;
import javax.swing.*;

import phonebook.model.TelephoneDirectory;

import java.awt.*;
import java.awt.event.*;

public class AddContactDialog extends JDialog {
    private JTextField nameField;
    private JTextField phoneField;
    private JButton addButton;
    private JButton cancelButton;
    private TelephoneDirectory directory;

    public AddContactDialog(TelephoneDirectory directory) {
        this.directory = directory;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                setEnabled(true);
            }
        });
        setTitle("Добавить телефон");
        setModal(true);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2));

        JLabel nameLabel = new JLabel("Наименование:");
        nameField = new JTextField();
        add(nameLabel);
        add(nameField);

        JLabel phoneLabel = new JLabel("Телефон:");
        phoneField = new JTextField();
        add(phoneLabel);
        add(phoneField);

        addButton = new JButton("Сохранить");
        addButton.addActionListener(new AddButtonListener());
        add(addButton);

        cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(new CancelButtonListener());
        add(cancelButton);
        
    
    }

    private class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String phone = phoneField.getText();
            directory.addContact(name, phone);
            setVisible(false);
        }
    }

    private class CancelButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    }
    

}