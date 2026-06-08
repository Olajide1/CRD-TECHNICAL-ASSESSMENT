package org.crd.data;
import org.crd.Portfolio;
import org.crd.Security;

import java.util.List;

/**
 * TestData
 *
 * All fixed test data taken directly from the assessment brief — Account ABC.
 * Matches exactly the test cases in the manual test case spreadsheet TC1-TC19.
 */
public class TestData {

    // ── Portfolio ─────────────────────────────────────────────────────────────

    public static final double TOTAL_ASSETS = 100_000;

    // ── Individual Securities — from assessment brief ─────────────────────────

    public static final Security IBM  = new Security("IBM",  20, 10, 150.0);
    public static final Security MSFT = new Security("MSFT", 20, 20,  90.0);
    public static final Security ORCL = new Security("ORCL", 20, 30, 220.0);
    public static final Security AAPL = new Security("AAPL", 20, 20, 450.0);
    public static final Security HD   = new Security("HD",   20, 20,  70.0);

    // ── Full Portfolio — Account ABC ──────────────────────────────────────────

    public static final Portfolio ACCOUNT_ABC = new Portfolio(
            List.of(IBM, MSFT, ORCL, AAPL, HD),
            TOTAL_ASSETS
    );

    // ── Post Trade Portfolio ──────────────────────────────────────────────────
    // After BUY 66 IBM and SELL 45 ORCL are applied
    // Used in TC10 and TC11

    public static final Portfolio ACCOUNT_ABC_POST_TRADE = new Portfolio(
            List.of(
                    new Security("IBM",  20, 20, 150.0),
                    new Security("MSFT", 20, 20,  90.0),
                    new Security("ORCL", 20, 20, 220.0),
                    new Security("AAPL", 20, 20, 450.0),
                    new Security("HD",   20, 20,  70.0)
            ),
            TOTAL_ASSETS
    );

    // ── Already Balanced Portfolio ────────────────────────────────────────────
    // Used in TC13 — target% = current% from the start

    public static final Portfolio ACCOUNT_ABC_BALANCED = new Portfolio(
            List.of(
                    new Security("IBM",  20, 20, 150.0),
                    new Security("MSFT", 20, 20,  90.0),
                    new Security("ORCL", 20, 20, 220.0),
                    new Security("AAPL", 20, 20, 450.0),
                    new Security("HD",   20, 20,  70.0)
            ),
            TOTAL_ASSETS
    );

    // ── Edge Case Securities ──────────────────────────────────────────────────

    // TC9 — price ($2,000) exceeds dollar allocation ($1,000) → 0 shares
    public static final Security EXPENSIVE_STOCK =
            new Security("PRICEY", 11, 10, 2000.0);

    // TC14 — IBM at $150: floor(10,000 / 150) = 66, not 67
    // 67 would cost $10,050 — exceeding the $10,000 budget
    public static final Security IBM_ROUNDING =
            new Security("IBM", 20, 10, 150.0);

    // TC16 (decimal price) — $149.87 → floor(10,000 / 149.87) = 66
    public static final Security DECIMAL_PRICE =
            new Security("IBM", 20, 10, 149.87);

    // ── Invalid Data — used in validation tests ───────────────────────────────

    // TC4 — zero unit price
    public static final Security ZERO_UNIT_PRICE =
            new Security("IBM", 20, 10, 0.0);

    // TC2 — zero total assets
    public static final Portfolio ZERO_ASSETS = new Portfolio(
            List.of(IBM), 0
    );

    // TC3 — negative total assets
    public static final Portfolio NEGATIVE_ASSETS = new Portfolio(
            List.of(IBM), -50_000
    );

    // TC5 (missing TC number) — total target exceeds 100%
    public static final Portfolio EXCEEDS_100_PCT = new Portfolio(
            List.of(
                    new Security("IBM",  60, 50, 150.0),
                    new Security("ORCL", 60, 50, 220.0)
            ),
            TOTAL_ASSETS
    );

    // ── Expected Outputs — correct answers from the assessment ────────────────

    public static final String IBM_EXPECTED_ACTION  = "BUY";
    public static final int    IBM_EXPECTED_SHARES  = 66;

    public static final String ORCL_EXPECTED_ACTION = "SELL";
    public static final int    ORCL_EXPECTED_SHARES = 45;

    public static final String MSFT_EXPECTED_ACTION = "HOLD";
    public static final int    MSFT_EXPECTED_SHARES = 0;

    public static final String AAPL_EXPECTED_ACTION = "HOLD";
    public static final int    AAPL_EXPECTED_SHARES = 0;

    public static final String HD_EXPECTED_ACTION   = "HOLD";
    public static final int    HD_EXPECTED_SHARES   = 0;

    // ── DataProvider data — used for parameterized test ───────────────────────
    // Covers TC5, TC6, TC7 and TC8 in one parameterized test

    public static final Object[][] SECURITY_CALCULATIONS = {
            { IBM,  "BUY",  66 },
            { MSFT, "HOLD",  0 },
            { ORCL, "SELL", 45 },
            { AAPL, "HOLD",  0 },
            { HD,   "HOLD",  0 },
    };
}
