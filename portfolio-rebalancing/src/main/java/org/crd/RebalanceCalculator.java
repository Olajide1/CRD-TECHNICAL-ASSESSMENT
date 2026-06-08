package org.crd;

import java.util.ArrayList;
import java.util.List;

/**
 * RebalanceCalculator
 *
 * Core calculation engine for the Portfolio Rebalancing Assessment.
 *
 * Formula:
 *   variance     = targetPct - currentPct
 *   dollarAmount = (variance / 100) * totalAssets
 *   shares       = Math.floor(|dollarAmount| / unitPrice)
 *   action       = variance > 0 → BUY
 *                  variance < 0 → SELL
 *                  variance = 0 → HOLD
 */
public class RebalanceCalculator {

    public static TradeOrder calculate(Security security, double totalAssets) {

        if (security == null) {
            throw new IllegalArgumentException("Security cannot be null");
        }
        if (totalAssets <= 0) {
            throw new IllegalArgumentException(
                    "Total assets must be a positive number");
        }
        if (security.getUnitPrice() <= 0) {
            throw new IllegalArgumentException(
                    security.getSecurity() + ": unit price must be a positive number");
        }

        double variance     = security.getTargetPct() - security.getCurrentPct();
        double dollarAmount = (variance / 100.0) * totalAssets;
        int    shares       = (int) Math.floor(
                Math.abs(dollarAmount) / security.getUnitPrice());

        String action;
        if      (variance > 0) action = "BUY";
        else if (variance < 0) action = "SELL";
        else                   action = "HOLD";

        return new TradeOrder(security.getSecurity(), action, shares, dollarAmount);
    }

    public static List<TradeOrder> rebalance(Portfolio portfolio) {

        if (portfolio == null) {
            throw new IllegalArgumentException("Portfolio cannot be null");
        }
        if (portfolio.getTotalAssets() <= 0) {
            throw new IllegalArgumentException(
                    "Total assets must be a positive number");
        }

        double totalTarget = portfolio.getSecurities()
                .stream()
                .mapToDouble(Security::getTargetPct)
                .sum();

        if (totalTarget > 100) {
            throw new IllegalArgumentException(
                    "Total target allocation " + totalTarget + "% exceeds 100%");
        }

        List<TradeOrder> orders = new ArrayList<>();
        for (Security security : portfolio.getSecurities()) {
            orders.add(calculate(security, portfolio.getTotalAssets()));
        }
        return orders;
    }
}
