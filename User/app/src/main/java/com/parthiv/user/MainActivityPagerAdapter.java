package com.parthiv.user;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.parthiv.user.cart.CartFragment;
import com.parthiv.user.item.ItemFragment;
import com.parthiv.user.order.OrderListFragment;

public class MainActivityPagerAdapter extends FragmentPagerAdapter {

    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ItemFragment.newInstance();
            case 1:
                return CartFragment.newInstance();
            case 2:
                return OrderListFragment.newInstance();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

}
