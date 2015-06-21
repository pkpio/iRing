package xyz.praveen.iring.AccessControl;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import xyz.praveen.iring.IRingAdminReceiver;

/**
 * Created by praveen on 17/6/15.
 */
public class AccessController {
    static final String DEBUG_TAG = AccessController.class.getSimpleName();

    //-TODO- Implement auto locking of devices based on events from Bluetooth and OnScreen

    /**
     * Lock the screen. Possible only if admin rights are available
     *
     * @param context Context required to get a DevicePolicyManager
     */
    public static void lockDevice(Context context) {
        DevicePolicyManager deviceManger = (DevicePolicyManager) context.getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        try {
            Log.d(DEBUG_TAG, "Lock requested");
            deviceManger.lockNow();
        } catch (SecurityException e) {
            Log.d(DEBUG_TAG, "No permissions given yet");
            e.printStackTrace();
        }
    }

    /**
     * Checks if device admin settings are enabled for our app. Requests them otherwise.
     *
     * @param activity Current activity
     */
    public static void checkDeviceAdminRights(Activity activity) {
        ComponentName compName = new ComponentName(activity, IRingAdminReceiver.class);
        DevicePolicyManager deviceManger = (DevicePolicyManager) activity.getSystemService(
                Context.DEVICE_POLICY_SERVICE);

        // If no permissions yet
        if (!deviceManger.isAdminActive(compName)) {
            Log.d(DEBUG_TAG, "Admin access requested");
            Intent intent = new Intent(DevicePolicyManager
                    .ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    compName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "Additional text explaining why this needs to be added.");
            activity.startActivityForResult(intent, 5);
        }
    }
}
