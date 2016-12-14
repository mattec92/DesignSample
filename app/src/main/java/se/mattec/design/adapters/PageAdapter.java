package se.mattec.design.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import se.mattec.design.views.PageFragment;
import se.mattec.design.views.RecyclerPageFragment;

public class PageAdapter
        extends FragmentPagerAdapter
{

    public static final int NUM_PAGES = 5;

    public PageAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return RecyclerPageFragment.newInstance(position, false);
            case 1:
                return RecyclerPageFragment.newInstance(position, true);
            default:
                return PageFragment.newInstance(position);
        }
    }

    @Override
    public int getCount()
    {
        return NUM_PAGES;
    }

}
