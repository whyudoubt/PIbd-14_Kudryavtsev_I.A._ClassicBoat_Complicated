package com.lab01;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Main extends JFrame {
    private final CanvasForBoat canvas;
    private DirectionType checkBordersState;
    private JPanel drawingPanel;

    public Main() {
        setTitle("Лабораторная работа №1 (усложнённая) - Лодка с вёслами");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        canvas = new CanvasForBoat();
        checkBordersState = DirectionType.NONE;

        // Панель для рисования
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image img = canvas.drawCanvas();
                if (img != null) {
                    g.drawImage(img, 0, 0, this);
                }
            }
        };
        drawingPanel.setBackground(Color.WHITE);
        drawingPanel.setPreferredSize(new Dimension(800, 500));
        add(drawingPanel, BorderLayout.CENTER);

        // Панель с кнопками
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton createButton = new JButton("Создать");
        JButton checkBordersButton = new JButton("Проверка границ");
        JButton leftButton = new JButton("←");
        JButton rightButton = new JButton("→");
        JButton upButton = new JButton("↑");
        JButton downButton = new JButton("↓");

        createButton.addActionListener(e -> createBoat());
        checkBordersButton.addActionListener(e -> checkBorders());
        leftButton.addActionListener(e -> moveBoat(DirectionType.LEFT));
        rightButton.addActionListener(e -> moveBoat(DirectionType.RIGHT));
        upButton.addActionListener(e -> moveBoat(DirectionType.UP));
        downButton.addActionListener(e -> moveBoat(DirectionType.DOWN));

        buttonPanel.add(createButton);
        buttonPanel.add(checkBordersButton);
        buttonPanel.add(leftButton);
        buttonPanel.add(rightButton);
        buttonPanel.add(upButton);
        buttonPanel.add(downButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Установка размеров поля после загрузки
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                if (drawingPanel.getWidth() > 0 && drawingPanel.getHeight() > 0) {
                    canvas.setPictureSize(drawingPanel.getWidth(), drawingPanel.getHeight());
                    drawingPanel.repaint();
                }
            }
        });
    }

    private void createBoat() {
        Random random = new Random();
        DrawingBoat boat = new DrawingBoat();

        int speed = random.nextInt(200) + 100; // 100-300
        double weight = random.nextInt(2000) + 1000; // 1000-3000
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

        boat.init(speed, weight, color);

        if (canvas.insertBoat(boat)) {
            canvas.setBoatPosition(random.nextInt(90) + 10, random.nextInt(90) + 10);
            drawingPanel.repaint();

            int oarCount = boat.getOarCount();
            setTitle(String.format("Лодка | Скорость: %d | Вес: %.0f | Шаг: %d | Вёсел: %d",
                    speed, weight, (int) Math.round(boat.getBoatStep()), oarCount));
        } else {
            JOptionPane.showMessageDialog(this, "Объект слишком большой для поля!", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void moveBoat(DirectionType direction) {
        if (canvas.moveTransport(direction)) {
            drawingPanel.repaint();
        }
    }

    private void checkBorders() {
        Random random = new Random();

        switch (checkBordersState) {
            case NONE:
            case DOWN:
                canvas.setBoatPosition(random.nextInt(90) + 10 - 1000, random.nextInt(90) + 10);
                checkBordersState = DirectionType.LEFT;
                break;
            case LEFT:
                canvas.setBoatPosition(random.nextInt(90) + 10, random.nextInt(90) + 10 - 1000);
                checkBordersState = DirectionType.UP;
                break;
            case UP:
                canvas.setBoatPosition(random.nextInt(90) + 10 + drawingPanel.getWidth(), random.nextInt(90) + 10);
                checkBordersState = DirectionType.RIGHT;
                break;
            case RIGHT:
                canvas.setBoatPosition(random.nextInt(90) + 10, random.nextInt(90) + 10 + drawingPanel.getHeight());
                checkBordersState = DirectionType.DOWN;
                break;
        }
        drawingPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main frame = new Main();
            frame.setVisible(true);
        });
    }
}