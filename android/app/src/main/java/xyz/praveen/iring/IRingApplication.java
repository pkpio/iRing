package xyz.praveen.iring;

import android.app.Application;

import xyz.praveen.iring.Bluetooth.BluetoothHandler;

/**
 * Created by praveen on 17/6/15.
 */
public class IRingApplication extends Application {
    BluetoothHandler mBluetoothHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        // Init bluetooth
        mBluetoothHandler = new BluetoothHandler(this);
        mBluetoothHandler.init();
    }
}
