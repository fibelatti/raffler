package com.fibelatti.raffler.helpers;

import com.squareup.otto.Bus;

/**
 * Created by fibelatti on 03/08/16.
 */
public class BusHelper {
    private static BusHelper instance = new BusHelper();

    public static BusHelper getInstance() {
        return instance;
    }

    private final Bus BUS = new Bus();

    private BusHelper() {
    }

    public Bus getBus() {
        return BUS;
    }
}
