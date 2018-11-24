package ch.bfh.btx8108.trinkster;

public class Drink {
    private long id;

    private Category category;

    private String name;
    private double quantity;
    public Drink(long id, Category category, String name, double quantity) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.quantity = quantity;
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

    @Override
    public String toString() {
        return name + ", " + quantity;
    }
}
