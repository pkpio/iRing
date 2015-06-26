package xyz.praveen.iring;

import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import xyz.praveen.iring.AccessControl.AccessController;
import xyz.praveen.iring.TouchPattern.TouchHandler;


public class MainActivity extends AppCompatActivity {
    static final String DEBUG_TAG = "MotionTest";

    AccessController mAccessControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View hello = findViewById(R.id.hello);
        hello.setOnTouchListener(new TouchHandler(this));

        // Admin permission check
        mAccessControl = new AccessController();
        mAccessControl.checkDeviceAdminRights(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                Log.d(DEBUG_TAG, "Action was DOWN");
                return true;
            case (MotionEvent.ACTION_UP):
                //mAccessControl.lockDevice(this);
                Log.d(DEBUG_TAG, "Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL):
                Log.d(DEBUG_TAG, "Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                Log.d(DEBUG_TAG, "Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }
}
