package com.lab03;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Random;

public class FormCompany extends JFrame {
    private AbstractCompany currentCompany;
    private JPanel drawingPanel;
    private JTextField positionField;
    private JLabel statusLabel;

    public FormCompany() {
        setTitle("Лабораторная работа №3 (усложнённая) - Гавань");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Создаём одну компанию (гавань) при запуске
        ICollectionGenericObjects<DrawingBoat> collection = new MassiveGenericObjects<>();
        currentCompany = new HarborCompany(800, 500, collection);

        createUI();
        refreshDisplay();
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
        add(drawingPanel, BorderLayout.CENTER);

        // Правая панель с кнопками
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(200, 500));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Кнопка добавления простой лодки
        JButton addSimpleBtn = new JButton("Добавить простую лодку");
        addSimpleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addSimpleBtn.addActionListener(e -> addSimpleBoat());
        rightPanel.add(addSimpleBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Кнопка добавления продвинутой лодки
        JButton addImprovedBtn = new JButton("Добавить лодку с парусом");
        addImprovedBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addImprovedBtn.addActionListener(e -> addImprovedBoat());
        rightPanel.add(addImprovedBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Кнопка генерации лодки (усложнение)
        JButton generateBtn = new JButton("🎲 Сгенерировать лодку");
        generateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateBtn.addActionListener(e -> openGenerator());
        rightPanel.add(generateBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Панель для удаления
        JPanel removePanel = new JPanel();
        removePanel.setLayout(new BoxLayout(removePanel, BoxLayout.Y_AXIS));
        removePanel.setBorder(BorderFactory.createTitledBorder("Удаление лодки"));

        JLabel posLabel = new JLabel("Номер позиции:");
        posLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        removePanel.add(posLabel);

        positionField = new JTextField();
        positionField.setMaximumSize(new Dimension(150, 25));
        removePanel.add(positionField);
        removePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton removeBtn = new JButton("Удалить по позиции");
        removeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeBtn.addActionListener(e -> removeBoatByPosition());
        removePanel.add(removeBtn);

        rightPanel.add(removePanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Кнопка передачи лодки на тесты
        JButton transferBtn = new JButton("Передать лодку на тесты");
        transferBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        transferBtn.addActionListener(e -> transferBoatToTest());
        rightPanel.add(transferBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Кнопка обновления
        JButton refreshBtn = new JButton("Обновить");
        refreshBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshBtn.addActionListener(e -> refreshDisplay());
        rightPanel.add(refreshBtn);

        // Статусная строка
        statusLabel = new JLabel("Лодок в гавани: 0");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(statusLabel);

        add(rightPanel, BorderLayout.EAST);
    }

    private void addSimpleBoat() {
        if (currentCompany == null) return;

        // Диалог выбора цвета
        Color bodyColor = JColorChooser.showDialog(this, "Выберите цвет корпуса", Color.WHITE);
        if (bodyColor == null) {
            // Если пользователь отменил, генерируем случайный цвет
            Random random = new Random();
            bodyColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }

        // Диалог выбора количества вёсел
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

        // Диалог выбора типа вёсел
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

        // Создаём вёсла выбранного типа
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
        if (currentCompany == null) return;

        // Диалог выбора цвета корпуса
        Color bodyColor = JColorChooser.showDialog(this, "Выберите цвет корпуса", Color.WHITE);
        if (bodyColor == null) {
            Random random = new Random();
            bodyColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }

        // Диалог выбора цвета паруса
        Color sailColor = JColorChooser.showDialog(this, "Выберите цвет паруса", Color.WHITE);
        if (sailColor == null) {
            Random random = new Random();
            sailColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }

        // Диалог выбора наличия паруса
        int hasSailChoice = JOptionPane.showConfirmDialog(this, "Добавить парус?", "Парус", JOptionPane.YES_NO_OPTION);
        boolean hasSail = (hasSailChoice == JOptionPane.YES_OPTION);

        // Диалог выбора количества вёсел
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

        // Диалог выбора типа вёсел
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

        // Создаём вёсла выбранного типа
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
        switch (type) {
            case 0: return new Oar(oarCount);
            case 1: return new OarWithOrnament(oarCount);
            default: return new OarWithColorfulBlade(oarCount);
        }
    }

    private void removeBoatByPosition() {
        if (currentCompany == null) return;

        String text = positionField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Сначала введите номер позиции!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int position = Integer.parseInt(text);
            if (currentCompany.removeBoat(position)) {
                refreshDisplay();
                JOptionPane.showMessageDialog(this, "Лодка удалена с позиции " + position, "Успех", JOptionPane.INFORMATION_MESSAGE);
                positionField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Не удалось удалить лодку! Неверная позиция или позиция пуста.", "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Введите корректное число!", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void transferBoatToTest() {
        if (currentCompany == null) return;

        DrawingBoat originalBoat = currentCompany.getRandomObject();
        if (originalBoat == null) {
            JOptionPane.showMessageDialog(this, "В гавани нет лодок для передачи!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Создаём новую лодку с теми же параметрами (простое копирование)
        DrawingBoat clonedBoat;
        if (originalBoat instanceof DrawingImprovedBoat improved) {
            clonedBoat = new DrawingImprovedBoat(
                    improved.getSpeed(),
                    improved.getWeight(),
                    improved.getBodyColor(),
                    improved.getSailColor(),
                    improved.hasSail()
            );
            clonedBoat.setOarDrawer(improved.getOarDrawer());
        } else {
            clonedBoat = new DrawingBoat(
                    originalBoat.getSpeed(),
                    originalBoat.getWeight(),
                    originalBoat.getBodyColor()
            );
            clonedBoat.setOarDrawer(originalBoat.getOarDrawer());
        }

        // Сбрасываем позицию
        clonedBoat.setPosition(0, 0);

        FormBoat formBoat = new FormBoat();
        formBoat.setDrawingBoat(clonedBoat);
        formBoat.setVisible(true);
    }

    // Вспомогательный метод для копирования лодки
    private DrawingBoat copyBoat(DrawingBoat original) {
        if (original instanceof DrawingImprovedBoat improved) {
            // Копируем продвинутую лодку
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
            // Копируем простую лодку
            DrawingBoat copy = new DrawingBoat(
                    original.getSpeed(),
                    original.getWeight(),
                    original.getBodyColor()
            );
            copy.setOarDrawer(original.getOarDrawer());
            return copy;
        }
    }

    private void openGenerator() {
        FormGenerator generator = new FormGenerator(this);
        generator.setVisible(true);

        if (generator.isBoatGenerated()) {
            DrawingBoat newBoat = generator.getGeneratedBoat();
            if (newBoat != null && currentCompany != null) {
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
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FormCompany frame = new FormCompany();
            frame.setVisible(true);
        });
    }
}