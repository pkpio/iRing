package xyz.praveen.iring;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;

import xyz.praveen.iring.accesscontrol.AccessController;
import xyz.praveen.iring.server.OnGadgetActionListener;
import xyz.praveen.iring.server.ServerHandle;
import xyz.praveen.iring.touchpattern.OnTouchListener;

import static xyz.praveen.iring.util.LogUtils.makeLogTag;


public class MainActivity extends AppCompatActivity implements OnGadgetActionListener,
        OnTouchListener {
    final static String TAG = makeLogTag(MainActivity.class);

    AccessController mAccessControl;

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
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.sliding_tabs);
        tabs.setViewPager(pager);
    }

    @Override
    public void onGadgetAction(final int action) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // -TODO- Action
            }
        });
    }

    @Override
    public void onTouchEvent(int event) {

    }

    /**
     *
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Categories", "Home", "Top Paid", "Top Free", "Top Grossing", "Top New Paid",
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
