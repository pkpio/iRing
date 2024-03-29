package xyz.praveen.iring.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;

import xyz.praveen.iring.R;
import xyz.praveen.iring.access.AccessController;
import xyz.praveen.iring.access.EventBox;
import xyz.praveen.iring.gadgeteer.OnGadgetActionListener;
import xyz.praveen.iring.gadgeteer.ServerHandle;
import xyz.praveen.iring.touch.OnTouchActionListener;
import xyz.praveen.iring.touch.TouchHandler;
import xyz.praveen.iring.util.Globals;
import xyz.praveen.iring.util.OnHitrateChangeListener;

import static xyz.praveen.iring.util.LogUtils.LOGD;
import static xyz.praveen.iring.util.LogUtils.LOGI;
import static xyz.praveen.iring.util.LogUtils.makeLogTag;


public class MainActivity extends AppCompatActivity implements OnGadgetActionListener,
        OnTouchActionListener, OnHitrateChangeListener {
    final static String TAG = makeLogTag(MainActivity.class);

    AccessController mAccessControl;
    MyPagerAdapter myPagerAdapter;
    ViewPager mPager;

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
        mPager = (ViewPager) findViewById(R.id.content_pager);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(myPagerAdapter);
        mPager.setOffscreenPageLimit(myPagerAdapter.getCount());

        // Touch view init
        findViewById(R.id.touchview).setOnTouchListener(new TouchHandler(this, this));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.sliding_tabs);
        tabs.setViewPager(mPager);

        // Give a Context and Hitrate listener for EventBox
        EventBox.mContext = this;
        EventBox.hitrateChangeListener = this;
    }

    @Override
    public void onGadgetAction(final int action) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Consume mode change events and return
                if (action == MODE_LOCK || action == MODE_CTRL) {
                    mode = action;

                    // Set title of current mode
                    if (mode == MODE_CTRL)
                        setTitle("Remote control");
                    else
                        setTitle("Intelli lock");

                    return;
                }

                if (mode == MODE_LOCK)
                    // Lock mode => Pass events to EventBox
                    EventBox.sendGadgetEvent(action, System.currentTimeMillis());

                else
                    // CTRL mode => Handle the control of UI
                    doUIControl(action);
            }
        });
    }

    @Override
    public void onTouchAction(int action) {
        if (mode == MODE_LOCK)
            // Lock mode => Pass events to EventBox
            EventBox.sendTouchEvent(action);
        LOGD(TAG, "Touch event : " + action);
    }

    @Override
    public void onHitrateUpdate(float hitrate) {
        setTitle("Intelli lock    Hit rate : " + hitrate);
    }

    void doUIControl(int action) {
        LOGI(TAG, "Handle control for action : " + action);

        switch (action) {
            case Globals.MOVEMENT_LEFT:
            case Globals.MOVEMENT_DOWN:
                int curPos = mPager.getCurrentItem();
                if (curPos > 0)
                    mPager.setCurrentItem(curPos - 1);
                break;
            case Globals.MOVEMENT_RIGHT:
            case Globals.MOVEMENT_UP:
                int curPos1 = mPager.getCurrentItem();
                if (curPos1 < myPagerAdapter.getCount() - 1)
                    mPager.setCurrentItem(curPos1 + 1);
                break;
        }

    }

    /**
     * A sample pagerAdapter for different pages of images
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        public final String[] TITLES = {"Page1", "Page2", "Page3", "Page4", "Page5", "Page6",
                "Page7", "Page8", "Page9", "Page10", "Page11", "Page12"};

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
