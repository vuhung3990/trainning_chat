package trainning.chat.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import trainning.chat.R;
import trainning.chat.adapter.ViewPagerAdapter;
import trainning.chat.customview.SlidingTabLayout;
import trainning.chat.entity.TabItem;
import trainning.chat.fragment.HistoryFragment;
import trainning.chat.fragment.ListUserFragment;
import trainning.chat.fragment.SettingFragment;

/**
 * Created by ASUS on 10/10/2015.
 */
public class HomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;
    private List<TabItem> tabItems;
    private static SlidingTabLayout mSlidingTabLayout;
    private String[] titles = {"Message", "Contact", "Setting"};
    private Fragment[] fragments = {new HistoryFragment(), new ListUserFragment(), new SettingFragment()};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        viewPager = (ViewPager) findViewById(R.id.pager);
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

}
