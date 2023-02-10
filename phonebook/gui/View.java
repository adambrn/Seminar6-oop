package phonebook.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import phonebook.model.Contact;
import phonebook.model.TelephoneDirectory;

public class View extends JFrame {
    private TelephoneDirectory directory;
    private JTable telephoneTable;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton clearSearchButton;

    public View(TelephoneDirectory directory) {
        this.directory = directory;

        initUI();
    }

    private void updateData() {
        DefaultTableModel model = (DefaultTableModel) telephoneTable.getModel();
        model.setRowCount(0); // очищаем таблицу
        for (Contact contact : directory.getContacts()) {
            model.addRow(new Object[] { contact.getName(), contact.getPhoneNumber() });
        }
    }

    private void initUI() {
        // Создание меню
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenu exportMenu = new JMenu("Экспорт");
        JMenu importMenu = new JMenu("Импорт");
        JMenuItem importItem = new JMenuItem("Разделитель запятая");
        importItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileImport(",");
            }
        });
        JMenuItem exportItem1 = new JMenuItem("Разделитель пробел");
        exportItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileExport(" ");
            }
        });
        JMenuItem exportItem2 = new JMenuItem("Разделитель запятая");
        exportItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileExport(",");
            }
        });

        JMenuItem exitMenuItem = new JMenuItem("Выход");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exportMenu.add(exportItem1);
        exportMenu.add(exportItem2);
        importMenu.add(importItem);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(importMenu);
        menuBar.add(exportMenu);

        setJMenuBar(menuBar);

        // Создание таблицы
        DefaultTableModel tableModel = new DefaultTableModel(new Object[] { "Наименование", "Телефон" }, 0);
        telephoneTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        updateData();
        JScrollPane tableScrollPane = new JScrollPane(telephoneTable);

        // Создание строки поиска и кнопки поиска
        JLabel nameLabel = new JLabel("Поиск:");
        searchField = new JTextField();
        searchButton = new JButton("Искать");
        clearSearchButton = new JButton("Очистить поиск");
        clearSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateData();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = searchField.getText();
                ArrayList<Contact> result = new ArrayList<Contact>();
                result = directory.searchContact(searchQuery);
                DefaultTableModel model = (DefaultTableModel) telephoneTable.getModel();
                model.setRowCount(0); // очищаем таблицу
                for (Contact contact : result) {
                    model.addRow(new Object[] { contact.getName(), contact.getPhoneNumber() });
                    // код для выполнения поиска и обновления таблицы результатов
                }
            }
        });

        // Создание кнопок
        addButton = new JButton("Добавить");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddContactDialog dialog = new AddContactDialog(directory);
                dialog.setVisible(true);
                setEnabled(false);
                updateData();
                setEnabled(true);
            }
        });

        editButton = new JButton("Редактировать");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = telephoneTable.getSelectedRow();
                if (selectedRow != -1) {
                    Contact selectedContact = directory.getContact((String) telephoneTable.getValueAt(selectedRow, 0),
                            (String) telephoneTable.getValueAt(selectedRow, 1));
                    EditContactDialog dialog = new EditContactDialog(selectedContact);
                    dialog.setVisible(true);
                    updateData();
                } else {
                    JOptionPane.showMessageDialog(null, "Выберить строку для редактирования");
                }
            }
        });

        deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = telephoneTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Выберить строку для удаления");
                } else {
                    int confirm = JOptionPane.showConfirmDialog(null, "Вы уверенны что хотите удалить контакт?",
                            "Удалить", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        directory.removeContact((String) telephoneTable.getValueAt(selectedRow, 0));
                        updateData();
                    }
                }
            }
        });
        // Создание панели для размещения элементов поиска
        JPanel searchPanel = new JPanel();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.1;
        constraints.weighty = 0.1;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        searchPanel.add(nameLabel, constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.8;
        searchField.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(searchField, constraints);

        constraints.gridx = 2;
        constraints.weightx = 0.1;
        searchPanel.add(searchButton, constraints);

        constraints.gridx = 3;
        searchPanel.add(clearSearchButton, constraints);

        // Создание панели для размещения кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Размещение элементов на главной панели
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Добавление главной панели на главное окно
        add(mainPanel);

        // Задание свойств главного окна
        setTitle("Телефонный справочник");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void run() {
        setVisible(true);
    }

    private void FileExport(String declamer) {
        JFileChooser fileChooser = new JFileChooser();
        File projectDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setCurrentDirectory(projectDirectory);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("txt files (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showSaveDialog(telephoneTable);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            directory.export(selectedFile.toString(), declamer);
        }
    }
    private void FileImport(String declamer) {
        JFileChooser fileChooser = new JFileChooser();
        File projectDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setCurrentDirectory(projectDirectory);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("txt files (*.txt)", "txt");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showSaveDialog(telephoneTable);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            directory.importContacts(selectedFile, declamer);
        }
        updateData();
    }

}