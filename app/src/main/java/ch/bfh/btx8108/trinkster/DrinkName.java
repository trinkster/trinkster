package ch.bfh.btx8108.trinkster;

/**
 * class drink name
 * for detail view in statistic
 * shows the quantity for a drink name, if more than one drink of this class is consumed in the
 * defined timeline, the quantity is counted together
 */
public class DrinkName {
    private String name;
    private Double quantity;

    /**
     * constructor for drink name
     * @param name - drink name
     * @param quantity - drink quantity
     */
    public DrinkName(String name, Double quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    /**
     * gets the name of the drink
     * @return drink name
     */
    public String getName() {
        return name;
    }

    /**
     * gets the quantity of the drink
     * @return drink quantity
     */
    public Double getQuantity() {
        return quantity;
    }

    /**
     * defines how the drink name is displayed
     * @return string for drink name
     */
    @Override
    public String toString() {
        return name + ", " + quantity;
    }

}
