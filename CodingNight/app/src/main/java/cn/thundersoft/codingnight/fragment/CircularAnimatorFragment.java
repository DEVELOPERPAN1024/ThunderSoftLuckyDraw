package cn.thundersoft.codingnight.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import cn.thundersoft.codingnight.R;

public class CircularAnimatorFragment extends Fragment {
    private static String TAG = "CircularAnimatorFragment";

    private CircularAnimatorListener mListener = null;
    private Point mTouchPoint;

    private boolean isAnimatorRunning = false;

    public CircularAnimatorFragment() {
    }

    public CircularAnimatorFragment(Point touchPoint, CircularAnimatorListener listener) {
        mTouchPoint = touchPoint;
        mListener = listener;
    }

    public static void startAnimator(FragmentManager fm, Point touchPoint,
                                     CircularAnimatorListener listener) {
        Fragment fragment = fm.findFragmentByTag(TAG);
        if (fragment == null) {
            fm.beginTransaction()
                    .add(R.id.main, new CircularAnimatorFragment(touchPoint, listener), TAG)
                    .commit();
        }
    }

    public static void endAnimator(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(TAG);
        if (fragment != null) {
            fm.beginTransaction().remove(fragment).commit();
            Log.d(TAG, "remove");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_circle_animation, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isAnimatorRunning) {
            startAnimator();
        }
        isAnimatorRunning = true;
    }

    public void startAnimator() {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        final Animator animator = getRevealAnimator(mTouchPoint);
        if (animator != null) {
            animator.start();
        }
    }

    private Animator getRevealAnimator(Point touchPoint) {
        final Activity activity = getActivity();
        if (activity == null) {
            return null;
        }
        final View view = getView().findViewById(R.id.animator_view);
        final Display display = activity.getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        int startX;
        int startY;
        if (touchPoint != null) {
            startX = touchPoint.x;
            startY = touchPoint.y;
        } else {
            startX = size.x / 2;
            startY = size.y / 2;
        }

        final Animator circularAnimator = ViewAnimationUtils.createCircularReveal(view, startX, startY,
                0, (float) Math.sqrt(size.x * size.x + size.y * size.y) + 1);
        circularAnimator.setDuration(800);
        circularAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onCircularFinish();
                }
            }
        });

        final Animator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        alphaAnimator.setStartDelay(100);
        alphaAnimator.setDuration(400);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(circularAnimator, alphaAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onAnimatorFinish();
                }
            }
        });
        return animatorSet;
    }

    public interface CircularAnimatorListener {
        void onCircularFinish();

        void onAnimatorFinish();
    }
}
