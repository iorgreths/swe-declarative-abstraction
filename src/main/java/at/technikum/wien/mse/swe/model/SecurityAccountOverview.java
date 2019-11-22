package at.technikum.wien.mse.swe.model;

import at.technikum.wien.mse.swe.connector.CompoundField;
import at.technikum.wien.mse.swe.connector.SimpleField;
import at.technikum.wien.mse.swe.connector.FieldAlignment;

/**
 * @author MatthiasKreuzriegler
 */
public class SecurityAccountOverview {

    @SimpleField(begin = 40, length = 10, padding = "0", alignment = FieldAlignment.LEFT)
    private String accountNumber;
    @CompoundField(name = "code", begin = 50, length = 2, padding = " ", alignment = FieldAlignment.RIGHT)
    private RiskCategory riskCategory;
    @CompoundField(name = "firstname", begin = 82, length = 30, padding = " ", alignment = FieldAlignment.LEFT)
    @CompoundField(name="lastname", begin = 52, length = 30, padding = " ", alignment = FieldAlignment.LEFT)
    private DepotOwner depotOwner;
    @CompoundField(name = "currency", begin = 112, length = 3, padding = " ", alignment = FieldAlignment.RIGHT)
    @CompoundField(name = "value", begin = 115, length = 17, padding = " ", alignment = FieldAlignment.LEFT)
    private Amount balance;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public RiskCategory getRiskCategory() {
        return riskCategory;
    }

    public void setRiskCategory(RiskCategory riskCategory) {
        this.riskCategory = riskCategory;
    }

    public DepotOwner getDepotOwner() {
        return depotOwner;
    }

    public void setDepotOwner(DepotOwner depotOwner) {
        this.depotOwner = depotOwner;
    }

    public Amount getBalance() {
        return balance;
    }

    public void setBalance(Amount balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "SecurityAccountOverview{" +
                "accountNumber='" + accountNumber + '\'' +
                ", riskCategory=" + riskCategory +
                ", depotOwner=" + depotOwner +
                ", balance=" + balance +
                '}';
    }
}
