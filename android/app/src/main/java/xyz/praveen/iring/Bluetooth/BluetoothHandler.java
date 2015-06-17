package xyz.praveen.iring.Bluetooth;

import android.content.Context;
import android.util.Log;

/**
 * Created by praveen on 17/6/15.
 */
public class BluetoothHandler {
    final static String DEBUG_TAG = "IRingBluetooth";
    Context mContext;

    public BluetoothHandler(Context context) {
        this.mContext = context;
    }

    public void init() {
        Log.d(DEBUG_TAG, "Init called");
        // -TODO-
        // All bluetooth related setup
    }
}
