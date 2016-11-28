package cn.thundersoft.codingnight.acitivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.db.DbUtil;
import cn.thundersoft.codingnight.models.Award;
import cn.thundersoft.codingnight.models.Person;
import cn.thundersoft.codingnight.util.MyRandom;

public class LuckyDrawActivity extends AppCompatActivity {

    private TextView mTvAwardDetail;
    private TextView mTvAwardNames;
    private Spinner mSpAwards;
    private Spinner mSpDrawTimes;
    private Button mBtnStart;
    private ImageView mIvAwardImage;
    private LinearLayout mBottomLayout;
    /*private RelativeLayout mAwardListLayout;*/

    private ArrayList<String> mArrayAward = new ArrayList<>();
    private List<Award> mAwards = new ArrayList<>();
    private List<Person> mPersons = new ArrayList<>();
    private ArrayList<String> mArrayCount = new ArrayList<>();
    private List<Person> mPersonAwarded = new ArrayList<>();

    private int mTotalDrawCount;
    private int mTotalDrawCountStatic;
    private boolean mIsDrawing;
    private int mTotalAwards;
    private int mTotalPersons;
    private int mCurrentShowCount;
    private int mAwardID;

    private ArrayList<String> mLastRandomList = new ArrayList<>();

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showNames(mLastRandomList);
            if (!mIsDrawing) {
                for (int i = 0; i < mPersonAwarded.size(); i++) {
                    setLocalPersonAwardState(mPersonAwarded.get(i));
                    DbUtil.insertWinner(LuckyDrawActivity.this, mPersonAwarded.get(i).getId(), mAwardID);
                }
            }
        }
    };

    private void setLocalPersonAwardState(Person p) {
        for (Person person : mPersons) {
            if (person.getId() == p.getId()) {
                person.setPrize(mAwardID);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_draw);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.begin_lucky_draw);
        }
        initData();
        initViews();
        initActions();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        mArrayCount.add(getString(R.string.lucky_draw_spinner_draw_count_tint));
        mArrayCount.add("索性1次全抽了");
        mArrayCount.add("抽2次意思意思");
        mArrayCount.add("比抽2次多一次");
        mArrayCount.add("细水长流抽4次");
        mArrayCount.add("我就是要抽5次");

        /*mArrayAward.add("尊贵的特等奖");
        mArrayAward.add("不错的一等奖");
        mArrayAward.add("凑合的二等奖");
        mArrayAward.add("简陋的三等奖");*/
        fillAwards();

        mTotalDrawCount = 0;
        mTotalAwards = 0;
        if (mAwards.size() <= 0) {
            mAwardID = -1;
            showToast(getString(R.string.lucky_draw_award_data_null_tint));
        } else {
            mAwardID = mAwards.get(0).getId();
        }

        mPersons = DbUtil.getAllPerson(this);
        if (mPersons.size() <= 0) {
            showToast(getString(R.string.lucky_draw_person_data_null_tint));
        }

        mIsDrawing = false;
    }

    private void clearData() {
        mTotalDrawCount = 0;
        mTotalAwards = 0;
        mSpAwards.setSelection(0, true);
        mSpDrawTimes.setSelection(0, true);
    }

    private void initViews() {

        mTvAwardDetail = (TextView) findViewById(R.id.lucky_draw_award_detail);
        mTvAwardNames = (TextView) findViewById(R.id.lucky_draw_award_names);
        mTvAwardNames.setLineSpacing(1, 2);
        mTvAwardNames.setMovementMethod(new ScrollingMovementMethod());

        mSpAwards = (Spinner) findViewById(R.id.lucky_draw_award_spinner);
        mSpDrawTimes = (Spinner) findViewById(R.id.lucky_draw_count_spinner);
        mIvAwardImage = (ImageView) findViewById(R.id.lucky_draw_award_image);
        mBtnStart = (Button) findViewById(R.id.lucky_draw_award_start_button);
        mBottomLayout = (LinearLayout) findViewById(R.id.lucky_draw_award_bottom_linear);

        /*mAwardListLayout = (RelativeLayout) findViewById(R.id.lucky_draw_award_list);*/
    }

    private void initActions() {

        ArrayAdapter<String> mSpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, mArrayAward);
        mSpinnerAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        mSpAwards.setAdapter(mSpinnerAdapter);
        mSpAwards.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToast("Spinner1: position=" + position + " id=" + id);
                if (0 == position) {
                    mTotalAwards = 0;
                    return;
                }
                mTotalAwards = mAwards.get(position).getCount();
                mAwardID = mAwards.get(position).getId();
                showToast("position is " + position + "  award count is " + mTotalAwards);
                setAwardDetails(position);
                mBottomLayout.setVisibility(View.VISIBLE);
                mIvAwardImage.setVisibility(View.VISIBLE);
                /*mAwardListLayout.setVisibility(View.GONE);
                mAwardListLayout.removeAllViews();*/
                mTvAwardNames.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showToast("Spinner1: unselected");
            }
        });

        mSpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, mArrayCount);
        mSpinnerAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        mSpDrawTimes.setAdapter(mSpinnerAdapter);
        mSpDrawTimes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToast("Spinner2: position=" + position + " id=" + id);
                mTotalDrawCount = position;
                mTotalDrawCountStatic = mTotalDrawCount;
                mBtnStart.setEnabled(true);
                mBtnStart.setText(R.string.lucky_draw_button_start);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showToast("Spinner2: unselected");
            }
        });


        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if (mTotalPersons <= 0)
                //    return;
                if (0 == mTotalDrawCount) {
                    showToast("请先选择抽奖次数^_^");
                    return;
                }

                if (0 == mTotalAwards) {
                    showToast("请先选择奖品^_^");
                    return;
                }

                if (mTotalAwards < mTotalDrawCount) {
                    showToast("奖项数目比抽奖次数少，没时间优化了");
                    return;
                }

                mIsDrawing = !mIsDrawing;
                onStartDraw();
            }
        });


    }

    private void fillAwards() {
        mArrayAward.add(getString(R.string.lucky_draw_spinner_prize_tint));
        mAwards = DbUtil.getAwards(this);
        for (Award award : mAwards) {
            mArrayAward.add(award.getName());
        }
    }

    private void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void onStartDraw() {

        if (mTotalDrawCount > 0) {
            mBtnStart.setText(mIsDrawing ? R.string.lucky_draw_button_end : R.string.lucky_draw_button_start);
            mSpDrawTimes.setEnabled(false);
            if (mIsDrawing) {
                mBottomLayout.setVisibility(View.GONE);
                /*mAwardListLayout.setVisibility(View.VISIBLE);*/
                mTvAwardNames.setVisibility(View.VISIBLE);

                getNameList();
                mTotalDrawCount--;
            }
        } else {
            mBtnStart.setText(R.string.lucky_draw_button_no_chance);
            mBtnStart.setEnabled(false);
            mSpDrawTimes.setEnabled(true);
            clearData();
        }
    }

    /*private Thread myThread = new Thread(new Runnable() {
        @Override
        public void run() { mLastRandomList.add(mPersons.get(randoms.get(i)).getInfo());
                                mPersonAwarded.add(mPersons.get(randoms.get(i)));
            while (mIsDrawing) {
                List<Integer> randoms = MyRandom.getRandomList(mTotalPersons, mCurrentShowCount);

                mLastRandomList.clear();
                mPersonAwarded.clear();
                if (mTotalPersons > 0)
                    for (int i = 0; i < mCurrentShowCount; ++i) {
                        mLastRandomList.add(mPersons.get(randoms.get(i)).getInfo());
                        mPersonAwarded.add(mPersons.get(randoms.get(i)));
                    }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(1);
            }
        }
    });*/


    private void getNameList() {
        //mPersons = DbUtil.getAllPerson(this);

        mTotalPersons = mPersons.size();
        if (mTotalDrawCount > 1) {
            Log.d("DBW", "mTotalDrawCount > 1  " + mTotalDrawCount);
            mCurrentShowCount = mTotalAwards / mTotalDrawCountStatic;
        } else {
            Log.d("DBW", "mTotalDrawCount " + mTotalDrawCount);
            mCurrentShowCount = mTotalAwards / mTotalDrawCountStatic + mTotalAwards % mTotalDrawCountStatic;
        }
        Log.d("DBW", "mCurrentShowCount" + mCurrentShowCount);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mIsDrawing) {


                    List<Person> mPersonAwarded = MyRandom.getRandomList(mPersons, mCurrentShowCount);

                    mLastRandomList.clear();
           //         mPersonAwarded.clear();
                    if (mTotalPersons > 0)
                        for (int i = 0; i < mCurrentShowCount; ++i) {
                            mLastRandomList.add(mPersonAwarded.get(i).getInfo());
               //             mPersonAwarded.add(mPersons.get(randoms.get(i)));
                        }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessage(1);
                }
            }
        }).start();

    }

    private void showNames(ArrayList<String> list) {
        Log.d("draw", list.size() + "");
        String str = "";
        for (int i = 0; i < list.size(); ++i) {

            str += (list.get(i) + "\n");
        }
        Log.d("draw", str);
        mTvAwardNames.setText(str);
    }

    private int getRandomSeed() {
        if (mTotalPersons == 0)
            return 0;
        return (int) (System.nanoTime() / Math.PI) % mTotalPersons;
    }

    private void setAwardDetails(int index) {
        mTvAwardDetail.setText(mAwards.get(index).getDetial());

        //if (mAwards.get(index).getPicUrl() != null) {
        Glide.with(this).load(mAwards.get(index).getPicUrl()).centerCrop().into(mIvAwardImage);
        //}
    }





}
