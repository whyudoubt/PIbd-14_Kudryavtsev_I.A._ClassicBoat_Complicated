package com.lab04;

import javax.swing.*;
import java.awt.*;

public class FormDeletedBoats extends JDialog {
    private final StorageCompanies storage;
    private final JList<String> boatsList;
    private final DefaultListModel<String> listModel;

    public FormDeletedBoats(JFrame parent, StorageCompanies storage) {
        super(parent, "Удалённые лодки", true);
        this.storage = storage;
        this.listModel = new DefaultListModel<>();
        this.boatsList = new JList<>(listModel);

        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        refreshList();

        add(new JScrollPane(boatsList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton transferBtn = new JButton("Передать выбранную на тесты");
        transferBtn.addActionListener(e -> transferSelected());

        JButton closeBtn = new JButton("Закрыть");
        closeBtn.addActionListener(e -> dispose());

        buttonPanel.add(transferBtn);
        buttonPanel.add(closeBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshList() {
        listModel.clear();
        for (int i = 0; i < storage.getDeletedBoatsCount(); i++) {
            DrawingBoat boat = storage.getDeletedBoatByIndex(i);
            if (boat != null) {
                String boatInfo = formatBoatInfo(i, boat);
                listModel.addElement(boatInfo);
            }
        }
    }

    private String formatBoatInfo(int index, DrawingBoat boat) {
        StringBuilder info = new StringBuilder();
        info.append(index).append(": ");

        if (boat instanceof DrawingImprovedBoat improved) {
            info.append("[ПРОДВИНУТАЯ] ");
            info.append("Парус: ").append(improved.hasSail() ? "есть" : "нет");
        } else {
            info.append("[ПРОСТАЯ] ");
        }

        info.append(" | Скорость: ").append(boat.getSpeed());
        info.append(" | Вес: ").append((int) boat.getWeight());
        info.append(" | Шаг: ").append((int) Math.round(boat.getBoatStep()));

        IOarDrawer oar = boat.getOarDrawer();
        if (oar != null) {
            String oarType;
            if (oar instanceof OarWithOrnament) {
                oarType = "с орнаментом";
            } else if (oar instanceof OarWithColorfulBlade) {
                oarType = "разноцветные";
            } else {
                oarType = "обычные";
            }
            info.append(" | Вёсла: ").append(oarType).append(" (").append(oar.getOarCount()).append(" шт.)");
        }

        return info.toString();
    }

    private void transferSelected() {
        int selected = boatsList.getSelectedIndex();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Выберите лодку!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DrawingBoat boat = storage.getDeletedBoatByIndex(selected);
        if (boat == null) {
            JOptionPane.showMessageDialog(this, "Лодка не найдена!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Создаём копию лодки
        DrawingBoat clonedBoat = copyBoat(boat);
        if (clonedBoat == null) {
            JOptionPane.showMessageDialog(this, "Не удалось скопировать лодку!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        clonedBoat.setPosition(0, 0);

        // Закрываем диалог перед открытием формы тест-драйва
        dispose();

        // Создаём и показываем форму тест-драйва
        SwingUtilities.invokeLater(() -> {
            FormBoat formBoat = new FormBoat();
            formBoat.setDrawingBoat(clonedBoat);
            formBoat.setVisible(true);
        });
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
}