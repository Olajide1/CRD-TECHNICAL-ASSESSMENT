package org.crd.tests;

import org.crd.Portfolio;
import org.crd.RebalanceCalculator;
import org.crd.Security;
import org.crd.TradeOrder;
import org.crd.data.TestData;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * RebalanceCalculatorTest
 *
 * Automated tests for the Portfolio Rebalancing Assessment
 *
 * Answers two questions:
 *   Q1. Output — Number of shares to buy/sell
 *   Q2. What do you have to do to get to zero target variance?
 */
public class RebalanceCalculatorTest {

    // MODULE 1 — PORTFOLIO INPUT & VALIDATION
    @Test(description = "All 5 Account ABC securities can be entered successfully")
    public void validInputAccepted() {
        List<TradeOrder> orders = RebalanceCalculator.rebalance(TestData.ACCOUNT_ABC);
        assertThat(orders).isNotEmpty();
    }

    @Test(description = "Application does not allow calculation when total assets is zero")
    public void zeroTotalAssetsBlocked() {
        assertThatThrownBy(() -> RebalanceCalculator.rebalance(TestData.ZERO_ASSETS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive number");
    }

    @Test(description = "Application does not allow calculation when total assets is negative")
    public void negativeTotalAssetsBlocked() {
        assertThatThrownBy(() -> RebalanceCalculator.rebalance(TestData.NEGATIVE_ASSETS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive number");
    }

    @Test(description = "Application does not allow calculation when unit price is zero")
    public void zeroUnitPriceBlocked() {
        Portfolio portfolio = new Portfolio(
                List.of(TestData.ZERO_UNIT_PRICE), TestData.TOTAL_ASSETS);

        assertThatThrownBy(() -> RebalanceCalculator.rebalance(portfolio))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("positive number");
    }

    @Test(description = "Application does not allow calculation when total target allocation exceeds 100%")
    public void totalTargetExceeds100Blocked() {
        assertThatThrownBy(() -> RebalanceCalculator.rebalance(TestData.EXCEEDS_100_PCT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exceeds 100%");
    }





    // Covers all 5 securities in one test using DataProvider


    @DataProvider(name = "securityData")
    public Object[][] securityData() {
        return TestData.SECURITY_CALCULATIONS;
    }

    @Test(
            dataProvider = "securityData",
            description = "correct action and shares for each security"
    )
    public void Parameterized_allSecurities(Security security,
                                              String expectedAction,
                                              int expectedShares) {
        TradeOrder order = RebalanceCalculator.calculate(
                security, TestData.TOTAL_ASSETS);

        assertThat(order.getAction()).isEqualTo(expectedAction);
        assertThat(order.getShares()).isEqualTo(expectedShares);
    }


    // MODULE 4 — ROUNDING & PRECISION
    @Test(description = "Shares are always rounded down to avoid exceeding the budget")
    public void sharesAlwaysRoundedDown() {
        TradeOrder order = RebalanceCalculator.calculate(
                TestData.IBM_ROUNDING, TestData.TOTAL_ASSETS);

        // floor(10,000 / 150) = 66, not 67
        // 67 × $150 = $10,050 — would exceed the $10,000 budget
        assertThat(order.getShares()).isEqualTo(66);
        assertThat(order.getShares()).isNotEqualTo(67);
    }

    @Test(description = "Residual cash from rounding is not treated as an error")
    public void residualCashNotAnError() {
        TradeOrder order = RebalanceCalculator.calculate(
                TestData.IBM, TestData.TOTAL_ASSETS);

        // 66 × $150 = $9,900. $100 left over — valid, not an error
        assertThat(order.getShares()).isEqualTo(TestData.IBM_EXPECTED_SHARES);
        assertThat(order.getAction()).isEqualTo(TestData.IBM_EXPECTED_ACTION);
    }

    @Test(description = "Application correctly handles decimal stock prices")
    public void decimalUnitPriceHandledCorrectly() {
        TradeOrder order = RebalanceCalculator.calculate(
                TestData.DECIMAL_PRICE, TestData.TOTAL_ASSETS);

        // floor(10,000 / 149.87) = 66
        assertThat(order.getShares()).isEqualTo(66);
        assertThat(order.getAction()).isEqualTo("BUY");
    }


    // MODULE 5 — TRADE OUTPUT


    @Test(description = "Results contain one row for every security entered")
    public void oneRowPerSecurity() {
        List<TradeOrder> orders = RebalanceCalculator.rebalance(TestData.ACCOUNT_ABC);
        assertThat(orders).hasSize(5);
    }

    @Test(description = "Each row displays the correct Security name")
    public void correctSecurityLabels() {
        List<TradeOrder> orders = RebalanceCalculator.rebalance(TestData.ACCOUNT_ABC);

        assertThat(orders)
                .extracting(TradeOrder::getSecurity)
                .containsExactly("IBM", "MSFT", "ORCL", "AAPL", "HD");
    }

    @Test(description = "Every security shows a valid action of BUY, SELL or HOLD")
    public void validActionValues() {
        List<TradeOrder> orders = RebalanceCalculator.rebalance(TestData.ACCOUNT_ABC);

        assertThat(orders)
                .extracting(TradeOrder::getAction)
                .containsOnly("BUY", "SELL", "HOLD");
    }

    @Test(description = "Securities that need no trade show 0 shares")
    public void holdRowsShowZeroShares() {
        List<TradeOrder> orders = RebalanceCalculator.rebalance(TestData.ACCOUNT_ABC);

        orders.stream()
                .filter(o -> o.getAction().equals("HOLD"))
                .forEach(o ->
                        assertThat(o.getShares())
                                .as(o.getSecurity() + " shares should be 0")
                                .isEqualTo(0));
    }
}
