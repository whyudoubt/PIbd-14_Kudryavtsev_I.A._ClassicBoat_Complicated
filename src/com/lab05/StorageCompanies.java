package com.lab05;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class StorageCompanies {
    private final Map<String, AbstractCompany> companies;

    // Коллекция усложнения (по варианту 34 — LinkedList)
    private final LinkedList<DrawingBoat> deletedBoats;

    public StorageCompanies() {
        this.companies = new HashMap<>();
        this.deletedBoats = new LinkedList<>();
    }

    public void addCompany(String name, CollectionType collectionType, int pictureWidth, int pictureHeight) {
        if (name == null || name.trim().isEmpty()) return;

        String key = name + "_" + collectionType;
        if (companies.containsKey(key)) return;

        ICollectionGenericObjects<DrawingBoat> collection = switch (collectionType) {
            case LIST -> new ListGenericObjects<>();
            case LINKED_LIST -> new LinkedListGenericObjects<>();
            default -> new MassiveGenericObjects<>();
        };

        HarborCompany company = new HarborCompany(pictureWidth, pictureHeight, collection);
        company.setStorageReference(this, key);
        companies.put(key, company);
    }

    public void removeCompany(String key) {
        companies.remove(key);
    }

    public AbstractCompany getCompany(String key) {
        return companies.get(key);
    }

    public Set<String> getCompanyKeys() {
        return companies.keySet();
    }

    // Индексатор с 2 параметрами (имитация C# индексатора)
    // Первый параметр — ключ компании, второй — позиция лодки в компании
    public DrawingBoat getBoat(String companyKey, int boatPosition) {
        AbstractCompany company = companies.get(companyKey);
        if (company == null) return null;
        return company.getBoatAt(boatPosition);
    }

    public void setBoat(String companyKey, int boatPosition, DrawingBoat boat) {
        AbstractCompany company = companies.get(companyKey);
        if (company != null && boat != null) {
            company.setBoatAt(boatPosition, boat);
        }
    }

    // Коллекция удалённых лодок
    public void addToDeletedBoats(DrawingBoat boat) {
        if (boat != null) {
            deletedBoats.addLast(boat);
        }
    }

    public DrawingBoat getLastDeletedBoat() {
        return deletedBoats.isEmpty() ? null : deletedBoats.getLast();
    }

    public DrawingBoat getDeletedBoatByIndex(int index) {
        if (index < 0 || index >= deletedBoats.size()) return null;
        return deletedBoats.get(index);
    }

    public boolean hasDeletedBoats() {
        return !deletedBoats.isEmpty();
    }

    public int getDeletedBoatsCount() {
        return deletedBoats.size();
    }

    public void clearDeletedBoats() {
        deletedBoats.clear();
    }
}