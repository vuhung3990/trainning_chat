package trainning.chat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import trainning.chat.entity.TabItem;

/**
 * Created by ASUS on 10/10/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<TabItem> tabItems;

    public ViewPagerAdapter(FragmentManager fm, List<TabItem> tabItems) {

        super(fm);
        this.tabItems = tabItems;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment tab = tabItems.get(position).getFragment();


        return tab;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabItems.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return tabItems.size();
    }
}
