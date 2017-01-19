package cn.thundersoft.codingnight.acitivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.util.DisplayUtil;

public class NewMainActivity extends AbsStoragePermissionCheckActivity
        implements View.OnClickListener {

    @Bind(R.id.main_data_cv)
    CardView mMainDataCV;
    @Bind(R.id.main_award_cv)
    CardView mMainAwardCV;
    @Bind(R.id.main_luckydraw_cv)
    CardView mMainLuckyDrawCV;
    @Bind(R.id.main_item_ll)
    LinearLayout mMainItemLL;
    @Bind(R.id.main_title_tv)
    TextView mMainTitleTV;
    @Bind(R.id.small_chicken_imgv)
    ImageView mSmallChickenImgv;
    @Bind(R.id.main_bg)
    FrameLayout mMainBg;


    private boolean isChickenShouldAnimated = false;
    private static int CHICKEN_HEIGHT = 80;
    private Handler mMyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (isChickenShouldAnimated) {
                    animatedChicken();
                }
            }
            if (msg.what == 2) {
                mSmallChickenImgv.setVisibility(View.INVISIBLE);
            }

            if (msg.what == 3) {
                mSmallChickenImgv.setVisibility(View.VISIBLE);
            }
        }
    };
    Timer mTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        ButterKnife.bind(this);
        DisplayUtil.init(this);
//        initAnimation();
        initBg();
        initView();
        new Thread(new Runnable() {


            @Override
            public void run() {
                try {
                    File file = new File("/sdcard/random2.txt");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    OutputStream os = new FileOutputStream(file);
                    for (int i = 0; i < 10000; i++) {
                        double ran = Math.random();
                        String ranstr = ran + "\n";
                        os.write(ranstr.getBytes());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void initView() {
        mMainDataCV.setOnClickListener(this);
        mMainAwardCV.setOnClickListener(this);
        mMainLuckyDrawCV.setOnClickListener(this);
    }

    private void initBg() {
        Glide.with(this).load(R.drawable.main_bg1).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                mMainBg.setBackground(resource);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyHandler.sendEmptyMessageDelayed(3, 2000);
        isChickenShouldAnimated = true;
        setupAnimatedTimer();
    }


    private void setupAnimatedTimer() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mMyHandler.sendEmptyMessage(1);
            }
        }, 2000, 2000);
    }

    private void animatedChicken() {
        int randomHeight = (int) (Math.random() * (int) DisplayUtil.dp2px(CHICKEN_HEIGHT));
        ObjectAnimator animatorHeight = ObjectAnimator.ofFloat(mSmallChickenImgv, "translationY", -randomHeight);
        animatorHeight.setDuration(2000);
        animatorHeight.setInterpolator(new OvershootInterpolator());
        animatorHeight.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                int randomWidth = (int) (Math.random() * DisplayUtil.screenWidth);
                if (randomWidth < DisplayUtil.dp2px(CHICKEN_HEIGHT)) {
                    randomWidth += DisplayUtil.dp2px(CHICKEN_HEIGHT);
                }
                ObjectAnimator animatorWidth = ObjectAnimator.ofFloat(mSmallChickenImgv, "translationX", randomWidth);
                animatorWidth.setDuration(2000);
                animatorWidth.setStartDelay(500);
                animatorWidth.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorWidth.start();
            }
        });
        animatorHeight.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMyHandler.sendEmptyMessage(2);
        isChickenShouldAnimated = false;
        mTimer.cancel();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.main_data_cv: {
                startActivity(new Intent(this, DataActivity.class));
                overridePendingTransition(R.anim.enter_from_left, R.anim.out_same);
                break;
            }
            case R.id.main_award_cv: {
                Intent intent = new Intent(this, AwardActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.out_same);
                break;
            }
            case R.id.main_luckydraw_cv: {
                checkAndRequestStoragePermission();
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onStoragePermissionGranted() {
        mMyHandler.sendEmptyMessage(2);
        Intent intent = new Intent(this, LuckyDrawActivityNew.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, mMainTitleTV, "shareTitle").toBundle());
    }
}
