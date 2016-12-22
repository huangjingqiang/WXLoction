package com.youqu.piclbs.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by hjq on 16-12-22.
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {
    private List<String> items;
    private List<Fragment> fragments ;

    public MainFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> items) {
        super(fm);
        this.fragments = fragments;
        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return items.get(position);
    }
}
