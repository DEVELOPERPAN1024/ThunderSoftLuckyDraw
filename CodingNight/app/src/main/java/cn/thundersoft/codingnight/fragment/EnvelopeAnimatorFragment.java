package cn.thundersoft.codingnight.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import cn.thundersoft.codingnight.R;

/**
 * @author GreenShadow
 */

public class EnvelopeAnimatorFragment extends Fragment {
    private View envelop;
    private View lid;
    private View lidTop, lidBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_envelope_animation, null, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        envelop = view.findViewById(R.id.envelope);
        lid = view.findViewById(R.id.lid);
        lidTop = lid.findViewById(R.id.lid_top);
        lidBack = lid.findViewById(R.id.lid_back);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animator animator = getAnimator();
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setOnClickListener(null);
                    }
                });
                animator.start();
            }
        });
    }

    private Animator getAnimator() {
        Animator flipOut = ObjectAnimator.ofFloat(lid, "rotationX", 0f, 90f);
        flipOut.setDuration(500);
        Animator scaleOut = ObjectAnimator.ofFloat(lid, "scaleY", 1f, 0.5f);
        scaleOut.setDuration(500);
        AnimatorSet setOut = new AnimatorSet();
        setOut.playTogether(flipOut, scaleOut);
        setOut.setInterpolator(new AccelerateInterpolator());
        setOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                lidTop.setVisibility(View.VISIBLE);
                lidBack.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lidTop.setVisibility(View.INVISIBLE);
                lidBack.setVisibility(View.VISIBLE);
            }
        });

        Animator flipIn = ObjectAnimator.ofFloat(lid, "rotationX", 90f, 180f);
        flipIn.setDuration(500);
        Animator scaleIn = ObjectAnimator.ofFloat(lid, "scaleY", 0.5f, 1f);
        scaleIn.setDuration(500);
        AnimatorSet setIn = new AnimatorSet();
        setIn.playTogether(flipIn, scaleIn);
        setIn.setInterpolator(new DecelerateInterpolator());

        AnimatorSet flip = new AnimatorSet();
        flip.playSequentially(setOut, setIn);

        Animator move = ObjectAnimator.ofFloat(envelop, "translationY", 0f, 400f);
        move.setDuration(700);
        move.setStartDelay(300);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(flip, move);
        return animatorSet;
    }
}
