package se.mattec.design.interfaces;

import android.view.View;

import java.lang.ref.WeakReference;

public class TransitionListenerImpl
        implements TransitionListener
{

    private static WeakReference<View> mViewRef;

    public TransitionListenerImpl(View view)
    {
        mViewRef = new WeakReference<>(view);
    }

    @Override
    public void onTransitionStarted()
    {
        View view = mViewRef.get();
        if (view != null)
        {
            view.setVisibility(View.INVISIBLE);
        }
    }

}
