package com.lab04;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Random;

public class FormGenerator extends JDialog {
    private final GeneratorModel<EntityBoat, IOarDrawer> generator;
    private final JList<String> entityListUI;
    private final JList<String> oarListUI;
    private final DefaultListModel<String> entityListModel;
    private final DefaultListModel<String> oarListModel;
    private final JPanel previewPanel;
    private DrawingBoat generatedBoat;
    private boolean boatGenerated;

    public FormGenerator(JFrame parent) {
        super(parent, "Генератор лодок", true);
        this.generator = new GeneratorModel<>();
        this.entityListModel = new DefaultListModel<>();
        this.oarListModel = new DefaultListModel<>();
        this.entityListUI = new JList<>(entityListModel);
        this.oarListUI = new JList<>(oarListModel);
        this.previewPanel = new JPanel();
        this.boatGenerated = false;

        setSize(900, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Генерируем начальные данные
        generateInitialData();

        // Создаём интерфейс
        createUI();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void generateInitialData() {
        Random random = new Random();

        // Генерируем 3 случайных объекта типа-сущности
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.PINK};
        Color[] sailColors = {Color.WHITE, Color.YELLOW, Color.CYAN, Color.MAGENTA};

        for (int i = 0; i < 3; i++) {
            int speed = random.nextInt(200) + 100;
            double weight = random.nextInt(2000) + 1000;
            Color bodyColor = colors[random.nextInt(colors.length)];

            // Случайно выбираем: простая или продвинутая лодка
            if (random.nextBoolean()) {
                // Продвинутая
                Color sailColor = sailColors[random.nextInt(sailColors.length)];
                boolean hasSail = random.nextBoolean();
                EntityImprovedBoat improved = new EntityImprovedBoat();
                improved.init(speed, weight, bodyColor, sailColor, hasSail);
                generator.addEntity(improved);
            } else {
                // Простая
                EntityBoat simple = new EntityBoat();
                simple.init(speed, weight, bodyColor);
                generator.addEntity(simple);
            }
        }

        // Генерируем 3 случайных объекта интерфейса ИнтерДоп (вёсла)
        for (int i = 0; i < 3; i++) {
            int oarCount = random.nextInt(3) + 1;
            int type = random.nextInt(3);
            IOarDrawer oar;
            switch (type) {
                case 0:
                    oar = new Oar(OarCount.fromInt(oarCount));
                    break;
                case 1:
                    oar = new OarWithOrnament(OarCount.fromInt(oarCount));
                    break;
                default:
                    oar = new OarWithColorfulBlade(OarCount.fromInt(oarCount));
                    break;
            }
            generator.addOarDrawer(oar);
        }

        // Обновляем списки
        updateEntityList();
        updateOarList();
    }

    private void updateEntityList() {
        entityListModel.clear();
        for (EntityBoat entity : generator.getEntityList()) {
            entityListModel.addElement(generator.getEntityDescription(entity));
        }
    }

    private void updateOarList() {
        oarListModel.clear();
        for (IOarDrawer oar : generator.getOarList()) {
            oarListModel.addElement(generator.getOarDescription(oar));
        }
    }

    private void createUI() {
        // Левая панель со списками
        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        // Список сущностей
        JPanel entityPanel = new JPanel(new BorderLayout());
        entityPanel.setBorder(new TitledBorder("Сгенерированные сущности (лодки)"));
        JScrollPane entityScroll = new JScrollPane(entityListUI);
        entityScroll.setPreferredSize(new Dimension(300, 200));
        entityPanel.add(entityScroll, BorderLayout.CENTER);
        leftPanel.add(entityPanel);

        // Список вёсел
        JPanel oarPanel = new JPanel(new BorderLayout());
        oarPanel.setBorder(new TitledBorder("Сгенерированные вёсла"));
        JScrollPane oarScroll = new JScrollPane(oarListUI);
        oarScroll.setPreferredSize(new Dimension(300, 200));
        oarPanel.add(oarScroll, BorderLayout.CENTER);
        leftPanel.add(oarPanel);

        add(leftPanel, BorderLayout.WEST);

        // Центральная панель с предпросмотром
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new TitledBorder("Предпросмотр сгенерированной лодки"));

        previewPanel.setPreferredSize(new Dimension(400, 300));
        previewPanel.setBackground(Color.WHITE);
        previewPanel.setLayout(new BorderLayout());

        centerPanel.add(previewPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Правая панель с кнопками
        JPanel rightPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        rightPanel.setBorder(new TitledBorder("Управление"));

        JButton generateBtn = new JButton("Сгенерировать лодку");
        generateBtn.addActionListener(e -> generateBoat());

        JButton acceptBtn = new JButton("Принять и добавить в коллекцию");
        acceptBtn.addActionListener(e -> acceptBoat());

        JButton cancelBtn = new JButton("Отмена");
        cancelBtn.addActionListener(e -> {
            boatGenerated = false;
            dispose();
        });

        JButton regenerateBtn = new JButton("Перегенерировать данные");
        regenerateBtn.addActionListener(e -> {
            generateInitialData();
            updateEntityList();
            updateOarList();
        });

        rightPanel.add(generateBtn);
        rightPanel.add(acceptBtn);
        rightPanel.add(cancelBtn);
        rightPanel.add(regenerateBtn);

        add(rightPanel, BorderLayout.EAST);

        // Нижняя панель с информацией
        JLabel infoLabel = new JLabel("Нажмите 'Сгенерировать лодку' для создания случайной комбинации");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(infoLabel, BorderLayout.SOUTH);
    }

    private void generateBoat() {
        generatedBoat = generator.generateRandomBoat();
        if (generatedBoat != null) {
            boatGenerated = true;
            drawPreview(generatedBoat);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Не удалось сгенерировать лодку. Возможно, списки пусты.",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void drawPreview(DrawingBoat boat) {
        previewPanel.removeAll();

        JPanel drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (boat != null) {
                    boat.setPosition(50, 50);
                    boat.drawTransport(g);
                }
            }
        };
        drawPanel.setBackground(Color.WHITE);
        previewPanel.add(drawPanel, BorderLayout.CENTER);
        previewPanel.revalidate();
        previewPanel.repaint();
    }

    private void acceptBoat() {
        if (boatGenerated && generatedBoat != null) {
            JOptionPane.showMessageDialog(this,
                    "Лодка будет добавлена в коллекцию!",
                    "Успех",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Сначала сгенерируйте лодку!",
                    "Предупреждение",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public DrawingBoat getGeneratedBoat() {
        return boatGenerated ? generatedBoat : null;
    }

    public boolean isBoatGenerated() {
        return boatGenerated;
    }
}