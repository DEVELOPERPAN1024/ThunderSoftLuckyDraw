package cn.thundersoft.codingnight.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.acitivity.LuckyDrawActivityFinal;
import cn.thundersoft.codingnight.db.DbUtil;
import cn.thundersoft.codingnight.models.Award;
import cn.thundersoft.codingnight.models.Prize;

/**
 * @author GreenShadow
 */

public class PrizeFragment extends Fragment implements View.OnClickListener {
    private View contentContainer;
    private ImageView prizeImage;
    private TextView name, detail;
    private FrameLayout fabContainer;
    private ImageView fabStart;

    private Prize mPrize;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lucky_draw, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        contentContainer = view.findViewById(R.id.content_container);
        prizeImage = (ImageView) view.findViewById(R.id.lucky_draw_award_image);
        name = (TextView) view.findViewById(R.id.lucky_draw_award_name);
        detail = (TextView) view.findViewById(R.id.lucky_draw_award_detail);
        fabContainer = (FrameLayout) view.findViewById(R.id.fab_container);
        fabStart = (ImageView) view.findViewById(R.id.fab_start);

        Bundle b = getArguments();
        mPrize = b == null ? null : (Prize) b.getParcelable(Prize.PRIZE_BUNDLE_KEY);
        if (mPrize != null) {
            prizeImage.setImageURI(mPrize.getImgUri());
            name.setText(mPrize.getName());
            detail.setText(mPrize.getDetail());
        }

        fabStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        startRandomRolling();
        if (mPrize == null) {
            return;
        }
        Award award = DbUtil.getAwardById(getContext(), mPrize.getId());
        if (award == null) {
            return;
        }
        Intent intent = new Intent(getContext(), LuckyDrawActivityFinal.class);
        intent.putExtra("award", award);
        startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(getActivity(), getActivity().findViewById(R.id.lucky_draw_award_image), "sharePrize").toBundle());


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void startRandomRolling() {
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        Animator fabMove = ObjectAnimator.ofFloat(fabContainer, "translationX",
                (width - fabContainer.getWidth()) / 2 - 100);
        Animator contentMoveX = ObjectAnimator.ofFloat(contentContainer, "translationX",
                contentContainer.getWidth() * -1);
        Animator contentMoveY = ObjectAnimator.ofFloat(contentContainer, "translationY",
                contentContainer.getHeight() * -1);

        AnimatorSet animator = new AnimatorSet();
        animator.playTogether(fabMove, contentMoveX, contentMoveY);
        animator.setDuration(333);
        animator.start();
    }
}
