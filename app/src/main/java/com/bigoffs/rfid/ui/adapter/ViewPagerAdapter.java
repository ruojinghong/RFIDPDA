package com.bigoffs.rfid.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bigoffs.rfid.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/22 16:05
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> fragments = new ArrayList<BaseFragment>();


    public ViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;

    }

    @Override
    public Fragment getItem(int position) {
        if (fragments.size() > 0) {
            return fragments.get(position);
        }
        throw new IllegalStateException("No fragment at position " + position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
