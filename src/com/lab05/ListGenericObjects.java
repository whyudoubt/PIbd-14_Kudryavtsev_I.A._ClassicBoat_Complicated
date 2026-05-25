package com.lab05;

import java.util.ArrayList;
import java.util.List;

// Реализация коллекции на ArrayList (список)
public class ListGenericObjects<T> implements ICollectionGenericObjects<T> {
    private final List<T> collection;
    private int maxCount;

    public ListGenericObjects() {
        this.collection = new ArrayList<>();
        this.maxCount = 100;
    }

    @Override
    public int getCountObjects() {
        return collection.size();
    }

    @Override
    public int getMaxCount() {
        return maxCount;
    }

    @Override
    public void setMaxCount(int maxCount) {
        if (maxCount > 0) {
            this.maxCount = maxCount;
            while (collection.size() > maxCount) {
                collection.remove(collection.size() - 1);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getObject(int position) {
        if (position < 0 || position >= collection.size()) return null;
        return collection.get(position);
    }

    @Override
    public boolean insertObject(T obj) {
        if (obj == null) return false;
        if (collection.size() >= maxCount) return false;
        return collection.add(obj);
    }

    @Override
    public boolean insertObject(T obj, int position) {
        if (obj == null) return false;
        if (collection.size() >= maxCount) return false;
        if (position < 0) position = 0;
        if (position > collection.size()) position = collection.size();
        collection.add(position, obj);
        return true;
    }

    @Override
    public boolean removeObject(int position) {
        if (position < 0 || position >= collection.size()) return false;
        collection.remove(position);
        return true;
    }
}