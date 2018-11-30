package ch.bfh.btx8108.trinkster;

import java.time.LocalDateTime;

/**
 * Domain-Objekt des Typs Drink (Container für Daten).
 * Drink repräsentiert ein konsumiertes Getränk.
 */
public class Drink {
    private long id;

    private Category category;

    private String name;
    private double quantity;

    private LocalDateTime dateTime;


    public Drink(long id, Category category, String name, double quantity, LocalDateTime dateTime) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.quantity = quantity;
        this.dateTime = dateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return name + ", " + quantity;
    }
}
