package se.mattec.design.views;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import se.mattec.design.R;
import se.mattec.design.adapters.PageAdapter;
import se.mattec.design.interfaces.PageListener;
import se.mattec.design.interfaces.ViewPagerHolder;
import se.mattec.design.interfaces.ViewPagerListener;
import se.mattec.design.utils.ColorUtils;

public class MainActivity
        extends AppCompatActivity
        implements PageListener, ViewPagerHolder
{

    @BindView(R.id.container)
    protected FrameLayout mContainer;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.viewpager)
    protected ViewPager mViewPager;

    @BindDimen(R.dimen.card_top_margin)
    protected float CARD_TOP_MARGIN;

    @BindDimen(R.dimen.viewpager_padding)
    protected float VIEWPAGER_PADDING;

    @BindDimen(R.dimen.negateive_viewpager_padding)
    protected float PAGE_MARGIN;

    @BindColor(R.color.colorPrimary)
    protected int COLOR_0;

    @BindColor(R.color.cyan)
    protected int COLOR_1;

    @BindColor(R.color.teal)
    protected int COLOR_2;

    @BindColor(R.color.deep_purple)
    protected int COLOR_3;

    @BindColor(R.color.blue_grey)
    protected int COLOR_4;

    @BindColor(R.color.white_trans_87)
    protected int COLOR_WHITE;

    @BindColor(R.color.black_trans_87)
    protected int COLOR_BLACK;

    private Map<Integer, ViewPagerListener> mViewPagerListeners;
    private int mLastPagePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mViewPagerListeners = new HashMap<>();

        setupToolbar();
        setupViewPager();
    }

    private void setupToolbar()
    {
        setSupportActionBar(mToolbar);
    }

    private void setupViewPager()
    {
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(PageAdapter.NUM_PAGES);
        mViewPager.setPageMargin((int) PAGE_MARGIN);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                mContainer.setBackgroundColor(ColorUtils.mixColors(getColorForPosition(position), getColorForPosition(position + 1), positionOffset));

                // Notify the page scrolled from.
                ViewPagerListener listener;
                if (mLastPagePosition > position)
                {
                    listener = mViewPagerListeners.get(mLastPagePosition);
                }
                else
                {
                    listener = mViewPagerListeners.get(position);
                }

                if (listener != null)
                {
                    if (mLastPagePosition > position)
                    {
                        listener.onViewPagerScrolled(positionOffset);
                    }
                    else
                    {
                        listener.onViewPagerScrolled(positionOffset);
                    }
                }
            }

            @Override
            public void onPageSelected(int position)
            {
                mLastPagePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
    }

    @Override
    public void onPageScrolled(int position, int scrollY, float ratio, boolean isScrollToTop)
    {
        //Only react to scroll events for current fragment.
        if (mViewPager.getCurrentItem() == position || isScrollToTop)
        {
            int color = ColorUtils.mixColors(COLOR_WHITE, COLOR_BLACK, ratio);
            mToolbar.setTitleTextColor(color);
            Drawable overflowIcon = mToolbar.getOverflowIcon();
            if (overflowIcon != null)
            {
                overflowIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
        }
    }

    @Override
    public void registerViewPagerListener(int position, ViewPagerListener listener)
    {
        mViewPagerListeners.put(position, listener);
    }

    @Override
    public void unregisterViewPagerListener(int position)
    {
        mViewPagerListeners.remove(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_item_mock:
                Toast.makeText(this, "Clicked menu item.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int getColorForPosition(int position)
    {
        switch (position)
        {
            case 0:
                return COLOR_0;
            case 1:
                return COLOR_1;
            case 2:
                return COLOR_2;
            case 3:
                return COLOR_3;
            case 4:
                return COLOR_4;
        }
        return COLOR_0;
    }

}
