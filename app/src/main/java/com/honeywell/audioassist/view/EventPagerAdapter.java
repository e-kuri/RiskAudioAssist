package com.honeywell.audioassist.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.honeywell.audioassist.R;

public class EventPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;
    private Context mContext;

    public EventPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
        fragments = new Fragment[]{new EventHistoryFragment(), new EventStatisticsFragment()};
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return mContext.getString(R.string.event_list_title);
            default:
                return mContext.getString(R.string.event_statistics_title);
        }
    }
}
