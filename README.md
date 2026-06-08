# CRD-TECHNICAL-ASSESSMENT

# ASSEMSMERNT ANSWERS

### Q1 — Number of shares to buy/sell

Based on Account ABC with $100,000 total assets the recommended trades are:
- IBM:  BUY  66 shares  ($10,000 / $150 = 66.67, rounded down to  66)
- ORCL: SELL 45 shares  ($10,000 / $220 = 45.45, rounded down to 45)
- MSFT, AAPL and HD require no trades — they are already at their target allocation

### Q2 — What do you have to do to reach zero target variance?

BUY 66 shares of IBM and SELL 45 shares of ORCL.
MSFT, AAPL and HD are already at their target allocation and require no action.

---

## Assumptions

A few assumptions were made in terms of how I shaped executing both manual and automated test cases

- Fractional shares are not supported — share counts are always floored
- Rounding down is deliberate — rounding up would exceed the dollar budget
- Residual cash from rounding is expected, not an error (IBM $100, ORCL $100)
- Rebalance is self-funding — ORCL sell proceeds ($9,900) cover the IBM purchase ($9,900), no additional cash is needed
- Unit prices are static inputs — no live market data is fetched

---

## Code Structure

### Main Classes

**Security.java**
Holds the data for one stock — security, target%, current% and unit price.

**Portfolio.java**
Groups all securities together with the total assets value.

**RebalanceCalculator.java**
The core calculation engine. Takes a portfolio and calculates what to do with each security.
- Variance = target% - current%
- Dollar amount = (variance / 100) x total assets
- Shares = floor(dollar amount / unit price)
- Positive variance = BUY, Negative = SELL, Zero = HOLD

**TradeOrder.java**
Holds the output for one security — action, shares and dollar amount.

### How the Tests Work

**TestData.java**
All fixed assessment data in one place. Tests reference this instead of hardcoding values.

**TestUtils.java**
Shared helper method for finding a trade order by ticker — used across all test classes.

**ShareCalculationTest.java**
Answers Q1 — verifies the correct number of shares to buy or sell for each security.

**ZeroVarianceTest.java**
Answers Q2 — verifies that executing the recommended trades brings all variances to zero.

**RebalanceCalculatorTest.java**
Supporting tests covering three areas:
- Input validation — verifies the application correctly blocks invalid inputs
- Rounding and precision  — verifies shares are always floored and residual cash is handled correctly
- Trade output  — verifies the results table displays the correct structure and values


