package se.mattec.design.views;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.mattec.design.R;
import se.mattec.design.adapters.PageAdapter;
import se.mattec.design.interfaces.PageListener;

public class MainActivity
        extends AppCompatActivity
{

    @BindView(R.id.container)
    protected FrameLayout mContainer;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.viewpager)
    protected ViewPager mViewPager;

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
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), new PageListener()
        {

            @Override
            public void onPageScrolled(int position, int scrollY)
            {
                Log.d(MainActivity.class.getSimpleName(), String.format("onPageScrolled, position: %d, scrollY: %d", position, scrollY));
            }
        });
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(PageAdapter.NUM_PAGES);
    }

}
