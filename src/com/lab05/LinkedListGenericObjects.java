package com.lab05;

import java.util.LinkedList;

// Реализация коллекции на LinkedList (связный список)
public class LinkedListGenericObjects<T> implements ICollectionGenericObjects<T> {
    private final LinkedList<T> collection;
    private int maxCount;

    public LinkedListGenericObjects() {
        this.collection = new LinkedList<>();
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
                collection.removeLast();
            }
        }
    }

    @Override
    public T getObject(int position) {
        if (position < 0 || position >= collection.size()) return null;

        var current = collection.listIterator(position);
        return current.hasNext() ? current.next() : null;
    }

    @Override
    public boolean insertObject(T obj) {
        if (obj == null) return false;
        if (collection.size() >= maxCount) return false;
        collection.addLast(obj);
        return true;
    }

    @Override
    public boolean insertObject(T obj, int position) {
        if (obj == null) return false;
        if (collection.size() >= maxCount) return false;
        if (position < 0) position = 0;

        if (position == 0) {
            collection.addFirst(obj);
            return true;
        }

        if (position >= collection.size()) {
            collection.addLast(obj);
            return true;
        }

        // Вставка в середину
        var iterator = collection.listIterator(position);
        iterator.add(obj);
        return true;
    }

    @Override
    public boolean removeObject(int position) {
        if (position < 0 || position >= collection.size()) return false;
        collection.remove(position);
        return true;
    }
}