package se.mattec.design.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.mattec.design.R;

public class PageFragment
        extends Fragment
{

    @BindView(R.id.card)
    protected CardView mCardView;

    @BindView(R.id.card_content)
    protected LinearLayout mCardContent;

    public static PageFragment newInstance()
    {
        Bundle args = new Bundle();
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_page, container, false);

        ButterKnife.bind(this, root);

        return root;
    }

}
