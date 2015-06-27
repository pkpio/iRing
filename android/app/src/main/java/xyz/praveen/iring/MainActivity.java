package xyz.praveen.iring;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import xyz.praveen.iring.accesscontrol.AccessController;
import xyz.praveen.iring.server.OnGadgetActionListener;
import xyz.praveen.iring.server.ServerHandle;
import xyz.praveen.iring.touchpattern.TouchHandler;


public class MainActivity extends AppCompatActivity implements OnGadgetActionListener {
    final String DEBUG_TAG = this.getClass().getName();

    AccessController mAccessControl;
    TextView helloView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloView = (TextView) findViewById(R.id.hello);
        helloView.setOnTouchListener(new TouchHandler(this));

        // Admin permission check
        mAccessControl = new AccessController();
        mAccessControl.checkDeviceAdminRights(this);

        // Init server
        ServerHandle mServerHandle = new ServerHandle(this);
        mServerHandle.startServer();
    }

    @Override
    public void onGadgetAction(final String action) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                helloView.setText(action);
            }
        });
    }
}
