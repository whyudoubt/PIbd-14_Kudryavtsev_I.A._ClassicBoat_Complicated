package com.lab03;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class FormBoat extends JFrame {
    private final CanvasForBoat canvas;
    private DirectionType checkBordersState;
    private DrawingBoat currentBoat;
    private BaseTemplateMovement templateMovement;
    private JComboBox<String> destinationCombo;
    private JPanel drawingPanel;

    public FormBoat() {
        setTitle("Тест-драйв лодки");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        canvas = new CanvasForBoat();
        checkBordersState = DirectionType.NONE;
        currentBoat = null;
        templateMovement = null;

        createUI();
    }

    private void createUI() {
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
        drawingPanel.setPreferredSize(new Dimension(800, 450));
        add(drawingPanel, BorderLayout.CENTER);

        // Верхняя панель с выбором цели
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JLabel destinationLabel = new JLabel("Цель перемещения:");
        destinationCombo = new JComboBox<>(new String[]{"К центру", "В правый нижний угол"});
        destinationCombo.setEnabled(false);
        destinationCombo.addActionListener(e -> startMovingToDestination());

        JButton stepButton = new JButton("Шаг");
        stepButton.addActionListener(e -> stepMove());

        topPanel.add(destinationLabel);
        topPanel.add(destinationCombo);
        topPanel.add(stepButton);
        add(topPanel, BorderLayout.NORTH);

        // Нижняя панель с кнопками
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton checkBordersButton = new JButton("Проверка границ");
        JButton leftButton = new JButton("←");
        JButton rightButton = new JButton("→");
        JButton upButton = new JButton("↑");
        JButton downButton = new JButton("↓");

        checkBordersButton.addActionListener(e -> checkBorders());
        leftButton.addActionListener(e -> moveBoat(DirectionType.LEFT));
        rightButton.addActionListener(e -> moveBoat(DirectionType.RIGHT));
        upButton.addActionListener(e -> moveBoat(DirectionType.UP));
        downButton.addActionListener(e -> moveBoat(DirectionType.DOWN));

        buttonPanel.add(checkBordersButton);
        buttonPanel.add(leftButton);
        buttonPanel.add(rightButton);
        buttonPanel.add(upButton);
        buttonPanel.add(downButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Обновление размеров поля при изменении окна
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                if (drawingPanel.getWidth() > 0 && drawingPanel.getHeight() > 0) {
                    canvas.setPictureSize(drawingPanel.getWidth(), drawingPanel.getHeight());
                    drawingPanel.repaint();
                }
            }
        });
    }

    public void setDrawingBoat(DrawingBoat boat) {
        if (boat == null) {
            JOptionPane.showMessageDialog(this, "Лодка не существует!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        this.currentBoat = boat;

        // Устанавливаем размеры поля
        canvas.setPictureSize(800, 500);

        // Сбрасываем позицию лодки
        boat.setPosition(0, 0);

        if (canvas.insertBoat(boat)) {
            canvas.setBoatPosition(50, 50);

            // Для продвинутой лодки включаем выбор цели
            destinationCombo.setEnabled(boat instanceof DrawingImprovedBoat);
            destinationCombo.setSelectedIndex(-1);
            templateMovement = null;
            drawingPanel.repaint();

            String boatType = boat instanceof DrawingImprovedBoat ? "продвинутая" : "простая";
            setTitle("Тест-драйв " + boatType + " лодки | Шаг: " + (int) Math.round(boat.getBoatStep()));
        } else {
            JOptionPane.showMessageDialog(this, "Не удалось добавить лодку на поле!", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void moveBoat(DirectionType direction) {
        if (currentBoat == null) return;
        if (canvas.moveTransport(direction)) {
            drawingPanel.repaint();
        }
    }

    private void checkBorders() {
        if (currentBoat == null) return;
        Random random = new Random();

        switch (checkBordersState) {
            case NONE, DOWN:
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

    private void startMovingToDestination() {
        if (currentBoat == null) return;

        int selectedIndex = destinationCombo.getSelectedIndex();
        if (selectedIndex == -1) {
            templateMovement = null;
            return;
        }

        // Создаём новую стратегию (старая просто заменяется)
        templateMovement = switch (selectedIndex) {
            case 0 -> new MoveToCenter();
            case 1 -> new MoveToRightDownBorder();
            default -> null;
        };

        if (templateMovement != null) {
            templateMovement.setData(new MoveableAdapterBoat(currentBoat),
                    drawingPanel.getWidth(), drawingPanel.getHeight());
        }
    }

    private void stepMove() {
        if (templateMovement == null || currentBoat == null) return;

        templateMovement.makeStep();
        drawingPanel.repaint();

        if (templateMovement.isFinishReached()) {
            JOptionPane.showMessageDialog(this, "Цель достигнута!", "Уведомление", JOptionPane.INFORMATION_MESSAGE);
            destinationCombo.setSelectedIndex(-1);
            templateMovement = null;
        }
    }
}