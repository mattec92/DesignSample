package se.mattec.design.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import se.mattec.design.R;
import se.mattec.design.adapters.PageAdapter;
import se.mattec.design.interfaces.PageListener;
import se.mattec.design.interfaces.ViewPagerHolder;
import se.mattec.design.interfaces.ViewPagerListener;

public class PageFragment
        extends Fragment
        implements ViewPagerListener
{

    private static final String EXTRAS_POSITION = "EXTRAS_POSITION";

    @BindView(R.id.page_container)
    protected FrameLayout mPageContainer;

    @BindView(R.id.card)
    protected CardView mCardView;

    @BindView(R.id.scrollview)
    protected CustomScrollView mScrollView;

    @BindDimen(R.dimen.card_top_margin)
    protected float CARD_TOP_MARGIN;

    @BindDimen(R.dimen.card_margin)
    protected float CARD_MARGIN;

    @BindDimen(R.dimen.card_corner_radius)
    protected float CARD_CORNER_RADIUS;

    @BindDimen(R.dimen.viewpager_padding)
    protected float VIEWPAGER_PADDING;

    private int mPosition;
    private PageListener mListener;
    private ViewPagerHolder mHolder;

    private boolean mIsScrollingToTop;

    public static PageFragment newInstance(int position)
    {
        Bundle args = new Bundle();
        args.putInt(EXTRAS_POSITION, position);

        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mListener = (PageListener) context;
        mHolder = (ViewPagerHolder) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_page, container, false);

        ButterKnife.bind(this, root);

        getExtras();
        setupView();

        if (mHolder != null)
        {
            mHolder.registerViewPagerListener(mPosition, this);
        }

        return root;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (mHolder != null)
        {
            mHolder.unregisterViewPagerListener(mPosition);
        }
    }

    private void getExtras()
    {
        Bundle args = getArguments();
        if (args != null)
        {
            mPosition = args.getInt(EXTRAS_POSITION);
        }
    }

    private void setupView()
    {
        if (mPosition == 0)
        {
            mPageContainer.setPadding(mPageContainer.getPaddingLeft() / 2, mPageContainer.getPaddingTop(), mPageContainer.getPaddingRight(), mPageContainer.getPaddingBottom());
        }
        else if (mPosition == PageAdapter.NUM_PAGES - 1)
        {
            mPageContainer.setPadding(mPageContainer.getPaddingLeft(), mPageContainer.getPaddingTop(), mPageContainer.getPaddingRight() / 2, mPageContainer.getPaddingBottom());
        }

        mScrollView.setOnScrollChangeListener(new CustomScrollView.OnScrollChangeListener()
        {

            private int prevY = -1;

            @Override
            public void onScrollChanged(int x, int y, int oldX, int oldY)
            {
                if (y == 0)
                {
                    prevY = 0;
                }

                float ratio = Math.min(1f, Math.max(0f, (float) (prevY >= 0 ? ((y + prevY) / 2) : y) / CARD_TOP_MARGIN));
                float invertedRatio = (1f - ratio);

                if (mListener != null)
                {
                    mListener.onPageScrolled(mPosition, y, ratio, mIsScrollingToTop);
                }

                if (ratio == 1f)
                {
                    mCardView.setRadius(0f);
                }
                else
                {
                    mCardView.setRadius(CARD_CORNER_RADIUS);
                }

                int containerPadding = (int) (invertedRatio * VIEWPAGER_PADDING);

                mPageContainer.setPadding((int) (containerPadding / (mPosition == 0 ? 2f : 1f)), mPageContainer.getPaddingTop(),
                        (int) (containerPadding / (mPosition == PageAdapter.NUM_PAGES - 1 ? 2f : 1f)), mPageContainer.getPaddingBottom());

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mCardView.getLayoutParams();
                if (params != null)
                {
                    int cardMargin = (int) Math.floor(invertedRatio * CARD_MARGIN);
                    int topMargin = (int) Math.floor(invertedRatio * CARD_TOP_MARGIN);
                    params.leftMargin = cardMargin;
                    params.rightMargin = cardMargin;
                    params.topMargin = topMargin;
                }
                mCardView.requestLayout();
                mPageContainer.bringToFront();

                prevY = y;
            }
        });
    }

    @Override
    public void onViewPagerScrolled(float ratio)
    {
        if (!mIsScrollingToTop && mScrollView.getScrollY() > 0)
        {
            mIsScrollingToTop = true;
            mScrollView.smoothScrollTo(mScrollView.getScrollX(), 0);
            new Handler().postDelayed(new Runnable()
            {

                @Override
                public void run()
                {
                    mIsScrollingToTop = false;
                }
            }, 300);
        }
    }

}
