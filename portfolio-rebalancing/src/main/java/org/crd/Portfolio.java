package org.crd;

import java.util.List;

/**
 * Portfolio
 * Holds the list of securities and the total assets value.
 */
public class Portfolio {

    private List<Security> securities;
    private double totalAssets;

    public Portfolio(List<Security> securities, double totalAssets) {
        this.securities  = securities;
        this.totalAssets = totalAssets;
    }

    public List<Security> getSecurities() { return securities;  }
    public double getTotalAssets()        { return totalAssets; }
}
