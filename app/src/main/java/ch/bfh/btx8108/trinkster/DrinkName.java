package ch.bfh.btx8108.trinkster;

public class DrinkName {
    private String name;
    private Double quantity;

    public DrinkName(String name, Double quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public Double getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return name + ", " + quantity;
    }

}
