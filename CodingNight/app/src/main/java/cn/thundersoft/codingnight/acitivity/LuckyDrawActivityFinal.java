package cn.thundersoft.codingnight.acitivity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    // view
    private Button mDrawButton;
    private ScrollView mRandomScrollView;
    private ListView mAwardListView;
    private TextView mHintTextView;
    private TextView mRandomTextView;
    // data
    private Award mCurrentAward;
    private List<String> mAwardNameList = new ArrayList<>();
    private List<Person> mRandomNameList = new ArrayList<>();
    private ArrayAdapter<String> mArrayAdapter;

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
        getNameList(); // 打开即滚动
    }


    @Override
    public void onBackPressed() { // need test
        mBackPressTime += 1;
        if (mBackPressTime == 2) {
            if (mTimer != null) mTimer.cancel();
            super.onBackPressed();
        } else {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mBackPressTime = 0;
                }
            }, 3000); // 3秒内
            Toast toast =  Toast.makeText(this, getText(R.string.lucky_draw_final_back_hint), Toast.LENGTH_SHORT);
            toast.show();
        }
    }



    private void init() {
        initData();
        initView();
        initAction();
    }

    private void initData() {
        // 是否需要检查award对象为空？？
//        int awardId = getIntent().getExtras().getInt("award_id");
//        mCurrentAward = (Award) getIntent().getExtras().get("award");
        mCurrentAward = (Award) getIntent().getSerializableExtra("award");

        mTotalPersons = DbUtil.getAllPerson(this);
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mAwardNameList);

        mIsDrawing = true; // 打开该Activity即开始滚动

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 1) {
                    updateRandomList();
                    if (!mIsDrawing) {
                        for (int i = 0; i < mPersonsToShow.size(); i++) {
                            updatePersonAwardState(mPersonsToShow.get(i));
                            DbUtil.insertWinner(LuckyDrawActivityFinal.this,
                                                mPersonsToShow.get(i).getId(),
                                                mCurrentAward.getId());
                            updateAwardNameList();
                        }
                    }
                }

            }
        };
    }

    private void initView() {
        mDrawButton = (Button) findViewById(R.id.lucky_draw_final_button);
        mRandomScrollView = (ScrollView) findViewById(R.id.lucky_draw_final_scrollview);
        mRandomTextView = (TextView) findViewById(R.id.lucky_draw_final_text_random);
        mHintTextView = (TextView) findViewById(R.id.lucky_draw_final_text_hint);
        mAwardListView = (ListView) findViewById(R.id.lucky_draw_final_list_award);
        mAwardListView.setAdapter(mArrayAdapter);

        updateHintText();

    }



    private void initAction() {
        mDrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 列表滚动状态切换
                // 按钮状态切换
                // hint信息切换

                // 获奖列表更新
                if (mIsDrawing) { // stop
                    mCurrentAward.increaseDrewTimes();
                    updateHintText();
                } else { // start
                    getNameList();

                }
                updateButtonState();
                mIsDrawing = !mIsDrawing;
            }
        });
    }


    // 抽奖
    private void getNameList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mIsDrawing) {
                    mPersonsToShow = MyRandom.getRandomList(mTotalPersons, getDrawCountForThisTime());
                    mHandler.sendEmptyMessage(1);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
        if (!isDrawEnd()) {
            mDrawButton.setBackground(mIsDrawing ?
                    getDrawable(R.drawable.person_item_modify_button_background) :
                    getDrawable(R.drawable.person_item_delete_button_background));
        } else {
            mDrawButton.setEnabled(false);
        }
    }

    private void updateHintText() {
        String strToShow = getResources().getString(R.string.lucky_draw_final_draw_hint);
        mHintTextView.setText(String.format(strToShow, mCurrentAward.getDrewTimes(), mCurrentAward.getTotalDrawTimes()));
    }

    private void updateRandomList() {
        String str = "";
        for (int i = 0; i < mPersonsToShow.size(); ++i) {
            str += (mPersonsToShow.get(i).getInfo() + "\n");
        }
        mRandomTextView.setText(str);
    }

    private void updateAwardNameList() {
        for (Person person : mPersonsToShow) {
            mAwardNameList.add(person.getInfo());
        }
        mArrayAdapter.notifyDataSetChanged();
    }

    // 状态检查
    private boolean isDrawEnd() {
        return mCurrentAward.getDrewTimes() == mCurrentAward.getTotalDrawTimes();
    }

    // 中断恢复
    private void checkDrawState() {

    }



}
