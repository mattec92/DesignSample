package se.mattec.design.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class CustomScrollView
        extends ScrollView
{

    private OnScrollChangeListener mOnScrollChangeListener;

    public CustomScrollView(Context context)
    {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        if (mOnScrollChangeListener != null)
        {
            mOnScrollChangeListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener)
    {
        mOnScrollChangeListener = onScrollChangeListener;
    }

    public interface OnScrollChangeListener
    {

        void onScrollChanged(int x, int y, int oldX, int oldY);

    }

}
