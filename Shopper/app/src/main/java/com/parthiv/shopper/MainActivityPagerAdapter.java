package com.parthiv.shopper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.parthiv.shopper.item.ItemFragment;

public class MainActivityPagerAdapter extends FragmentPagerAdapter {

    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ItemFragment.newInstance();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

}
