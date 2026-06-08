package org.crd;

/**
 * Security
 * Represents one security in the portfolio.
 */
public class Security {

    private String security;
    private double targetPct;
    private double currentPct;
    private double unitPrice;

    public Security(String security, double targetPct,
                    double currentPct, double unitPrice) {
        this.security     = security;
        this.targetPct  = targetPct;
        this.currentPct = currentPct;
        this.unitPrice  = unitPrice;
    }

    public String getSecurity()     { return security;     }
    public double getTargetPct()  { return targetPct;  }
    public double getCurrentPct() { return currentPct; }
    public double getUnitPrice()  { return unitPrice;  }
}
