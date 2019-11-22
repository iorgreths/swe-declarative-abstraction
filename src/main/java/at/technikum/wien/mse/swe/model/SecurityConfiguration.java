package at.technikum.wien.mse.swe.model;

import at.technikum.wien.mse.swe.connector.CompoundField;
import at.technikum.wien.mse.swe.connector.FieldAlignment;
import at.technikum.wien.mse.swe.connector.SimpleField;

/**
 * @author MatthiasKreuzriegler
 */
public class SecurityConfiguration {

    @CompoundField(name = "value", begin = 40, length = 12, padding = " ", alignment = FieldAlignment.LEFT)
    private ISIN isin;
    @CompoundField(name = "code", begin = 52, length = 2, padding = " ", alignment = FieldAlignment.LEFT)
    private RiskCategory riskCategory;
    @SimpleField(begin = 54, length = 30, padding = " ", alignment = FieldAlignment.LEFT)
    private String name;
    @CompoundField(name = "currency", begin = 84, length = 3, padding = " ", alignment = FieldAlignment.LEFT)
    @CompoundField(name = "value", begin = 87, length = 10, padding = " ", alignment = FieldAlignment.LEFT)
    private Amount yearHighest;
    @CompoundField(name = "currency", begin = 84, length = 3, padding = " ", alignment = FieldAlignment.LEFT)
    @CompoundField(name = "value", begin = 97, length = 10, padding = " ", alignment = FieldAlignment.LEFT)
    private Amount yearLowest;

    public ISIN getIsin() {
        return isin;
    }

    public void setIsin(ISIN isin) {
        this.isin = isin;
    }

    public RiskCategory getRiskCategory() {
        return riskCategory;
    }

    public void setRiskCategory(RiskCategory riskCategory) {
        this.riskCategory = riskCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Amount getYearHighest() {
        return yearHighest;
    }

    public void setYearHighest(Amount yearHighest) {
        this.yearHighest = yearHighest;
    }

    public Amount getYearLowest() {
        return yearLowest;
    }

    public void setYearLowest(Amount yearLowest) {
        this.yearLowest = yearLowest;
    }
}
