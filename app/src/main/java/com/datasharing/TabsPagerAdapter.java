package com.datasharing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    Context context;

    public TabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    public View getTabView(int position) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.header_tab, null);
        TextView img_tab = v.findViewById(R.id.img_tab);

        if (position == 0) {
            img_tab.setText("HISTORY");
            img_tab.setTextColor(context.getResources().getColor(R.color.white));
        } else if (position == 1) {
            img_tab.setText("APP");
            img_tab.setTextColor(context.getResources().getColor(R.color.tabcolor));
        } else if (position == 2) {
            img_tab.setText("PHOTO");
            img_tab.setTextColor(context.getResources().getColor(R.color.tabcolor));
        } else if (position == 3) {
            img_tab.setText("MUSIC");
            img_tab.setTextColor(context.getResources().getColor(R.color.tabcolor));
        } else if (position == 4) {
            img_tab.setText("VIDEO");
            img_tab.setTextColor(context.getResources().getColor(R.color.tabcolor));
        } else {
            img_tab.setText("FILE");
            img_tab.setTextColor(context.getResources().getColor(R.color.tabcolor));
        }

        return v;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "HISTORY"; //ChildFragment1 at position 0
            case 1:
                return "APP";
            case 2:
                return "PHOTO";
            case 3:
                return "MUSIC";
            case 4:
                return "VIDEO";
            case 5:
                return "FILE";
        }

        return null;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new SentReceiveHistoryFragment(); //ChildFragment1 at position 0
            case 1:
                return new AppsFragment();
            case 2:
                return new PhotosFragment();
            case 3:
                return new AudioFragment();
            case 4:
                return new VideosFragment();
            case 5:
                return new FilesFragment();
        }

        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 6;
    }
}
