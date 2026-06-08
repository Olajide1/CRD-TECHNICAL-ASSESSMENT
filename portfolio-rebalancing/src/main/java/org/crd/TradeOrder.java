package org.crd;

/**
 * TradeOrder
 * Represents the calculated trade instruction for one security.
 * This is the output the assessment is asking us to validate.
 */
public class TradeOrder {

    private String security;
    private String action;        // "BUY", "SELL" or "HOLD"
    private int    shares;        // whole shares only — floor rounded
    private double dollarAmount;  // positive = buy, negative = sell

    public TradeOrder(String security, String action,
                      int shares, double dollarAmount) {
        this.security       = security;
        this.action       = action;
        this.shares       = shares;
        this.dollarAmount = dollarAmount;
    }

    public String getSecurity()      { return security;       }
    public String getAction()      { return action;       }
    public int    getShares()      { return shares;       }
    public double getDollarAmount(){ return dollarAmount; }

    @Override
    public String toString() {
        return security + " → " + action + " " + shares + " shares";
    }
}
