package xyz.praveen.iring;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;

import xyz.praveen.iring.accesscontrol.AccessController;
import xyz.praveen.iring.accesscontrol.EventBox;
import xyz.praveen.iring.server.OnGadgetActionListener;
import xyz.praveen.iring.server.ServerHandle;
import xyz.praveen.iring.touchpattern.OnTouchActionListener;
import xyz.praveen.iring.touchpattern.TouchHandler;

import static xyz.praveen.iring.util.LogUtils.LOGD;
import static xyz.praveen.iring.util.LogUtils.makeLogTag;


public class MainActivity extends AppCompatActivity implements OnGadgetActionListener,
        OnTouchActionListener {
    final static String TAG = makeLogTag(MainActivity.class);

    AccessController mAccessControl;

    final int MODE_LOCK = 5;
    final int MODE_CTRL = 6;
    int mode = MODE_LOCK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Admin permission check
        mAccessControl = new AccessController();
        AccessController.checkDeviceAdminRights(this);

        // Init server
        ServerHandle mServerHandle = new ServerHandle(this);
        mServerHandle.startServer();

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) findViewById(R.id.content_pager);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(adapter.getCount());

        // Touch view init
        findViewById(R.id.touchview).setOnTouchListener(new TouchHandler(this, this));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.sliding_tabs);
        tabs.setViewPager(pager);

        // Give a mContext for EventBox
        EventBox.mContext = this;
    }

    @Override
    public void onGadgetAction(final int action) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Consume mode change events and return
                if (action == MODE_LOCK || action == MODE_CTRL) {
                    mode = action;
                    return;
                }

                if (mode == MODE_LOCK)
                    // Lock mode => Pass events to EventBox
                    EventBox.sendGadgetEvent(action, System.currentTimeMillis());

                else
                    // CTRL mode => Handle the control of UI
                    LOGD(TAG, "Handle control for action : " + action);
            }
        });
    }

    @Override
    public void onTouchAction(int action) {
        EventBox.sendTouchEvent(action);
    }


    /**
     * A sample pager for different pages of images
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        public final String[] TITLES = {"Categories", "Home", "Top Paid", "Top Free", "Top Grossing", "Top New Paid",
                "Top New Free", "Trending"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return SampleFragment.newInstance(position);
        }

    }
}
