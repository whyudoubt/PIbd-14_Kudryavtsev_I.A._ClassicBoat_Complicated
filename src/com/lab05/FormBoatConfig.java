package com.lab05;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormBoatConfig extends JDialog {
    private DrawingBoat currentBoat;
    private boolean isImproved;
    private JSpinner speedSpinner;
    private JSpinner weightSpinner;
    private JSpinner oarCountSpinner;
    private JCheckBox hasSailCheckBox;
    private JPanel previewPanel;
    private IOarDrawer selectedOar;
    private JLabel oarTypeLabel;
    private JLabel statusLabel;
    private JButton bodyColorBtn;
    private JButton sailColorBtn;

    public interface BoatCreatedListener {
        void onBoatCreated(DrawingBoat boat);
    }
    private BoatCreatedListener listener;

    public FormBoatConfig(JFrame parent, BoatCreatedListener listener) {
        super(parent, "Создание новой лодки", true);
        this.listener = listener;
        this.currentBoat = null;
        this.isImproved = false;
        this.selectedOar = new Oar(OarCount.TWO);

        setSize(800, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        createUI();
    }

    private void createUI() {
        // Левая панель с параметрами
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Параметры лодки
        JPanel paramsPanel = new JPanel(new GridBagLayout());
        paramsPanel.setBorder(BorderFactory.createTitledBorder("Параметры лодки"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        paramsPanel.add(new JLabel("Скорость (100-300):"), gbc);
        gbc.gridx = 1;
        speedSpinner = new JSpinner(new SpinnerNumberModel(200, 100, 300, 10));
        paramsPanel.add(speedSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        paramsPanel.add(new JLabel("Вес (1000-3000):"), gbc);
        gbc.gridx = 1;
        weightSpinner = new JSpinner(new SpinnerNumberModel(2000, 1000, 3000, 100));
        paramsPanel.add(weightSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        paramsPanel.add(new JLabel("Количество вёсел (1-3):"), gbc);
        gbc.gridx = 1;
        oarCountSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 3, 1));
        paramsPanel.add(oarCountSpinner, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        hasSailCheckBox = new JCheckBox("Есть парус (только для продвинутой)");
        hasSailCheckBox.setEnabled(false);
        paramsPanel.add(hasSailCheckBox, gbc);

        leftPanel.add(paramsPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Кнопки выбора цветов
        JPanel colorPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        colorPanel.setBorder(BorderFactory.createTitledBorder("Цвета"));

        JLabel bodyColorLabel = new JLabel("Цвет корпуса:");
        bodyColorBtn = new JButton("Выбрать цвет корпуса");
        bodyColorBtn.setBackground(new Color(200, 200, 255));
        bodyColorBtn.setOpaque(true);
        bodyColorBtn.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Выберите цвет корпуса", bodyColorBtn.getBackground());
            if (color != null) {
                bodyColorBtn.setBackground(color);
                if (currentBoat != null) {
                    currentBoat.changeBodyColor(color);
                    previewPanel.repaint();
                }
            }
        });

        JLabel sailColorLabel = new JLabel("Цвет паруса:");
        sailColorBtn = new JButton("Выбрать цвет паруса");
        sailColorBtn.setBackground(new Color(255, 200, 200));
        sailColorBtn.setOpaque(true);
        sailColorBtn.setEnabled(false);
        sailColorBtn.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Выберите цвет паруса", sailColorBtn.getBackground());
            if (color != null) {
                sailColorBtn.setBackground(color);
                if (currentBoat instanceof DrawingImprovedBoat improved) {
                    improved.changeAdditionalColor(color);
                    previewPanel.repaint();
                }
            }
        });

        colorPanel.add(bodyColorLabel);
        colorPanel.add(bodyColorBtn);
        colorPanel.add(sailColorLabel);
        colorPanel.add(sailColorBtn);

        leftPanel.add(colorPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Панель выбора типа лодки
        JPanel typePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        typePanel.setBorder(BorderFactory.createTitledBorder("Выбор типа (перетаскиванием)"));

        JLabel simpleLabel = new JLabel("Простая лодка", SwingConstants.CENTER);
        simpleLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        simpleLabel.setOpaque(true);
        simpleLabel.setBackground(new Color(200, 255, 200));
        simpleLabel.setPreferredSize(new Dimension(120, 40));
        setupDragSource(simpleLabel, "SIMPLE");

        JLabel improvedLabel = new JLabel("Продвинутая лодка", SwingConstants.CENTER);
        improvedLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        improvedLabel.setOpaque(true);
        improvedLabel.setBackground(new Color(255, 200, 200));
        improvedLabel.setPreferredSize(new Dimension(120, 40));
        setupDragSource(improvedLabel, "IMPROVED");

        typePanel.add(simpleLabel);
        typePanel.add(improvedLabel);
        leftPanel.add(typePanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Панель выбора типа вёсел
        JPanel oarPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        oarPanel.setBorder(BorderFactory.createTitledBorder("Выбор типа вёсел (перетаскиванием)"));

        JLabel oarNormal = new JLabel("Обычные вёсла", SwingConstants.CENTER);
        oarNormal.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        oarNormal.setOpaque(true);
        oarNormal.setBackground(new Color(220, 220, 220));
        setupDragSource(oarNormal, new Oar(OarCount.TWO));

        JLabel oarOrnament = new JLabel("Вёсла с орнаментом", SwingConstants.CENTER);
        oarOrnament.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        oarOrnament.setOpaque(true);
        oarOrnament.setBackground(new Color(220, 220, 220));
        setupDragSource(oarOrnament, new OarWithOrnament(OarCount.TWO));

        JLabel oarColorful = new JLabel("Разноцветные вёсла", SwingConstants.CENTER);
        oarColorful.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        oarColorful.setOpaque(true);
        oarColorful.setBackground(new Color(220, 220, 220));
        setupDragSource(oarColorful, new OarWithColorfulBlade(OarCount.TWO));

        oarPanel.add(oarNormal);
        oarPanel.add(oarOrnament);
        oarPanel.add(oarColorful);
        leftPanel.add(oarPanel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        oarTypeLabel = new JLabel("Выбранные вёсла: обычные");
        leftPanel.add(oarTypeLabel);

        statusLabel = new JLabel("Перетащите тип лодки и вёсла в область предпросмотра");
        statusLabel.setForeground(Color.BLUE);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(statusLabel);

        add(leftPanel, BorderLayout.WEST);

        // Панель предпросмотра
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Предпросмотр (перетащите сюда)"));

        previewPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (currentBoat != null) {
                    currentBoat.setPosition(20, 20);
                    currentBoat.drawTransport(g);
                }
            }
        };
        previewPanel.setBackground(Color.WHITE);
        previewPanel.setPreferredSize(new Dimension(300, 300));
        setupDropTarget(previewPanel);

        centerPanel.add(previewPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Кнопки внизу
        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton createButton = new JButton("Создать и добавить");
        createButton.addActionListener(e -> createAndAddBoat());

        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> dispose());

        bottomPanel.add(createButton);
        bottomPanel.add(cancelButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Слушатели
        speedSpinner.addChangeListener(e -> updateCurrentBoat());
        weightSpinner.addChangeListener(e -> updateCurrentBoat());
        oarCountSpinner.addChangeListener(e -> updateOarCount());
        hasSailCheckBox.addChangeListener(e -> updateCurrentBoat());
    }

    // Настройка источника перетаскивания для строки
    private void setupDragSource(JLabel label, String data) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Transferable transferable = new StringTransferable(data);
                label.getTransferHandler().exportAsDrag(label, e, TransferHandler.COPY);
            }
        });
        label.setTransferHandler(new TransferHandler() {
            @Override
            public int getSourceActions(JComponent c) {
                return COPY;
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                return new StringTransferable(data);
            }
        });
    }

    // Настройка источника перетаскивания для вёсел
    private void setupDragSource(JLabel label, IOarDrawer oar) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Transferable transferable = new OarTransferable(oar);
                label.getTransferHandler().exportAsDrag(label, e, TransferHandler.COPY);
            }
        });
        label.setTransferHandler(new TransferHandler() {
            @Override
            public int getSourceActions(JComponent c) {
                return COPY;
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
                return new OarTransferable(oar);
            }
        });
    }

    // Настройка цели перетаскивания
    private void setupDropTarget(JPanel panel) {
        panel.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.stringFlavor) ||
                        support.isDataFlavorSupported(OarTransferable.OAR_FLAVOR);
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) return false;

                try {
                    Transferable t = support.getTransferable();
                    if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        String data = (String) t.getTransferData(DataFlavor.stringFlavor);
                        handleDropData(data);
                        return true;
                    } else if (t.isDataFlavorSupported(OarTransferable.OAR_FLAVOR)) {
                        IOarDrawer oar = (IOarDrawer) t.getTransferData(OarTransferable.OAR_FLAVOR);
                        setSelectedOar(oar);
                        return true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });
    }

    // Transferable для строки
    static class StringTransferable implements Transferable {
        static final DataFlavor[] FLAVORS = {DataFlavor.stringFlavor};
        private final String data;

        StringTransferable(String data) {
            this.data = data;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return FLAVORS;
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(DataFlavor.stringFlavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (flavor.equals(DataFlavor.stringFlavor)) {
                return data;
            }
            throw new UnsupportedFlavorException(flavor);
        }
    }

    // Transferable для вёсел
    static class OarTransferable implements Transferable {
        static final DataFlavor OAR_FLAVOR = new DataFlavor(IOarDrawer.class, "IOarDrawer");
        static final DataFlavor[] FLAVORS = {OAR_FLAVOR};
        private final IOarDrawer oar;

        OarTransferable(IOarDrawer oar) {
            this.oar = oar;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return FLAVORS;
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(OAR_FLAVOR);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (flavor.equals(OAR_FLAVOR)) {
                return oar;
            }
            throw new UnsupportedFlavorException(flavor);
        }
    }

    private void handleDropData(String data) {
        if ("SIMPLE".equals(data)) {
            isImproved = false;
            hasSailCheckBox.setEnabled(false);
            hasSailCheckBox.setSelected(false);
            sailColorBtn.setEnabled(false);
            createBoat();
            statusLabel.setText("Выбрана простая лодка");
        } else if ("IMPROVED".equals(data)) {
            isImproved = true;
            hasSailCheckBox.setEnabled(true);
            sailColorBtn.setEnabled(true);
            createBoat();
            statusLabel.setText("Выбрана продвинутая лодка");
        }
    }

    private void setSelectedOar(IOarDrawer oar) {
        this.selectedOar = oar;
        String typeName;
        if (oar instanceof OarWithOrnament) {
            typeName = "с орнаментом";
        } else if (oar instanceof OarWithColorfulBlade) {
            typeName = "разноцветные";
        } else {
            typeName = "обычные";
        }
        oarTypeLabel.setText("Выбранные вёсла: " + typeName);
        statusLabel.setText("Выбраны вёсла: " + typeName);

        int count = (Integer) oarCountSpinner.getValue();
        selectedOar.setOarCount(count);

        updateCurrentBoat();
    }

    private void updateOarCount() {
        int count = (Integer) oarCountSpinner.getValue();
        if (selectedOar != null) {
            selectedOar.setOarCount(count);
        }
        updateCurrentBoat();
    }

    private void createBoat() {
        int speed = (Integer) speedSpinner.getValue();
        double weight = (Integer) weightSpinner.getValue();
        boolean hasSail = hasSailCheckBox.isSelected();

        Color bodyColor = bodyColorBtn.getBackground();
        Color sailColor = sailColorBtn.getBackground();

        IOarDrawer oarCopy = copyOarWithCount(selectedOar, (Integer) oarCountSpinner.getValue());

        if (isImproved) {
            currentBoat = new DrawingImprovedBoat(speed, weight, bodyColor, sailColor, hasSail);
        } else {
            currentBoat = new DrawingBoat(speed, weight, bodyColor);
        }
        currentBoat.setOarDrawer(oarCopy);

        previewPanel.repaint();
    }

    private void updateCurrentBoat() {
        if (currentBoat == null) return;

        int speed = (Integer) speedSpinner.getValue();
        double weight = (Integer) weightSpinner.getValue();
        boolean hasSail = hasSailCheckBox.isSelected() && isImproved;

        Color bodyColor = bodyColorBtn.getBackground();
        Color sailColor = sailColorBtn.getBackground();

        currentBoat.updateSpeed(speed);
        currentBoat.updateWeight(weight);
        currentBoat.changeBodyColor(bodyColor);

        if (currentBoat instanceof DrawingImprovedBoat improved) {
            improved.updateHasSail(hasSail);
            improved.changeAdditionalColor(sailColor);
        }

        IOarDrawer oarCopy = copyOarWithCount(selectedOar, (Integer) oarCountSpinner.getValue());
        currentBoat.setOarDrawer(oarCopy);

        previewPanel.repaint();
    }

    private IOarDrawer copyOarWithCount(IOarDrawer original, int count) {
        OarCount oarCount = OarCount.fromInt(count);
        if (original instanceof OarWithOrnament) {
            return new OarWithOrnament(oarCount);
        } else if (original instanceof OarWithColorfulBlade) {
            return new OarWithColorfulBlade(oarCount);
        } else {
            return new Oar(oarCount);
        }
    }

    private void createAndAddBoat() {
        if (currentBoat == null) {
            JOptionPane.showMessageDialog(this, "Сначала выберите тип лодки!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (listener != null) {
            listener.onBoatCreated(currentBoat);
        }
        dispose();
    }
}