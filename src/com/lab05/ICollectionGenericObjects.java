package com.lab05;

// Интерфейс для работы с коллекцией объектов
public interface ICollectionGenericObjects<T> {
    // Количество объектов в коллекции
    int getCountObjects();

    // Максимальное количество элементов
    int getMaxCount();
    void setMaxCount(int maxCount);

    // Получение объекта по позиции
    T getObject(int position);

    // Добавление объекта (в конец или на первую свободную позицию)
    boolean insertObject(T obj);

    // Добавление объекта на конкретную позицию
    boolean insertObject(T obj, int position);

    // Удаление объекта с позиции
    boolean removeObject(int position);
}