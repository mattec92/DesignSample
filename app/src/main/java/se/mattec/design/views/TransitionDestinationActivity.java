package se.mattec.design.views;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;

import butterknife.BindView;
import butterknife.ButterKnife;
import se.mattec.design.R;
import se.mattec.design.interfaces.TransitionListener;

public class TransitionDestinationActivity
        extends AppCompatActivity
{

    private static final String EXTRAS_SHARED_VIEW_POSITION_X = "EXTRAS_SHARED_VIEW_POSITION_X";
    private static final String EXTRAS_SHARED_VIEW_POSITION_Y = "EXTRAS_SHARED_VIEW_POSITION_Y";
    private static final String EXTRAS_SHARED_VIEW_WIDTH = "EXTRAS_SHARED_VIEW_WIDTH";
    private static final String EXTRAS_SHARED_VIEW_HEIGHT = "EXTRAS_SHARED_VIEW_HEIGHT";
    private static final String EXTRAS_TRANSITION_LISTENER = "EXTRAS_TRANSITION_LISTENER";

    private static final int ANIMATION_DURATION = 500;
    private static final int START_DELAY = 20;

    @BindView(R.id.root)
    View mRoot;

    @BindView(R.id.shared_destination_view)
    View mSharedDestinationView;

    private int mSharedViewPosX;
    private int mSharedViewPosY;
    private int mSharedViewWidth;
    private int mSharedViewHeight;
    private TransitionListener mTransitionListener;

    public static void openWithTransition(Context context, View sharedView, TransitionListener listener)
    {
        Intent intent = new Intent(context, TransitionDestinationActivity.class);

        int[] location = new int[2];
        sharedView.getLocationOnScreen(location);
        intent.putExtra(EXTRAS_SHARED_VIEW_POSITION_X, location[0]);
        intent.putExtra(EXTRAS_SHARED_VIEW_POSITION_Y, location[1]);
        intent.putExtra(EXTRAS_SHARED_VIEW_WIDTH, sharedView.getWidth());
        intent.putExtra(EXTRAS_SHARED_VIEW_HEIGHT, sharedView.getHeight());
        intent.putExtra(EXTRAS_TRANSITION_LISTENER, listener);

        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_destination);
        ButterKnife.bind(this);

        getExtras();
        animateIn();
    }

    @Override
    public void onBackPressed()
    {
        animateOut();
    }

    private void getExtras()
    {
        Intent intent = getIntent();
        if (intent != null)
        {
            Bundle extras = intent.getExtras();
            if (extras != null)
            {
                mSharedViewPosX = extras.getInt(EXTRAS_SHARED_VIEW_POSITION_X);
                mSharedViewPosY = extras.getInt(EXTRAS_SHARED_VIEW_POSITION_Y);
                mSharedViewWidth = extras.getInt(EXTRAS_SHARED_VIEW_WIDTH);
                mSharedViewHeight = extras.getInt(EXTRAS_SHARED_VIEW_HEIGHT);
                mTransitionListener = (TransitionListener) extras.getSerializable(EXTRAS_TRANSITION_LISTENER);
            }
        }
    }

    private void animateIn()
    {
        mSharedDestinationView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {

            @Override
            public boolean onPreDraw()
            {
                mSharedDestinationView.getViewTreeObserver().removeOnPreDrawListener(this);

                mSharedDestinationView.setScaleX(getScaleDelta(mSharedViewWidth, mSharedDestinationView.getWidth()));
                mSharedDestinationView.setScaleY(getScaleDelta(mSharedViewHeight, mSharedDestinationView.getHeight()));

                int[] location = new int[2];
                mSharedDestinationView.getLocationOnScreen(location);

                mSharedDestinationView.setTranslationX(getTranslationDelta(mSharedViewPosX, location[0]));
                mSharedDestinationView.setTranslationY(getTranslationDelta(mSharedViewPosY, location[1]));

                mSharedDestinationView.setVisibility(View.VISIBLE);

                mSharedDestinationView.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .translationX(0)
                        .translationY(0)
                        .setDuration(ANIMATION_DURATION)
                        .setStartDelay(START_DELAY)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .withStartAction(new Runnable()
                        {

                            @Override
                            public void run()
                            {
                                if (mTransitionListener != null)
                                {
                                    mTransitionListener.onTransitionStarted();
                                }
                            }
                        })
                        .start();

                ObjectAnimator colorFade = ObjectAnimator.ofObject(
                        mRoot,
                        "backgroundColor",
                        new ArgbEvaluator(),
                        ContextCompat.getColor(TransitionDestinationActivity.this, android.R.color.transparent),
                        ContextCompat.getColor(TransitionDestinationActivity.this, android.R.color.black));

                colorFade.setDuration(ANIMATION_DURATION);
                colorFade.setStartDelay(START_DELAY);
                colorFade.setInterpolator(new FastOutSlowInInterpolator());
                colorFade.start();

                return true;
            }
        });
    }

    private void animateOut()
    {
        int[] location = new int[2];
        mSharedDestinationView.getLocationOnScreen(location);

        mSharedDestinationView.animate()
                .scaleX(getScaleDelta(mSharedViewWidth, mSharedDestinationView.getWidth()))
                .scaleY(getScaleDelta(mSharedViewHeight, mSharedDestinationView.getHeight()))
                .translationX(getTranslationDelta(mSharedViewPosX, location[0]))
                .translationY(getTranslationDelta(mSharedViewPosY, location[1]))
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(new FastOutSlowInInterpolator())
                .withEndAction(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        finish();
                        overridePendingTransition(0, 0);
                    }
                })
                .start();

        ObjectAnimator colorFade = ObjectAnimator.ofObject(
                mRoot,
                "backgroundColor",
                new ArgbEvaluator(),
                ContextCompat.getColor(TransitionDestinationActivity.this, android.R.color.black),
                ContextCompat.getColor(TransitionDestinationActivity.this, android.R.color.transparent));

        colorFade.setDuration(ANIMATION_DURATION);
        colorFade.setInterpolator(new FastOutSlowInInterpolator());
        colorFade.start();
    }

    private float getScaleDelta(float start, float end)
    {
        return start / end;
    }

    private int getTranslationDelta(int start, int end)
    {
        return start - end;
    }

}
