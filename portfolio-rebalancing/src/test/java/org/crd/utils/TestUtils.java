package org.crd.utils;

import org.crd.TradeOrder;

import java.util.List;

public class TestUtils {

    public static TradeOrder getOrder(List<TradeOrder> orders, String security) {
        return orders.stream()
                .filter(o -> o.getSecurity().equals(security))
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError(security + " not found in results"));
    }
}
