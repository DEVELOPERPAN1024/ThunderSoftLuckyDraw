package cn.thundersoft.codingnight.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.thundersoft.codingnight.R;

/**
 * Created by pandroid on 11/25/16.
 */

public class NewMainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.main_data_cv)
    CardView mMainDataCV;
    @Bind(R.id.main_award_cv)
    CardView mMainAwardCV;
    @Bind(R.id.main_luckydraw_cv)
    CardView mMainLuckyDrawCV;
    @Bind(R.id.main_item_ll)
    LinearLayout mMainItemLL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        ButterKnife.bind(this);
        initAnimation();
        initView();
    }

    private void initView() {
        mMainDataCV.setOnClickListener(this);
        mMainAwardCV.setOnClickListener(this);
        mMainLuckyDrawCV.setOnClickListener(this);
    }

    private void initAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.enter_from_right);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        mMainItemLL.setLayoutAnimation(controller);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.main_data_cv: {
                startActivity(new Intent(this, DataActivity.class));
            }
            case R.id.main_award_cv: {

            }
            case R.id.main_luckydraw_cv: {

            }
            default:
                break;
        }
    }
}