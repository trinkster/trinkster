package ch.bfh.btx8108.trinkster;

public class StatisticEntry {
    private String category;
    private Double quantity;

    public StatisticEntry(String category, Double quantity) {
        this.category = category;
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public Double getQuantity() {
        return quantity;
    }
}
