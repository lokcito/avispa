package info.rayrojas.avispa.activities.ui.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import info.rayrojas.avispa.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    public static Fragment[] currentFragments = new Fragment[3];
//    @StringRes
//    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);

        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        currentFragments[position] = PlaceholderFragment.newInstance(position + 1);
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return currentFragments[position];
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = mContext.getResources().getString(TAB_TITLES[position]);
        return title;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}