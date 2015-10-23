package trainning.chat.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import trainning.chat.R;
import trainning.chat.adapter.ViewPagerAdapter;
import trainning.chat.customview.SlidingTabLayout;
import trainning.chat.entity.TabItem;
import trainning.chat.entity.chatroom.MessageChat;
import trainning.chat.fragment.HistoryFragment;
import trainning.chat.fragment.ListUserFragment;
import trainning.chat.fragment.SettingFragment;
import trainning.chat.gcm.GCMConfig;
import trainning.chat.util.MySharePreferences;

/**
 * Created by ASUS on 10/10/2015.
 */
public class HomeActivity extends FragmentActivity {
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;
    private List<TabItem> tabItems;
    private static SlidingTabLayout mSlidingTabLayout;
    private String[] titles = {"Message", "Contact", "Setting"};
    private Fragment[] fragments = {new HistoryFragment(), new ListUserFragment(), new SettingFragment()};
    private SharedPreferences mSharedPreferences;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        viewPager = (ViewPager) findViewById(R.id.pager);
        email = MySharePreferences.getValue(this, "email", "");
        getTab();
        pagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager(), tabItems);
        viewPager.setAdapter(pagerAdapter);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });
        mSlidingTabLayout.setViewPager(viewPager);
        mSharedPreferences = this.getSharedPreferences(GCMConfig.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void getTab() {
        tabItems = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            TabItem tabItem = new TabItem();
            tabItem.setTitle(titles[i]);
            tabItem.setFragment(fragments[i]);
            tabItems.add(tabItem);
        }
    }

    @Subscribe
    public void getLogout(MessageChat msg) {
        if (msg != null && msg.getAction() != null && msg.getEmail().equals(email)) {
            closeApp();
        }

    }

    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();

    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {

        }

        return super.onCreateOptionsMenu(menu);


    }

    public void closeApp() {
        Toast.makeText(getApplicationContext(), "App Closed, Account was logged by other devices ", Toast.LENGTH_LONG).show();
        MySharePreferences.setValue(this, "email", null);
        MySharePreferences.setValue(this, "token", null);
        MySharePreferences.setValue(this, "checked", false);
        mSharedPreferences.edit().putString(GCMConfig.PREFERENCE_KEY_REG_ID, null).commit();

        finish();
        startActivity(new Intent(this, SplashActivity.class));
    }
}
