package se.mattec.design.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import se.mattec.design.R;
import se.mattec.design.adapters.PageAdapter;
import se.mattec.design.adapters.RecyclerAdapter;
import se.mattec.design.interfaces.PageListener;
import se.mattec.design.interfaces.ViewPagerHolder;
import se.mattec.design.interfaces.ViewPagerListener;

public class RecyclerPageFragment
        extends Fragment
        implements ViewPagerListener
{

    private static final String EXTRAS_POSITION = "EXTRAS_POSITION";
    private static final String EXTRAS_SHORT_ITEMS = "EXTRAS_SHORT_ITEMS";

    @BindView(R.id.page_container)
    protected FrameLayout mPageContainer;

    @BindView(R.id.card)
    protected CardView mCardView;

    @BindView(R.id.recyclerview)
    protected RecyclerView mRecyclerView;

    @BindDimen(R.dimen.card_top_margin)
    protected float CARD_TOP_MARGIN;

    @BindDimen(R.dimen.card_margin)
    protected float CARD_MARGIN;

    @BindDimen(R.dimen.card_corner_radius)
    protected float CARD_CORNER_RADIUS;

    @BindDimen(R.dimen.viewpager_padding)
    protected float VIEWPAGER_PADDING;

    private int mPosition;
    private boolean mShortItems;
    private PageListener mListener;
    private ViewPagerHolder mHolder;

    private boolean mIsScrollingToTop;

    public static RecyclerPageFragment newInstance(int position, boolean shortItems)
    {
        Bundle args = new Bundle();
        args.putInt(EXTRAS_POSITION, position);
        args.putBoolean(EXTRAS_SHORT_ITEMS, shortItems);

        RecyclerPageFragment fragment = new RecyclerPageFragment();
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
        View root = inflater.inflate(R.layout.fragment_recycler_page, container, false);

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
            mShortItems = args.getBoolean(EXTRAS_SHORT_ITEMS);
        }
    }

    private void setupView()
    {
        RecyclerAdapter adapter = new RecyclerAdapter(mShortItems);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);

        if (mPosition == 0)
        {
            mPageContainer.setPadding(mPageContainer.getPaddingLeft() / 2, mPageContainer.getPaddingTop(), mPageContainer.getPaddingRight(), mPageContainer.getPaddingBottom());
        }
        else if (mPosition == PageAdapter.NUM_PAGES - 1)
        {
            mPageContainer.setPadding(mPageContainer.getPaddingLeft(), mPageContainer.getPaddingTop(), mPageContainer.getPaddingRight() / 2, mPageContainer.getPaddingBottom());
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {

            private int prevY = -1;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                int y;

                int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisiblePosition == 0)
                {
                    View firstItemView = layoutManager.findViewByPosition(firstVisiblePosition);
                    int firstHeight = firstItemView.getHeight();

                    y = -firstItemView.getTop();

                    if (firstHeight < CARD_TOP_MARGIN)
                    {
                        y = Math.round(y * CARD_TOP_MARGIN / (float) firstHeight);
                    }
                }
                else
                {
                    y = (int) CARD_TOP_MARGIN;
                }

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
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        View firstItemView = layoutManager.findViewByPosition(firstVisiblePosition);
        int top = firstItemView != null ? -firstItemView.getTop() : 0;
        if (!mIsScrollingToTop && (firstVisiblePosition > 0 || top > 0))
        {
            mIsScrollingToTop = true;
            mRecyclerView.smoothScrollToPosition(0);
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