package ch.bfh.btx8108.trinkster;

/**
 * class StatisticEntry for creating the pie chart with the category and quantity
 */
public class StatisticEntry {
    private String category;
    private Double quantity;

    /**
     * constructor for statistic entry
     * @param category - the drink category
     * @param quantity - the drink quantity
     */
    public StatisticEntry(String category, Double quantity) {
        this.category = category;
        this.quantity = quantity;
    }

    /**
     * gets the category of the drink
     * @return the drink category
     */
    public String getCategory() {
        return category;
    }

    /**
     * gets the quantity of the drink
     * @return the drink quantity
     */
    public Double getQuantity() {
        return quantity;
    }
}
