package cn.thundersoft.codingnight.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.models.Prize;

/**
 * @author GreenShadow
 */

public class EnvelopeAnimatorFragment extends Fragment {
    private RelativeLayout envelop;
    private View lidBackStatic;
    private View lid;
    private View lidTop, lidBack;
    private ImageView imagePreview;

    private Prize mPrize;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_envelope_animation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setSharedElementEnterTransition(null);
        setSharedElementReturnTransition(null);
        setEnterTransition(null);
        setExitTransition(new Fade().setInterpolator(new DecelerateInterpolator()));

        envelop = (RelativeLayout) view.findViewById(R.id.envelope);
        lidBackStatic = view.findViewById(R.id.lid_back_static);
        lid = view.findViewById(R.id.lid);
        lidTop = lid.findViewById(R.id.lid_top);
        lidBack = lid.findViewById(R.id.lid_back);
        imagePreview = (ImageView) view.findViewById(R.id.image_preview);
        ViewCompat.setTransitionName(imagePreview, "imagePreview");
        envelop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                envelop.setOnClickListener(null);
                Animator animator = getAnimator();
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        changeFragment();
                    }
                });
                animator.start();
            }
        });
    }

    private void changeFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment prizeFragment = fm.findFragmentByTag("prize");
        if (prizeFragment == null) {
            prizeFragment = new PrizeFragment();
            Bundle b = new Bundle();
            b.putParcelable(Prize.PRIZE_BUNDLE_KEY, mPrize);
            prizeFragment.setArguments(b);
        }
        prizeFragment.setSharedElementEnterTransition(new DetailTransition()
                .setInterpolator(new AccelerateInterpolator()));
        prizeFragment.setSharedElementReturnTransition(null);
        prizeFragment.setEnterTransition(new Fade().setInterpolator(new AccelerateInterpolator()));
        prizeFragment.setExitTransition(null);
        fm.beginTransaction()
                .replace(R.id.content, prizeFragment, "prize")
                .addSharedElement(imagePreview, getString(R.string.img_transition_name))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        mPrize = args.getParcelable(Prize.PRIZE_BUNDLE_KEY);
    }

    @Override
    public void onResume() {
        if (mPrize == null) {
            imagePreview.setVisibility(View.GONE);
        } else {
            imagePreview.setVisibility(View.VISIBLE);
            imagePreview.setImageURI(mPrize.getImgUri());
        }
        super.onResume();
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

        Animator envMove = ObjectAnimator.ofFloat(envelop, "translationY", 0f, 400f);
        envMove.setDuration(700);
        envMove.setStartDelay(300);

        AnimatorSet openEnvelope = new AnimatorSet();
        openEnvelope.playTogether(flip, envMove);
        openEnvelope.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                lid.setVisibility(View.GONE);
                lidBackStatic.setVisibility(View.VISIBLE);
            }
        });

        Animator picMove = ObjectAnimator.ofFloat(imagePreview, "translationY", 0f, -440);
        picMove.setDuration(500);
        picMove.setInterpolator(new DecelerateInterpolator());

        AnimatorSet animator = new AnimatorSet();
        animator.playSequentially(openEnvelope, picMove);

        return animator;
    }
}
