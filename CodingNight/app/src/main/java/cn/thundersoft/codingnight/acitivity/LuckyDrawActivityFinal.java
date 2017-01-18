package cn.thundersoft.codingnight.acitivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.db.DbUtil;
import cn.thundersoft.codingnight.models.Award;
import cn.thundersoft.codingnight.models.Person;
import cn.thundersoft.codingnight.util.MyRandom;
import cn.thundersoft.codingnight.util.StringUtil;

public class LuckyDrawActivityFinal extends AppCompatActivity {

    public static final int REQ_CODE = 2017;
    private static final int STOP_DRAW = 1;
    private static final int START_DRAW = 0;

    private static final int PLACE_NAME_IN_SEQUENCE = 2;
    private static final int PLACE_NAME_DONE = 3;

    private static final int RANDOM_FAKE = 1;
    private static final int RANDOM_REAL = 0;


    private Resources mRes;
    // view
    private FrameLayout mDrawBtnLayout;
    private ImageView mDrawButton;
    private ScrollView mRandomScrollView;
    private TextView mHintTextView;
    private TextView mRandomTextView;
    // data
    private Award mCurrentAward;
    private List<String> mAwardNameList = new ArrayList<>();
    private List<Person> mRandomNameList = new ArrayList<>();

    private List<Person> mTotalPersons = new ArrayList<>();
    private List<Person> mPersonsToShow = new ArrayList<>();


    // state
    private boolean mIsDrawing;
    private int mBackPressTime = 0;

    private Timer mTimer;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_draw_final);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //屏幕常亮
        init();
        if (!isDrawEnd()) {
            getNameList(); // 打开即滚动
        } else {
            mRandomTextView.setText("已经抽完了\n去首页奖项中查看中将名单及完成更多其它操作");
            updateRandomTextStyle(18f, 0, 0);
        }
    }

    @Override
    public void onBackPressed() { // need test
        if (isDrawEnd()) {
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.out_to_right);
            return;
        }
        Toast toast = Toast.makeText(this, getText(R.string.lucky_draw_final_back_hint), Toast.LENGTH_SHORT);
        toast.show();
    }


    private void init() {
        initData();
        initView();
        restoreData();
        initAction();
    }

    private void initData() {
        int id = getIntent().getIntExtra("award_id", -1);
        if (id != -1) {
            mCurrentAward = DbUtil.getAwardById(this, id);
        } else {
            Toast.makeText(this, "数据错误", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (mCurrentAward == null) {
            Toast.makeText(this, "数据错误", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (mCurrentAward.isRepeatable()) {
            mTotalPersons = DbUtil.getAllPerson(this);
        } else {
            mTotalPersons = DbUtil.getUnawardPersons(this);
        }

        mIsDrawing = true; // 打开该Activity即开始滚动

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case START_DRAW:
                        updateRandomList();
                        break;
                    case STOP_DRAW:
                        updateRandomList();
//                    if (!mIsDrawing) {
                        for (int i = 0; i < mPersonsToShow.size(); i++) {
                            //一个人获取多个奖项?
                            //updatePersonAwardState(mPersonsToShow.get(i));
                            DbUtil.insertWinner(LuckyDrawActivityFinal.this,
                                    mPersonsToShow.get(i).getId(),
                                    mCurrentAward.getId());
                            DbUtil.updateAward(LuckyDrawActivityFinal.this, mCurrentAward);
                        }
//                    updateAwardNameList();
//                    }
                        break;
                    case PLACE_NAME_IN_SEQUENCE:
                        updateRandomList();
                        insertWinner(mPersonsToShow.get(mPersonsToShow.size() - 1).getId());
                        break;
                    case PLACE_NAME_DONE:
                        mDrawBtnLayout.setEnabled(true);
                    default:
                        break;
                }
            }
        };

        mRes = getResources();
    }

    private void animateStopDrawing() {
        mDrawBtnLayout.setEnabled(false);
        Animator alpha = ObjectAnimator.ofFloat(mRandomTextView, "alpha", 1, 0);
        alpha.setDuration(500);
        alpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRandomTextView.setText("");
                mRandomTextView.setAlpha(1);
                mIsDrawing = false;
                updateButtonState();
                placeNameInSequence();
            }
        });
        alpha.start();
    }

    private void placeNameInSequence() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPersonsToShow.clear();
                int count = getDrawCountForThisTime(RANDOM_REAL);
                for (int i = 0; i < count; i++) {
                    mPersonsToShow.add(MyRandom.getRandomPersion(mTotalPersons));
                    mHandler.sendEmptyMessage(PLACE_NAME_IN_SEQUENCE);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.sendEmptyMessage(PLACE_NAME_DONE);
            }
        }).start();
    }

    private void insertWinner(final int winnerId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DbUtil.insertWinner(LuckyDrawActivityFinal.this,
                        winnerId,
                        mCurrentAward.getId());
                DbUtil.updateAward(LuckyDrawActivityFinal.this, mCurrentAward);
            }
        }).start();
    }

    private void initView() {
        mDrawButton = (ImageView) findViewById(R.id.lucky_draw_final_button);
        mRandomTextView = (TextView) findViewById(R.id.lucky_draw_final_text_random);
        mHintTextView = (TextView) findViewById(R.id.lucky_draw_final_text_hint);
        mDrawBtnLayout = (FrameLayout) findViewById(R.id.fab_container);
        updateHintText();
        updateButtonState();

    }


    private void initAction() {

        mDrawBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 列表滚动状态切换
                // 按钮状态切换
                // hint信息切换
                // 获奖列表更新
                if (isDrawEnd()) {
                    updateButtonState();
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.out_to_right);
//                    Toast.makeText(LuckyDrawActivityFinal.this, "已经抽完了", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mIsDrawing) { // stop
                    animateStopDrawing();
                    mCurrentAward.increaseDrewTimes();
                    updateHintText();
//                    DbUtil.updateAward(LuckyDrawActivityFinal.this, mCurrentAward); 放到handler更新
                    /*
                    mIsDrawing = false; // stop while in thread
                    */
                } else { // start
                    mIsDrawing = true;
                    getNameList();
                    updateButtonState();
                }

            }
        });
    }


    // 抽奖
    private void getNameList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mIsDrawing) {
                    if (mTotalPersons == null) {
                        break;
                    }
                    if (mTotalPersons.size() < 1) {
                        break;
                    }

                    mPersonsToShow = MyRandom.getRandomListFake(mTotalPersons, getDrawCountForThisTime(RANDOM_FAKE));
                    mHandler.sendEmptyMessage(START_DRAW); //按钮停止再发
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                /*
                //滚动时点击抽奖，真正的随机抽奖
                if (!mIsDrawing) {
                    Log.d("DBW", "real draw count " + getDrawCountForThisTime(RANDOM_REAL));
                    mPersonsToShow = MyRandom.getRandomList(mTotalPersons, getDrawCountForThisTime(RANDOM_REAL));
                    mHandler.sendEmptyMessage(STOP_DRAW);
                }
                */
            }
        }).start();
    }


    private int getDrawCountForThisTime(int randomStyle) {
        int total = mCurrentAward.getCount();
        int drewTimes = mCurrentAward.getDrewTimes() + randomStyle;
        if (drewTimes == mCurrentAward.getTotalDrawTimes()) {
            return total / mCurrentAward.getTotalDrawTimes() + total % mCurrentAward.getTotalDrawTimes();
        } else {
            return total / mCurrentAward.getTotalDrawTimes();
        }
    }

    private void updateDrawState() { // 开始抽奖
        if (!isDrawEnd()) {

        } else {
            // 抽奖结束显示什么？
        }
    }

    private void updatePersonAwardState(Person p) {
        for (Person person : mTotalPersons) {
            if (person.getId() == p.getId()) {
                person.setPrize(mCurrentAward.getId());
            }
        }
    }

    private void updateButtonState() {
        //不同样式控制？
        if (isDrawEnd()) {
            Log.d("DBW", "disable button");
            mDrawButton.setImageResource(R.drawable.ic_arrow_left_white_24dp);
        } else {
            mDrawButton.setImageResource(mIsDrawing ?
                    R.drawable.ic_pause_white_24dp :
                    R.drawable.begin_draw_icon);
        }
    }

    private void updateHintText() {
        String strToShow = mRes.getString(R.string.lucky_draw_final_draw_hint);
        mHintTextView.setText(String.format(strToShow, mCurrentAward.getDrewTimes(), mCurrentAward.getTotalDrawTimes()));
    }

    private void updateRandomList() {
        String str = "";
        if (mPersonsToShow.size() < 1) {
            return;
        }
        if (getDrawCountForThisTime(RANDOM_REAL) > 10) {
            for (int i = 0; i < mPersonsToShow.size(); ++i) {
                if (i + 1 < mPersonsToShow.size()) {
                    str += (controlStringLength(mPersonsToShow.get(i).getInfo())
                            + "  "
                            + controlStringLength(mPersonsToShow.get(++i).getInfo())
                            + "\n");
                } else {
                    str += (controlStringLength(mPersonsToShow.get(i).getInfo()))
                            + "  "
                            + getNumbersOfSpace(20);
                }
            }
        } else {
            for (int i = 0; i < mPersonsToShow.size(); ++i) {
                str += (mPersonsToShow.get(i).getInfo() + "\n");
            }
        }
        mRandomTextView.setText(str);
        calculateRandomTextShowStyleByLines(mPersonsToShow.size());
    }

    private void calculateRandomTextShowStyleByLines(int lines) {
        if (lines > 10) {
            updateRandomTextStyle(14f, 0, 0);
            return;
        }
        if (lines == 1) {
            updateRandomTextStyle(24f, 0, 0);
            return;
        }
        if (lines > 1 && lines < 3) {
            updateRandomTextStyle(20f, 0, 0);
            return;
        }
        if (lines < 10 && lines > 3) {
            updateRandomTextStyle(16f, 0, 0);
            return;
        }
    }

    private void updateRandomTextStyle(float textSize, int textColor, float lineMul) {
        mRandomTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                textSize == 0 ? 12f : textSize);
        mRandomTextView.setTextColor(textColor == 0 ? mRes.getColor(R.color.colorSubTitle) : textColor);
        mRandomTextView.setLineSpacing(0, lineMul == 0 ? 1.5f : lineMul);
    }

    private String getNumbersOfSpace(int num) {
        String str = "";
        for (int i = 0; i < num; i++) {
            str += " ";
        }
        return str;
    }

    private String controlStringLength(String str) {
        int strRealLength = StringUtil.getStringRealLength(str);
        if (strRealLength < 18) {
            str += getNumbersOfSpace(18 - strRealLength);
        } else {
            str = str.substring(0, 17); // 这个会有问题
        }
        return str;
    }

    private void updateAwardNameList() {
        for (Person person : mPersonsToShow) {
            mAwardNameList.add(person.getInfo());
        }
//        updateAwardNameTVByNameList();
    }

//    private void updateAwardNameTVByNameList() {
//        if (mAwardNameList == null) {
//            return;
//        }
//        if (mAwardNameList.size() < 1) {
//            return;
//        }
//        String str = "";
//        for (int i = 0; i < mAwardNameList.size(); ++i) {
//            str += (mAwardNameList.get(i) + "\n\n");
//        }
//        mAwardListTV.setText(str);
//        mAwardNameSV.fullScroll(View.FOCUS_DOWN);
//    }

    // 状态检查
    private boolean isDrawEnd() {
        return mCurrentAward.getDrewTimes() >= mCurrentAward.getTotalDrawTimes();
    }

    // 中断恢复
    private void restoreData() {
        mAwardNameList = DbUtil.getAwardPeopleArrayList(this, mCurrentAward);
//        updateAwardNameTVByNameList();
    }


}
