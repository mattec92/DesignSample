package se.mattec.design.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.mattec.design.R;
import se.mattec.design.interfaces.PageListener;

public class PageFragment
        extends Fragment
{

    private static final String EXTRAS_POSITION = "EXTRAS_POSITION";

    @BindView(R.id.card)
    protected CardView mCardView;

    @BindView(R.id.scrollview)
    protected CustomScrollView mScrollView;

    private int mPosition;

    private PageListener mListener;

    public static PageFragment newInstance(int position, PageListener listener)
    {
        Bundle args = new Bundle();
        args.putInt(EXTRAS_POSITION, position);

        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        fragment.setPageListener(listener);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_page, container, false);

        ButterKnife.bind(this, root);

        getExtras();
        setupScrollView();

        return root;
    }

    private void getExtras()
    {
        Bundle args = getArguments();
        if (args != null)
        {
            mPosition = args.getInt(EXTRAS_POSITION);
        }
    }

    private void setupScrollView()
    {
        mScrollView.setOnScrollChangeListener(new CustomScrollView.OnScrollChangeListener()
        {

            @Override
            public void onScrollChanged(int x, int y, int oldX, int oldY)
            {
                if (mListener != null)
                {
                    mListener.onPageScrolled(mPosition, y);
                }
            }
        });
    }

    private void setPageListener(PageListener listener)
    {
        mListener = listener;
    }

}
