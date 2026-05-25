package com.lab04;

// Реализация коллекции на массиве
public class MassiveGenericObjects<T> implements ICollectionGenericObjects<T> {
    private Object[] collection;

    public MassiveGenericObjects() {
        this.collection = new Object[0];
    }

    @Override
    public int getCountObjects() {
        int count = 0;
        for (Object obj : collection) {
            if (obj != null) count++;
        }
        return count;
    }

    @Override
    public int getMaxCount() {
        return collection.length;
    }

    @Override
    public void setMaxCount(int maxCount) {
        if (maxCount > 0) {
            Object[] newArray = new Object[maxCount];
            System.arraycopy(collection, 0, newArray, 0, Math.min(collection.length, maxCount));
            this.collection = newArray;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getObject(int position) {
        if (position < 0 || position >= collection.length) return null;
        return (T) collection[position];
    }

    @Override
    public boolean insertObject(T obj) {
        return insertObject(obj, 0);
    }

    @Override
    public boolean insertObject(T obj, int position) {
        if (obj == null) return false;
        if (position < 0) position = 0;

        // Расширяем массив если нужно
        if (position >= collection.length) {
            Object[] newArray = new Object[position + 1];
            System.arraycopy(collection, 0, newArray, 0, collection.length);
            collection = newArray;
        }

        // Если место свободно - вставляем
        if (collection[position] == null) {
            collection[position] = obj;
            return true;
        }

        // Ищем свободное место справа
        for (int i = position + 1; i < collection.length; i++) {
            if (collection[i] == null) {
                collection[i] = obj;
                return true;
            }
        }

        // Ищем свободное место слева
        for (int i = position - 1; i >= 0; i--) {
            if (collection[i] == null) {
                collection[i] = obj;
                return true;
            }
        }

        // Нет свободных мест - расширяем массив
        Object[] newArray = new Object[collection.length + 1];
        System.arraycopy(collection, 0, newArray, 0, collection.length);
        newArray[collection.length] = obj;
        collection = newArray;
        return true;
    }

    @Override
    public boolean removeObject(int position) {
        if (position < 0 || position >= collection.length) return false;
        if (collection[position] == null) return false;

        collection[position] = null;
        return true;
    }
}