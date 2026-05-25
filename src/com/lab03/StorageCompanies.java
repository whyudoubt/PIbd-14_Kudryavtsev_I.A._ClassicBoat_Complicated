package com.lab03;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Хранилище компаний
public class StorageCompanies {
    private final Map<String, AbstractCompany> companies;

    public StorageCompanies() {
        this.companies = new HashMap<>();
    }

    public void addCompany(String name, CollectionType type, int pictureWidth, int pictureHeight) {
        if (name == null || name.trim().isEmpty()) return;

        String key = name + "_" + type;
        if (companies.containsKey(key)) return;

        ICollectionGenericObjects<DrawingBoat> collection;
        switch (type) {
            case LIST:
                // Можно добавить ListGenericObjects позже
                collection = new MassiveGenericObjects<>();
                break;
            case LINKED_LIST:
                // Можно добавить LinkedListGenericObjects позже
                collection = new MassiveGenericObjects<>();
                break;
            default:
                collection = new MassiveGenericObjects<>();
                break;
        }

        AbstractCompany company = new HarborCompany(pictureWidth, pictureHeight, collection);
        companies.put(key, company);
    }

    public void removeCompany(String name) {
        if (name == null) return;
        companies.remove(name);
    }

    public AbstractCompany getCompany(String name) {
        return companies.get(name);
    }

    public List<String> getStorageKeys() {
        return new ArrayList<>(companies.keySet());
    }
}