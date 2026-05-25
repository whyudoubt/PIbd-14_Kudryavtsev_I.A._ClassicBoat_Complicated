package com.lab05;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Параметризованный класс с двумя параметрами
public class GeneratorModel<T1 extends EntityBoat, T2 extends IOarDrawer> {

    private final List<T1> entityList;
    private final List<T2> oarList;
    private final Random random;

    public GeneratorModel() {
        this.entityList = new ArrayList<>();
        this.oarList = new ArrayList<>();
        this.random = new Random();
    }

    public void addEntity(T1 entity) {
        if (entity != null) {
            entityList.add(entity);
        }
    }

    public void addOarDrawer(T2 oarDrawer) {
        if (oarDrawer != null) {
            oarList.add(oarDrawer);
        }
    }

    // Генерация случайной лодки (может быть как простой, так и продвинутой)
    public DrawingBoat generateRandomBoat() {
        if (entityList.isEmpty() || oarList.isEmpty()) {
            return null;
        }

        // Случайный выбор сущности
        T1 selectedEntity = entityList.get(random.nextInt(entityList.size()));

        // Случайный выбор вёсел
        T2 selectedOar = oarList.get(random.nextInt(oarList.size()));

        DrawingBoat boat;

        if (selectedEntity instanceof EntityImprovedBoat improved) {
            // Продвинутая лодка
            boat = new DrawingImprovedBoat(
                    improved.getSpeed(),
                    improved.getWeight(),
                    improved.getBodyColor(),
                    improved.getSailColor(),
                    improved.hasSail()
            );
        } else {
            // Простая лодка
            boat = new DrawingBoat(
                    selectedEntity.getSpeed(),
                    selectedEntity.getWeight(),
                    selectedEntity.getBodyColor()
            );
        }

        // Устанавливаем вёсла
        boat.setOarDrawer(selectedOar);

        return boat;
    }

    public List<T1> getEntityList() {
        return entityList;
    }

    public List<T2> getOarList() {
        return oarList;
    }

    public String getEntityDescription(T1 entity) {
        if (entity instanceof EntityImprovedBoat improved) {
            return String.format("Продвинутая: скорость=%d, вес=%.0f, цвет=%s, цвет паруса=%s, парус=%s",
                    improved.getSpeed(),
                    improved.getWeight(),
                    colorToRussian(improved.getBodyColor()),
                    colorToRussian(improved.getSailColor()),
                    improved.hasSail() ? "есть" : "нет"
            );
        } else {
            return String.format("Простая: скорость=%d, вес=%.0f, цвет=%s",
                    entity.getSpeed(),
                    entity.getWeight(),
                    colorToRussian(entity.getBodyColor())
            );
        }
    }

    public String getOarDescription(T2 oar) {
        String typeName;
        if (oar instanceof OarWithOrnament) {
            typeName = "с орнаментом";
        } else if (oar instanceof OarWithColorfulBlade) {
            typeName = "разноцветные";
        } else {
            typeName = "обычные";
        }
        return String.format("%s, количество=%d", typeName, oar.getOarCount());
    }

    private String colorToRussian(Color color) {
        if (color.equals(Color.RED)) return "красный";
        if (color.equals(Color.GREEN)) return "зелёный";
        if (color.equals(Color.BLUE)) return "синий";
        if (color.equals(Color.YELLOW)) return "жёлтый";
        if (color.equals(Color.ORANGE)) return "оранжевый";
        if (color.equals(Color.PINK)) return "розовый";
        if (color.equals(Color.CYAN)) return "голубой";
        if (color.equals(Color.MAGENTA)) return "пурпурный";
        if (color.equals(Color.WHITE)) return "белый";
        if (color.equals(Color.BLACK)) return "чёрный";
        if (color.equals(Color.GRAY)) return "серый";
        return "неизвестный";
    }
}