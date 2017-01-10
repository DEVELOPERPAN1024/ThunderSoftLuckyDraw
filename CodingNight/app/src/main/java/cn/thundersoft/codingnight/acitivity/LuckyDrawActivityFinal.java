package cn.thundersoft.codingnight.acitivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.db.DbUtil;
import cn.thundersoft.codingnight.models.Award;
import cn.thundersoft.codingnight.models.Person;
import cn.thundersoft.codingnight.util.MyRandom;

public class LuckyDrawActivityFinal extends AppCompatActivity {

    public static final int REQ_CODE = 2017;
    private static final int STOP_DRAW = 1;
    private static final int START_DRAW = 0;
    // view
    private ImageView mDrawButton;
    private ScrollView mRandomScrollView;
    private TextView mHintTextView;
    private TextView mRandomTextView;
    private TextView mAwardListTV;
    private ScrollView mAwardNameSV;
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
        init();
        if (!isDrawEnd()) {
            getNameList(); // 打开即滚动
        }else{
            mRandomTextView.setText("已经抽完了\ncheck右边中将名单，或去首页奖项中查看及完成更多其它操作");
        }
    }

    @Override
    public void onBackPressed() { // need test
        mBackPressTime += 1;
        if (mBackPressTime == 2) {
            if (mTimer != null) mTimer.cancel();
            super.onBackPressed();
        } else {
            if(isDrawEnd()){
                super.onBackPressed();
                return;
            }
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mBackPressTime = 0;
                }
            }, 3000); // 3秒内
            Toast toast = Toast.makeText(this, getText(R.string.lucky_draw_final_back_hint), Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    private void init() {
        initData();
        initView();
        restoreData();
        initAction();
    }

    private void initData() {
        mCurrentAward = (Award) getIntent().getSerializableExtra("award");

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

                if (msg.what == START_DRAW) {
                    updateRandomList();
                }

                if (msg.what == STOP_DRAW) {
                    updateRandomList();
//                    if (!mIsDrawing) {
                    for (int i = 0; i < mPersonsToShow.size(); i++) {
                        //一个人获取多个奖项?
                        updatePersonAwardState(mPersonsToShow.get(i));
                        DbUtil.insertWinner(LuckyDrawActivityFinal.this,
                                mPersonsToShow.get(i).getId(),
                                mCurrentAward.getId());
                        DbUtil.updateAward(LuckyDrawActivityFinal.this, mCurrentAward);
                    }
                    updateAwardNameList();
//                    }
                }
            }
        };
    }

    private void initView() {
        mDrawButton = (ImageView) findViewById(R.id.lucky_draw_final_button);
        mRandomTextView = (TextView) findViewById(R.id.lucky_draw_final_text_random);
        mHintTextView = (TextView) findViewById(R.id.lucky_draw_final_text_hint);
        mAwardListTV = (TextView) findViewById(R.id.award_name_list_tv);
        mAwardNameSV = (ScrollView) findViewById(R.id.award_name_sv);
        updateHintText();
        updateButtonState();

    }


    private void initAction() {

        mDrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 列表滚动状态切换
                // 按钮状态切换
                // hint信息切换
                // 获奖列表更新
                if (isDrawEnd()) {
                    updateButtonState();
                    finish();
//                    Toast.makeText(LuckyDrawActivityFinal.this, "已经抽完了", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mIsDrawing) { // stop
                    mCurrentAward.increaseDrewTimes();
                    updateHintText();
//                    DbUtil.updateAward(LuckyDrawActivityFinal.this, mCurrentAward); 放到handler更新
                    mIsDrawing = false; // stop while in thread
                } else { // start
                    mIsDrawing = true;
                    getNameList();
                }
                updateButtonState();
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
                    mPersonsToShow = MyRandom.getRandomListFake(mTotalPersons, getDrawCountForThisTime());
                    mHandler.sendEmptyMessage(START_DRAW); //按钮停止再发
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //滚动时点击抽奖，真正的随机抽奖
                if(!mIsDrawing){
                    mPersonsToShow = MyRandom.getRandomList(mTotalPersons, getDrawCountForThisTime());
                    mHandler.sendEmptyMessage(STOP_DRAW);
                }
            }
        }).start();
    }


    private int getDrawCountForThisTime() {
        int total = mCurrentAward.getCount();
        if (mCurrentAward.getDrewTimes() == mCurrentAward.getTotalDrawTimes()) {
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
        String strToShow = getResources().getString(R.string.lucky_draw_final_draw_hint);
        mHintTextView.setText(String.format(strToShow, mCurrentAward.getDrewTimes(), mCurrentAward.getTotalDrawTimes()));
    }

    private void updateRandomList() {
        String str = "";
        if (mPersonsToShow.size() > 10) {
            for (int i = 0; i < mPersonsToShow.size(); ++i) {
                str += (mPersonsToShow.get(i).getInfo() + "\n");
            }
        } else {
            for (int i = 0; i < mPersonsToShow.size(); ++i) {
                str += (mPersonsToShow.get(i).getInfo() + "\n");
            }
        }
        mRandomTextView.setText(str);
    }

    private void updateAwardNameList() {
        for (Person person : mPersonsToShow) {
            mAwardNameList.add(person.getInfo());
        }
        updateAwardNameTVByNameList();
    }

    private void updateAwardNameTVByNameList() {
        if (mAwardNameList == null) {
            return;
        }
        if (mAwardNameList.size() < 1) {
            return;
        }
        String str = "";
        for (int i = 0; i < mAwardNameList.size(); ++i) {
            str += (mAwardNameList.get(i) + "\n\n");
        }
        mAwardListTV.setText(str);
        mAwardNameSV.fullScroll(View.FOCUS_DOWN);
    }

    // 状态检查
    private boolean isDrawEnd() {
        return mCurrentAward.getDrewTimes() >= mCurrentAward.getTotalDrawTimes();
    }

    // 中断恢复
    private void restoreData() {
        mAwardNameList = DbUtil.getAwardPeopleArrayList(this, mCurrentAward);
        updateAwardNameTVByNameList();
    }


}
