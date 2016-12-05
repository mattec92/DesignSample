package se.mattec.design.views;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import se.mattec.design.R;
import se.mattec.design.adapters.PageAdapter;

public class MainActivity
        extends AppCompatActivity
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupViewPager();
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

            }

            @Override
            public void onPageSelected(int position)
            {

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
    }

}
