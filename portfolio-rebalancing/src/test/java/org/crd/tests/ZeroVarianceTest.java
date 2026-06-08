package org.crd.tests;

import org.crd.RebalanceCalculator;
import org.crd.TradeOrder;
import org.crd.data.TestData;
import org.crd.utils.TestUtils;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ZeroVarianceTest {
    @Test(description = "Applying BUY 66 IBM and SELL 45 ORCL removes all variance")
    public void tradesAchieveZeroVariance() {
        List<TradeOrder> orders = RebalanceCalculator.rebalance(
                TestData.ACCOUNT_ABC_POST_TRADE);

        orders.forEach(order ->
                assertThat(order.getAction())
                        .as(order.getSecurity() + " should be HOLD after trades applied")
                        .isEqualTo("HOLD"));
    }

    @Test(
            description = "After trades are executed no further trades are needed",
            dependsOnMethods = "tradesAchieveZeroVariance"
    )
    public void noFurtherTradesAfterRebalance() {
        List<TradeOrder> orders = RebalanceCalculator.rebalance(
                TestData.ACCOUNT_ABC_POST_TRADE);

        orders.forEach(order -> {
            assertThat(order.getAction())
                    .as(order.getSecurity() + " action should be HOLD")
                    .isEqualTo("HOLD");
            assertThat(order.getShares())
                    .as(order.getSecurity() + " shares should be 0")
                    .isEqualTo(0);
        });
    }

    @Test(description = "Portfolio can be rebalanced without adding any extra money")
    public void rebalanceIsSelfFunding() {
        List<TradeOrder> orders = RebalanceCalculator.rebalance(TestData.ACCOUNT_ABC);

        TradeOrder ibm = TestUtils.getOrder(orders, "IBM");
        TradeOrder orcl = TestUtils.getOrder(orders, "ORCL");

        // IBM dollarAmount = +$10,000 (buy)
        // ORCL dollarAmount = -$10,000 (sell)
        // net cash = $0
        assertThat(ibm.getDollarAmount() + orcl.getDollarAmount())
                .as("Net cash required should be zero and rebalance is self-funding")
                .isEqualTo(0.0);
    }

    @Test(description = "A portfolio already at its target shows no trades are needed")
    public void alreadyBalancedPortfolio() {
        List<TradeOrder> orders = RebalanceCalculator.rebalance(
                TestData.ACCOUNT_ABC_BALANCED);

        orders.forEach(order -> {
            assertThat(order.getAction())
                    .as(order.getSecurity() + " should be HOLD — already at target")
                    .isEqualTo("HOLD");
            assertThat(order.getShares())
                    .as(order.getSecurity() + " shares should be 0")
                    .isEqualTo(0);
        });
    }
}

