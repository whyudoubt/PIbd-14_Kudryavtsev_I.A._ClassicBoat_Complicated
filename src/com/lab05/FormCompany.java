package com.lab05;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class FormCompany extends JFrame {
    private AbstractCompany currentCompany;
    private final StorageCompanies storage;
    private JPanel drawingPanel;
    private JTextField positionField;
    private JLabel statusLabel;
    private JComboBox<String> companyCombo;
    private JTextField companyNameField;
    private JComboBox<CollectionType> collectionTypeCombo;

    public FormCompany() {
        setTitle("Лабораторная работа №5 (усложнённая) - Гавань");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());  // возвращаем BorderLayout

        storage = new StorageCompanies();
        currentCompany = null;

        createUI();
        updateCompanyList();
    }

    private void createUI() {
        // Панель для рисования
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (currentCompany != null) {
                    Image img = currentCompany.show();
                    if (img != null) {
                        g.drawImage(img, 0, 0, this);
                    }
                }
            }
        };
        drawingPanel.setBackground(Color.LIGHT_GRAY);
        drawingPanel.setPreferredSize(new Dimension(800, 500));
        add(drawingPanel, BorderLayout.CENTER);  // Центр будет растягиваться

        // Правая панель с кнопками
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(280, 500));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Панель создания новой компании
        JPanel createCompanyPanel = new JPanel();
        createCompanyPanel.setLayout(new BoxLayout(createCompanyPanel, BoxLayout.Y_AXIS));
        createCompanyPanel.setBorder(BorderFactory.createTitledBorder("Создать компанию"));
        createCompanyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        companyNameField = new JTextField();
        companyNameField.setMaximumSize(new Dimension(250, 25));
        companyNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        createCompanyPanel.add(companyNameField);
        createCompanyPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        collectionTypeCombo = new JComboBox<>(CollectionType.values());
        collectionTypeCombo.setMaximumSize(new Dimension(250, 25));
        collectionTypeCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        createCompanyPanel.add(collectionTypeCombo);
        createCompanyPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JButton createCompanyBtn = new JButton("Создать компанию");
        createCompanyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        createCompanyBtn.setMaximumSize(new Dimension(250, 30));
        createCompanyBtn.addActionListener(e -> createCompany());
        createCompanyPanel.add(createCompanyBtn);

        rightPanel.add(createCompanyPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Панель выбора компании
        JPanel selectCompanyPanel = new JPanel();
        selectCompanyPanel.setLayout(new BoxLayout(selectCompanyPanel, BoxLayout.Y_AXIS));
        selectCompanyPanel.setBorder(BorderFactory.createTitledBorder("Выбрать компанию"));
        selectCompanyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        companyCombo = new JComboBox<>();
        companyCombo.setMaximumSize(new Dimension(250, 25));
        companyCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectCompanyPanel.add(companyCombo);
        selectCompanyPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JButton deleteCompanyBtn = new JButton("Удалить компанию");
        deleteCompanyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteCompanyBtn.setMaximumSize(new Dimension(250, 30));
        deleteCompanyBtn.addActionListener(e -> deleteCompany());
        selectCompanyPanel.add(deleteCompanyBtn);

        rightPanel.add(selectCompanyPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Кнопка создания лодки через конфигуратор
        JButton configBoatBtn = new JButton("Создать лодку (конфигуратор)");
        configBoatBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        configBoatBtn.setMaximumSize(new Dimension(250, 35));
        configBoatBtn.addActionListener(e -> openBoatConfig());
        rightPanel.add(configBoatBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Кнопка добавления простой лодки
        JButton addSimpleBtn = new JButton("Добавить простую лодку");
        addSimpleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addSimpleBtn.setMaximumSize(new Dimension(250, 35));
        addSimpleBtn.addActionListener(e -> addSimpleBoat());
        rightPanel.add(addSimpleBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Кнопка добавления продвинутой лодки
        JButton addImprovedBtn = new JButton("Добавить лодку с парусом");
        addImprovedBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addImprovedBtn.setMaximumSize(new Dimension(250, 35));
        addImprovedBtn.addActionListener(e -> addImprovedBoat());
        rightPanel.add(addImprovedBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Кнопка генерации лодки
        JButton generateBtn = new JButton("Генерировать лодку");
        generateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateBtn.setMaximumSize(new Dimension(250, 35));
        generateBtn.addActionListener(e -> openGenerator());
        rightPanel.add(generateBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Панель для удаления
        JPanel removePanel = new JPanel();
        removePanel.setLayout(new BoxLayout(removePanel, BoxLayout.Y_AXIS));
        removePanel.setBorder(BorderFactory.createTitledBorder("Удаление лодки"));
        removePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel posLabel = new JLabel("Номер позиции:");
        posLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        removePanel.add(posLabel);

        positionField = new JTextField();
        positionField.setMaximumSize(new Dimension(150, 25));
        positionField.setAlignmentX(Component.CENTER_ALIGNMENT);
        removePanel.add(positionField);
        removePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton removeBtn = new JButton("Удалить по позиции");
        removeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeBtn.setMaximumSize(new Dimension(250, 35));
        removeBtn.addActionListener(e -> removeBoatByPosition());
        removePanel.add(removeBtn);

        rightPanel.add(removePanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Кнопка передачи лодки на тесты
        JButton transferBtn = new JButton("Передать лодку на тесты");
        transferBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        transferBtn.setMaximumSize(new Dimension(250, 35));
        transferBtn.addActionListener(e -> transferBoatToTest());
        rightPanel.add(transferBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Кнопка показа удалённых лодок
        JButton showDeletedBtn = new JButton("Показать удалённую лодку");
        showDeletedBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        showDeletedBtn.setMaximumSize(new Dimension(250, 35));
        showDeletedBtn.addActionListener(e -> showDeletedBoat());
        rightPanel.add(showDeletedBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Кнопка всех удалённых лодок
        JButton showAllDeletedBtn = new JButton("Все удалённые лодки");
        showAllDeletedBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        showAllDeletedBtn.setMaximumSize(new Dimension(250, 35));
        showAllDeletedBtn.addActionListener(e -> showAllDeletedBoats());
        rightPanel.add(showAllDeletedBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Кнопка обновления
        JButton refreshBtn = new JButton("Обновить");
        refreshBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshBtn.setMaximumSize(new Dimension(250, 35));
        refreshBtn.addActionListener(e -> refreshDisplay());
        rightPanel.add(refreshBtn);

        // Статусная строка
        statusLabel = new JLabel("Нет выбранной компании");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(statusLabel);

        add(rightPanel, BorderLayout.EAST);  // Правая панель фиксированной ширины
    }

    private void createCompany() {
        String name = companyNameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите название компании!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CollectionType type = (CollectionType) collectionTypeCombo.getSelectedItem();
        storage.addCompany(name, type, 800, 500);
        updateCompanyList();
        companyNameField.setText("");

        JOptionPane.showMessageDialog(this, "Компания \"" + name + "\" создана!", "Успех", JOptionPane.INFORMATION_MESSAGE);
    }

    // Метод открытия формы конфигурации
    private void openBoatConfig() {
        if (currentCompany == null) {
            JOptionPane.showMessageDialog(this, "Сначала выберите компанию!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        FormBoatConfig config = new FormBoatConfig(this, boat -> {
            if (currentCompany != null && currentCompany.addBoat(boat)) {
                refreshDisplay();
                JOptionPane.showMessageDialog(this, "Лодка добавлена!", "Успех", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Не удалось добавить лодку!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        });
        config.setVisible(true);
    }
    private void deleteCompany() {
        String selected = (String) companyCombo.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Выберите компанию для удаления!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Удалить компанию \"" + selected + "\"?",
                "Подтверждение",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            storage.removeCompany(selected);
            if (currentCompany != null && storage.getCompany(selected) == null) {
                currentCompany = null;
            }
            updateCompanyList();
            refreshDisplay();
            JOptionPane.showMessageDialog(this, "Компания удалена!", "Успех", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateCompanyList() {
        companyCombo.removeAllItems();
        for (String key : storage.getCompanyKeys()) {
            companyCombo.addItem(key);
        }
        if (companyCombo.getItemCount() > 0) {
            companyCombo.setSelectedIndex(0);
            selectCompany();
        } else {
            currentCompany = null;
            statusLabel.setText("Нет выбранной компании");
            drawingPanel.repaint();
        }
    }

    private void selectCompany() {
        String selected = (String) companyCombo.getSelectedItem();
        if (selected != null) {
            currentCompany = storage.getCompany(selected);
            refreshDisplay();
        }
    }

    private void addSimpleBoat() {
        if (currentCompany == null) {
            JOptionPane.showMessageDialog(this, "Сначала создайте или выберите компанию!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Color bodyColor = JColorChooser.showDialog(this, "Выберите цвет корпуса", Color.WHITE);
        if (bodyColor == null) {
            Random random = new Random();
            bodyColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }

        String[] options = {"1 весло", "2 весла", "3 весла"};
        int choice = JOptionPane.showOptionDialog(this,
                "Выберите количество вёсел",
                "Вёсла",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);
        int oarCount = choice + 1;

        String[] oarTypes = {"Обычные", "С орнаментом", "Разноцветные"};
        int oarTypeChoice = JOptionPane.showOptionDialog(this,
                "Выберите тип вёсел",
                "Тип вёсел",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                oarTypes,
                oarTypes[0]);

        Random random = new Random();
        int speed = random.nextInt(200) + 100;
        double weight = random.nextInt(2000) + 1000;

        DrawingBoat boat = new DrawingBoat(speed, weight, bodyColor);
        IOarDrawer oar = createOarDrawer(oarTypeChoice, oarCount);
        boat.setOarDrawer(oar);

        if (currentCompany.addBoat(boat)) {
            refreshDisplay();
            JOptionPane.showMessageDialog(this, "Лодка добавлена!", "Успех", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Не удалось добавить лодку!", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addImprovedBoat() {
        if (currentCompany == null) {
            JOptionPane.showMessageDialog(this, "Сначала создайте или выберите компанию!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Color bodyColor = JColorChooser.showDialog(this, "Выберите цвет корпуса", Color.WHITE);
        if (bodyColor == null) {
            Random random = new Random();
            bodyColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }

        Color sailColor = JColorChooser.showDialog(this, "Выберите цвет паруса", Color.WHITE);
        if (sailColor == null) {
            Random random = new Random();
            sailColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }

        int hasSailChoice = JOptionPane.showConfirmDialog(this, "Добавить парус?", "Парус", JOptionPane.YES_NO_OPTION);
        boolean hasSail = (hasSailChoice == JOptionPane.YES_OPTION);

        String[] options = {"1 весло", "2 весла", "3 весла"};
        int choice = JOptionPane.showOptionDialog(this,
                "Выберите количество вёсел",
                "Вёсла",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);
        int oarCount = choice + 1;

        String[] oarTypes = {"Обычные", "С орнаментом", "Разноцветные"};
        int oarTypeChoice = JOptionPane.showOptionDialog(this,
                "Выберите тип вёсел",
                "Тип вёсел",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                oarTypes,
                oarTypes[0]);

        Random random = new Random();
        int speed = random.nextInt(200) + 100;
        double weight = random.nextInt(2000) + 1000;

        DrawingImprovedBoat boat = new DrawingImprovedBoat(speed, weight, bodyColor, sailColor, hasSail);
        IOarDrawer oar = createOarDrawer(oarTypeChoice, oarCount);
        boat.setOarDrawer(oar);

        if (currentCompany.addBoat(boat)) {
            refreshDisplay();
            JOptionPane.showMessageDialog(this, "Продвинутая лодка добавлена!", "Успех", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Не удалось добавить лодку!", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    private IOarDrawer createOarDrawer(int type, int count) {
        OarCount oarCount = OarCount.fromInt(count);
        return switch (type) {
            case 0 -> new Oar(oarCount);
            case 1 -> new OarWithOrnament(oarCount);
            default -> new OarWithColorfulBlade(oarCount);
        };
    }

    private void removeBoatByPosition() {
        if (currentCompany == null) {
            JOptionPane.showMessageDialog(this, "Сначала выберите компанию!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String text = positionField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите номер позиции!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int position = Integer.parseInt(text);
            if (currentCompany.removeBoat(position)) {
                refreshDisplay();
                JOptionPane.showMessageDialog(this,
                        "Лодка удалена с позиции " + position,
                        "Успех",
                        JOptionPane.INFORMATION_MESSAGE);
                positionField.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Не удалось удалить лодку! Неверная позиция или позиция пуста.",
                        "Ошибка",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Введите корректное число!", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void transferBoatToTest() {
        if (currentCompany == null) {
            JOptionPane.showMessageDialog(this, "Сначала выберите компанию!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DrawingBoat originalBoat = currentCompany.getRandomObject();
        if (originalBoat == null) {
            JOptionPane.showMessageDialog(this, "В гавани нет лодок для передачи!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DrawingBoat clonedBoat = copyBoat(originalBoat);
        if (clonedBoat == null) {
            JOptionPane.showMessageDialog(this, "Не удалось скопировать лодку!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        clonedBoat.setPosition(0, 0);

        FormBoat formBoat = new FormBoat();
        formBoat.setDrawingBoat(clonedBoat);
        formBoat.setVisible(true);
    }

    private DrawingBoat copyBoat(DrawingBoat original) {
        if (original instanceof DrawingImprovedBoat improved) {
            DrawingImprovedBoat copy = new DrawingImprovedBoat(
                    improved.getSpeed(),
                    improved.getWeight(),
                    improved.getBodyColor(),
                    improved.getSailColor(),
                    improved.hasSail()
            );
            copy.setOarDrawer(improved.getOarDrawer());
            return copy;
        } else {
            DrawingBoat copy = new DrawingBoat(
                    original.getSpeed(),
                    original.getWeight(),
                    original.getBodyColor()
            );
            copy.setOarDrawer(original.getOarDrawer());
            return copy;
        }
    }

    private void showAllDeletedBoats() {
        if (!storage.hasDeletedBoats()) {
            JOptionPane.showMessageDialog(this, "Нет удалённых лодок!", "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Создаём диалог и показываем
        FormDeletedBoats dialog = new FormDeletedBoats(this, storage);
        dialog.setVisible(true);
    }

    private void showDeletedBoat() {
        if (!storage.hasDeletedBoats()) {
            JOptionPane.showMessageDialog(this,
                    "Нет удалённых лодок!",
                    "Информация",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        DrawingBoat deletedBoat = storage.getLastDeletedBoat();
        if (deletedBoat == null) return;

        FormBoat formBoat = new FormBoat();
        formBoat.setDrawingBoat(deletedBoat);
        formBoat.setVisible(true);
    }

    private void openGenerator() {
        if (currentCompany == null) {
            JOptionPane.showMessageDialog(this, "Сначала выберите компанию!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        FormGenerator generator = new FormGenerator(this);
        generator.setVisible(true);

        if (generator.isBoatGenerated()) {
            DrawingBoat newBoat = generator.getGeneratedBoat();
            if (newBoat != null) {
                currentCompany.addBoat(newBoat);
                refreshDisplay();
                JOptionPane.showMessageDialog(this,
                        "Лодка успешно добавлена в коллекцию!",
                        "Успех",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void refreshDisplay() {
        drawingPanel.repaint();
        if (currentCompany != null) {
            statusLabel.setText("Лодок в гавани: " + currentCompany.getCountObjects());
        } else {
            statusLabel.setText("Нет выбранной компании");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FormCompany frame = new FormCompany();
            frame.setVisible(true);
        });
    }
}