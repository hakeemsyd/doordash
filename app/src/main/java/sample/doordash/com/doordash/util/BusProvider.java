package sample.doordash.com.doordash.util;

import com.squareup.otto.Bus;

/**
 * Created by Hakeem on 1/23/17.
 */

public class BusProvider {

    private static final MainThreadBus mBus = new MainThreadBus();
    public static Bus getBus(){
        return mBus;
    }

    private BusProvider(){

    }
}
