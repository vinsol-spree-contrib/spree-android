package com.vinsol.spree.utils;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by vaibhav on 11/2/15.
 */
public final class BusProvider {
    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
