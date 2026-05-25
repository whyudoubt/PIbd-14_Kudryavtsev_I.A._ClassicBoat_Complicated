package com.lab04;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main extends JFrame {
    private final CanvasForBoat canvas;
    private DirectionType checkBordersState;
    private DrawingBoat currentBoat;
    private BaseTemplateMovement templateMovement;
    private JComboBox<String> destinationCombo;
    private JComboBox<String> oarTypeComboBox;
    private JPanel drawingPanel;

    public Main() {
        setTitle("Лабораторная работа №2 (усложнённая) - Лодка с вёслами");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        canvas = new CanvasForBoat();
        checkBordersState = DirectionType.NONE;
        currentBoat = null;
        templateMovement = null;

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

        // Верхняя панель (выбор типа вёсел и цели)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JLabel oarTypeLabel = new JLabel("Тип вёсел:");
        oarTypeComboBox = new JComboBox<>(new String[]{"Обычные", "С орнаментом", "Разноцветные"});

        JLabel destinationLabel = new JLabel("Цель перемещения:");
        destinationCombo = new JComboBox<>(new String[]{"К центру", "В правый нижний угол"});
        // Комбобокс всегда активен
        destinationCombo.setEnabled(true);
        destinationCombo.addActionListener(e -> changeDestination());

        topPanel.add(oarTypeLabel);
        topPanel.add(oarTypeComboBox);
        topPanel.add(destinationLabel);
        topPanel.add(destinationCombo);
        add(topPanel, BorderLayout.NORTH);

        // Нижняя панель с кнопками
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton createSimpleButton = new JButton("Создать (простая)");
        JButton createImprovedButton = new JButton("Создать (продвинутая)");
        JButton checkBordersButton = new JButton("Проверка границ");
        JButton stepButton = new JButton("Шаг");
        JButton leftButton = new JButton("←");
        JButton rightButton = new JButton("→");
        JButton upButton = new JButton("↑");
        JButton downButton = new JButton("↓");

        createSimpleButton.addActionListener(e -> createBoat(false));
        createImprovedButton.addActionListener(e -> createBoat(true));
        checkBordersButton.addActionListener(e -> checkBorders());
        stepButton.addActionListener(e -> stepMove());
        leftButton.addActionListener(e -> moveBoat(DirectionType.LEFT));
        rightButton.addActionListener(e -> moveBoat(DirectionType.RIGHT));
        upButton.addActionListener(e -> moveBoat(DirectionType.UP));
        downButton.addActionListener(e -> moveBoat(DirectionType.DOWN));

        buttonPanel.add(createSimpleButton);
        buttonPanel.add(createImprovedButton);
        buttonPanel.add(checkBordersButton);
        buttonPanel.add(stepButton);
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

    private void createBoat(boolean isImproved) {
        Random random = new Random();

        int speed = random.nextInt(200) + 100;
        double weight = random.nextInt(2000) + 1000;
        Color bodyColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

        // Создаём вёсла выбранного типа
        int oarCountValue = random.nextInt(3) + 1;
        int oarType = oarTypeComboBox.getSelectedIndex();
        IOarDrawer oarDrawer = createOarDrawer(oarType, oarCountValue);

        if (isImproved) {
            Color sailColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            boolean hasSail = random.nextBoolean();
            DrawingImprovedBoat boat = new DrawingImprovedBoat(speed, weight, bodyColor, sailColor, hasSail);
            boat.setOarDrawer(oarDrawer);
            currentBoat = boat;

            if (canvas.insertBoat(boat)) {
                canvas.setBoatPosition(random.nextInt(90) + 10, random.nextInt(90) + 10);
                // Не отключаем комбобокс
                destinationCombo.setSelectedIndex(-1);
                templateMovement = null;
                drawingPanel.repaint();

                String sailStatus = hasSail ? "есть" : "нет";
                String oarTypeName = getOarTypeName(oarType);
                setTitle(String.format("Лодка (продвинутая) | Скорость: %d | Вес: %.0f | Шаг: %d | Парус: %s | Вёсла: %s (%d шт.)",
                        speed, weight, (int) Math.round(boat.getBoatStep()), sailStatus, oarTypeName, oarCountValue));
            } else {
                JOptionPane.showMessageDialog(this, "Объект слишком большой для поля!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            DrawingBoat boat = new DrawingBoat(speed, weight, bodyColor);
            boat.setOarDrawer(oarDrawer);
            currentBoat = boat;

            if (canvas.insertBoat(boat)) {
                canvas.setBoatPosition(random.nextInt(90) + 10, random.nextInt(90) + 10);
                // Для простой лодки комбобокс отключать не нужно, просто сбрасываем выбор
                destinationCombo.setSelectedIndex(-1);
                templateMovement = null;
                drawingPanel.repaint();

                String oarTypeName = getOarTypeName(oarType);
                setTitle(String.format("Лодка (простая) | Скорость: %d | Вес: %.0f | Шаг: %d | Вёсла: %s (%d шт.)",
                        speed, weight, (int) Math.round(boat.getBoatStep()), oarTypeName, oarCountValue));
            } else {
                JOptionPane.showMessageDialog(this, "Объект слишком большой для поля!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
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

    private String getOarTypeName(int type) {
        return switch (type) {
            case 0 -> "обычные";
            case 1 -> "с орнаментом";
            default -> "разноцветные";
        };
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

    // Обработчик изменения выбора в комбобоксе - просто обновляем стратегию
    private void changeDestination() {
        if (currentBoat == null) return;

        // Проверяем, что лодка продвинутая (только у неё есть парус и смысл двигаться к цели)
        if (!(currentBoat instanceof DrawingImprovedBoat)) {
            // Для простой лодки просто сбрасываем выбор
            destinationCombo.setSelectedIndex(-1);
            return;
        }

        int selectedIndex = destinationCombo.getSelectedIndex();
        if (selectedIndex == -1) {
            templateMovement = null;
            return;
        }

        // Создаём новую стратегию в соответствии с выбранной целью
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
            // Не отключаем комбобокс, просто сбрасываем выбор
            destinationCombo.setSelectedIndex(-1);
            templateMovement = null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main frame = new Main();
            frame.setVisible(true);
        });
    }
}