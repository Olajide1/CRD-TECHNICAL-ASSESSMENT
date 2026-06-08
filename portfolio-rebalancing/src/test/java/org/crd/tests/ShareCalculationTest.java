package org.crd.tests;

import org.crd.RebalanceCalculator;
import org.crd.Security;
import org.crd.TradeOrder;
import org.crd.data.TestData;
import org.crd.utils.TestUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ShareCalculationTest
 *
 * Directly answers Assessment Question 1:
 * "Output — Number of shares to buy/sell"
 */
public class ShareCalculationTest {

    // MODULE 2 — SHARE CALCULATION
    @Test(description = "IBM is under-allocated → BUY 66 shares")
    public void ibmBuy66Shares() {
        TradeOrder order = RebalanceCalculator.calculate(
                TestData.IBM, TestData.TOTAL_ASSETS);

        assertThat(order.getAction()).isEqualTo(TestData.IBM_EXPECTED_ACTION);
        assertThat(order.getShares()).isEqualTo(TestData.IBM_EXPECTED_SHARES);
    }

    @Test(description = "ORCL is over-allocated then SELL 45 shares")
    public void orclSell45Shares() {
        TradeOrder order = RebalanceCalculator.calculate(
                TestData.ORCL, TestData.TOTAL_ASSETS);

        assertThat(order.getAction()).isEqualTo(TestData.ORCL_EXPECTED_ACTION);
        assertThat(order.getShares()).isEqualTo(TestData.ORCL_EXPECTED_SHARES);
    }

    @Test(description = "MSFT, AAPL and HD are balanced then HOLD with 0 shares")
    public void holdSecurities() {
        TradeOrder msft = RebalanceCalculator.calculate(
                TestData.MSFT, TestData.TOTAL_ASSETS);
        TradeOrder aapl = RebalanceCalculator.calculate(
                TestData.AAPL, TestData.TOTAL_ASSETS);
        TradeOrder hd = RebalanceCalculator.calculate(
                TestData.HD, TestData.TOTAL_ASSETS);

        assertThat(msft.getAction()).isEqualTo(TestData.MSFT_EXPECTED_ACTION);
        assertThat(msft.getShares()).isEqualTo(TestData.MSFT_EXPECTED_SHARES);

        assertThat(aapl.getAction()).isEqualTo(TestData.AAPL_EXPECTED_ACTION);
        assertThat(aapl.getShares()).isEqualTo(TestData.AAPL_EXPECTED_SHARES);

        assertThat(hd.getAction()).isEqualTo(TestData.HD_EXPECTED_ACTION);
        assertThat(hd.getShares()).isEqualTo(TestData.HD_EXPECTED_SHARES);
    }

    @Test(
            description      = "Full Account ABC produces correct output for all 5 securities"

    )
    public void fullAccountAbcScenario() {
        List<TradeOrder> orders = RebalanceCalculator.rebalance(TestData.ACCOUNT_ABC);

        TradeOrder ibm  = TestUtils.getOrder(orders, "IBM");
        TradeOrder msft = TestUtils.getOrder(orders, "MSFT");
        TradeOrder orcl = TestUtils.getOrder(orders, "ORCL");
        TradeOrder aapl = TestUtils.getOrder(orders, "AAPL");
        TradeOrder hd   = TestUtils.getOrder(orders, "HD");

        assertThat(ibm.getAction()).isEqualTo(TestData.IBM_EXPECTED_ACTION);
        assertThat(ibm.getShares()).isEqualTo(TestData.IBM_EXPECTED_SHARES);

        assertThat(msft.getAction()).isEqualTo(TestData.MSFT_EXPECTED_ACTION);
        assertThat(msft.getShares()).isEqualTo(TestData.MSFT_EXPECTED_SHARES);

        assertThat(orcl.getAction()).isEqualTo(TestData.ORCL_EXPECTED_ACTION);
        assertThat(orcl.getShares()).isEqualTo(TestData.ORCL_EXPECTED_SHARES);

        assertThat(aapl.getAction()).isEqualTo(TestData.AAPL_EXPECTED_ACTION);
        assertThat(aapl.getShares()).isEqualTo(TestData.AAPL_EXPECTED_SHARES);

        assertThat(hd.getAction()).isEqualTo(TestData.HD_EXPECTED_ACTION);
        assertThat(hd.getShares()).isEqualTo(TestData.HD_EXPECTED_SHARES);
    }

    @Test(description = "Stock price exceeds dollar allocation → 0 shares, no error")
    public void stockPriceExceedsAllocation() {
        TradeOrder order = RebalanceCalculator.calculate(
                TestData.EXPENSIVE_STOCK, TestData.TOTAL_ASSETS);

        assertThat(order.getAction()).isEqualTo("BUY");
        assertThat(order.getShares()).isEqualTo(0);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // MODULE 2 — PARAMETERIZED TEST
    // Covers all 5 securities in one test using DataProvider
    // ═══════════════════════════════════════════════════════════════════════

    @DataProvider(name = "securityData")
    public Object[][] securityData() {
        return TestData.SECURITY_CALCULATIONS;
    }

    @Test(
            dataProvider = "securityData",
            description  = "correct action and shares for each security"
    )
    public void tcParameterized_allSecurities(Security security,
                                              String expectedAction,
                                              int expectedShares) {
        TradeOrder order = RebalanceCalculator.calculate(
                security, TestData.TOTAL_ASSETS);

        assertThat(order.getAction()).isEqualTo(expectedAction);
        assertThat(order.getShares()).isEqualTo(expectedShares);
    }
}


