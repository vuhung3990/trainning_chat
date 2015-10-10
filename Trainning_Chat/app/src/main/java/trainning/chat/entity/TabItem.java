package trainning.chat.entity;

import android.support.v4.app.Fragment;

/**
 * Created by ASUS on 10/10/2015.
 */
public class TabItem {
    Fragment fragment;
    String title;

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
