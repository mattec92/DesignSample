package se.mattec.design.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import se.mattec.design.interfaces.PageListener;
import se.mattec.design.views.PageFragment;

public class PageAdapter
        extends FragmentPagerAdapter
{

    public static final int NUM_PAGES = 5;

    private PageListener mListener;

    public PageAdapter(FragmentManager fm, PageListener listener)
    {
        super(fm);
        mListener = listener;
    }

    @Override
    public Fragment getItem(int position)
    {
        return PageFragment.newInstance(position, mListener);
    }

    @Override
    public int getCount()
    {
        return NUM_PAGES;
    }

}
