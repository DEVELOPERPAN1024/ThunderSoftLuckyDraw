package cn.thundersoft.codingnight.acitivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.db.DbUtil;
import cn.thundersoft.codingnight.models.Award;
import cn.thundersoft.codingnight.models.Person;
import cn.thundersoft.codingnight.util.MyRandom;
import cn.thundersoft.codingnight.util.StringUtil;

public class LuckyDrawActivityFinal extends BaseActivity {

    private static final int EMPTY_DRAW = 1;
    private static final int START_DRAW = 0;

    private static final int PLACE_NAME_IN_SEQUENCE = 2;
    private static final int PLACE_NAME_DONE = 3;

    private static final int SINGLE_NAME_LENGTH = 18;
    private static final int SINGLE_NAME_LENGTH_SHORT = 16;
    private static final int SINGLE_MONEY_LENGTH = 8;

    private Resources mRes;
    // view
    private FrameLayout mDrawBtnLayout;
    private ImageView mDrawButton;
    private TextView mHintTextView;
    private TextView mRandomTextView;
    private LinearLayout mMainBGLL;
    // data
    private Award mCurrentAward;
    private List<String> mAwardNameList = new ArrayList<>();

    private List<Person> mTotalPersons = new ArrayList<>();
    private List<Person> mPersonsToShow = new ArrayList<>();

    private List<Integer> mRedPackageMoneys = new ArrayList<>();


    // state
    private boolean mIsDrawing;
    private boolean hasMoneyAttached = false;
    private boolean mIsRedPackage;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_DRAW:
                    updateRandomList();
                    break;
                case EMPTY_DRAW:
                    mDrawBtnLayout.setEnabled(true);
                    mRandomTextView.append("所有人都中奖啦~\n添加一个允许重复的奖项试一下咯");
                    updateButtonState();
                    break;
                case PLACE_NAME_IN_SEQUENCE:
                    updateRandomList();
                    Person p = (Person) msg.obj;
                    if (p.getMoney() < 0) {
                        DbUtil.insertWinner(LuckyDrawActivityFinal.this,
                                p.getId(),
                                mCurrentAward.getId());
                    } else {
                        DbUtil.insertWinner(LuckyDrawActivityFinal.this,
                                p.getId(),
                                mCurrentAward.getId(),
                                p.getMoney());
                    }
                    //insertWinner(mPersonsToShow.get(mPersonsToShow.size() - 1).getId());
                    break;
                case PLACE_NAME_DONE:
                    mDrawBtnLayout.setEnabled(true);
                    DbUtil.updateAward(LuckyDrawActivityFinal.this, mCurrentAward);
                    if (mCurrentAward.isSpecial()) {
                        mDrawButton.setImageResource(R.drawable.ic_money);
//                        mCurrentAward.decreaseDrewTimes(); // 保证红包按钮状态更新后不会因为isDrawEnd结束
                    }
                    break;
                default:
                    break;
            }
        }
    };

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

    private void initBg() {
        Glide.with(this).load(R.drawable.background1).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                mMainBGLL.setBackground(resource);
            }
        });
    }

    @Override
    public void onBackPressed() { // need test
        if (mTotalPersons.size() == 0) {
            super.onBackPressed();
            return;
        }
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
        if (mIsRedPackage) {
            mTotalPersons = DbUtil.getNoMoneyPersonList(this);
        } else {
            if (mCurrentAward.isRepeatable()) {
                mTotalPersons = DbUtil.getAllPerson(this);
            } else {
                mTotalPersons = DbUtil.getUnawardPersons(this);
            }
        }

        if (mTotalPersons.size() == 0) {
            mHandler.sendEmptyMessage(EMPTY_DRAW);
        } else {
            mIsRedPackage = mCurrentAward.isSpecial();
            mIsDrawing = true; // 打开该Activity即开始滚动
        }

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
                mCurrentAward.increaseDrewTimes();
                updateHintText();
                if (!mIsRedPackage)
                    updateButtonState();
                else
                    mDrawButton.setImageResource(R.drawable.ic_money);
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
                int count = getDrawCountForThisTime();
                if (mIsRedPackage) {
                    mRedPackageMoneys = MyRandom.getMoneys(count, Integer.parseInt(mCurrentAward.getDetail()));
                }
                for (int i = 0; i < count; i++) {
                    if (mTotalPersons.size() == 0) {
                        mHandler.sendEmptyMessage(EMPTY_DRAW);
                        return;
                    }
                    Person p = MyRandom.getRandomPersion(mTotalPersons);
                    if (mIsRedPackage) p.setMoney(mRedPackageMoneys.get(i));
                    mPersonsToShow.add(p);
                    Message msg = new Message();
                    msg.obj = p;
                    msg.what = PLACE_NAME_IN_SEQUENCE;
                    mHandler.sendMessage(msg);
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

    private void initView() {
        mDrawButton = (ImageView) findViewById(R.id.lucky_draw_final_button);
        mRandomTextView = (TextView) findViewById(R.id.lucky_draw_final_text_random);
        mHintTextView = (TextView) findViewById(R.id.lucky_draw_final_text_hint);
        mDrawBtnLayout = (FrameLayout) findViewById(R.id.fab_container);
        mMainBGLL = (LinearLayout) findViewById(R.id.lucky_draw_final_ll);
        initBg();
        updateHintText();
        updateButtonState();
    }

    private void initAction() {

        mDrawBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsRedPackage) {
                    if (isDrawEnd() && mIsDrawing && !hasMoneyAttached) { // 初始状态drawEnd
                        updateButtonState();
                        finish();
                        overridePendingTransition(R.anim.enter_from_left, R.anim.out_to_right);
                        return;
                    }
                    if (mIsDrawing) { // stop
                        animateStopDrawing();
                    } else if (!hasMoneyAttached) {
                        updateRandomListWithMoney();
                        hasMoneyAttached = true;
                        mDrawButton.setImageResource(R.drawable.ic_arrow_left_white_24dp);
                    } else {
                        finish();
                    }
                } else {
                    // 列表滚动状态切换
                    // 按钮状态切换
                    // hint信息切换
                    // 获奖列表更新
                    if (isDrawEnd()) {
                        updateButtonState();
                        finish();
                        overridePendingTransition(R.anim.enter_from_left, R.anim.out_to_right);
                        return;
                    }
                    if (mTotalPersons.size() == 0) {
                        finish();
                        overridePendingTransition(R.anim.enter_from_left, R.anim.out_to_right);
                        return;
                    }
                    if (mIsDrawing) { // stop
                        animateStopDrawing();
                    } else { // start
                        mIsDrawing = true;
                        getNameList();
                        updateButtonState();
                    }
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
                    mPersonsToShow = MyRandom.getRandomListFake(mTotalPersons, getDrawCountForThisTime());
                    if (mPersonsToShow.size() == 0) {
                        mIsDrawing = false;
                        mHandler.sendEmptyMessage(EMPTY_DRAW);
                    } else {
                        mHandler.sendEmptyMessage(START_DRAW); //按钮停止再发
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    //每次获取的数量尽可能相近
    private int getDrawCountForThisTime() {
        int total = mCurrentAward.getCount();
        //余数
        int extraNum = total % mCurrentAward.getTotalDrawTimes();
        //商
        int standNum = total / mCurrentAward.getTotalDrawTimes();
        int drewTimes;
        if (mIsDrawing) {
            drewTimes = mCurrentAward.getDrewTimes() + 1;
        } else {
            drewTimes = mCurrentAward.getDrewTimes();
        }
        if (drewTimes <= extraNum) {
            return standNum + 1;
        } else {
            return standNum;
        }
    }

    private void updateButtonState() {
        if (mTotalPersons.size() == 0) {
            mDrawButton.setImageResource(R.drawable.ic_arrow_left_white_24dp);
            return;
        }
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
        String str = String.format(strToShow, mCurrentAward.getDrewTimes(), mCurrentAward.getTotalDrawTimes());
        if (mIsRedPackage) {
            String strTotalMoney = mRes.getString(R.string.lucky_draw_final_draw_hint_money);
            str += "\n";
            str += String.format(strTotalMoney, mCurrentAward.getDetail());
        }
        mHintTextView.setText(str);
    }

    private void updateRandomList() {
        String str = "";
        if (mPersonsToShow.size() < 1) {
            return;
        }
        if (getDrawCountForThisTime() > 10) {
            for (int i = 0; i < mPersonsToShow.size(); ++i) {
                if (i + 1 < mPersonsToShow.size()) {
                    str += (controlStringLength(mPersonsToShow.get(i).getInfo(), SINGLE_NAME_LENGTH)
                            + "  "
                            + controlStringLength(mPersonsToShow.get(++i).getInfo(), SINGLE_NAME_LENGTH)
                            + "\n");
                } else {
                    str += (controlStringLength(mPersonsToShow.get(i).getInfo(), SINGLE_NAME_LENGTH))
                            + "  "
                            + getNumbersOfSpace(SINGLE_NAME_LENGTH);
                }
            }
        } else {
            for (int i = 0; i < mPersonsToShow.size(); ++i) {
                str += (mPersonsToShow.get(i).getInfo() + "\n");
            }
        }

        mRandomTextView.setText(str);
        calculateRandomTextShowStyleByLines(getDrawCountForThisTime());
    }

    private void updateRandomListWithMoney() {
        String str = "";
        if (mPersonsToShow.size() < 1) {
            return;
        }

        if (getDrawCountForThisTime() > 10) {
            for (int i = 0; i < mPersonsToShow.size(); ++i) {
                if (i + 1 < mPersonsToShow.size()) {
                    str += (controlHTMLStringLength(mPersonsToShow.get(i).getInfo(), SINGLE_NAME_LENGTH_SHORT)
                            + "<font color=\"#f44336\">"
                            + controlHTMLStringLength("¥" + mRedPackageMoneys.get(i).toString() + ".00", SINGLE_MONEY_LENGTH)
                            + "</font>"
                            + controlHTMLStringLength(mPersonsToShow.get(++i).getInfo(), SINGLE_NAME_LENGTH_SHORT)
                            + "<font color=\"#f44336\">"
                            + controlHTMLStringLength("¥" + mRedPackageMoneys.get(i).toString() + ".00", SINGLE_MONEY_LENGTH)
                            + "</font>"
                            + "<br />");
                } else {
//                    str += (controlStringLength(mPersonsToShow.get(i).getInfo(), SINGLE_NAME_LENGTH))
//                            + "  "
//                            + getNumbersOfSpace(SINGLE_NAME_LENGTH);
                    str += (controlHTMLStringLength(mPersonsToShow.get(i).getInfo(), SINGLE_NAME_LENGTH_SHORT)
                            + "<font color=\"#f44336\">"
                            + controlHTMLStringLength("¥" + mRedPackageMoneys.get(i).toString() + ".00", SINGLE_MONEY_LENGTH)
                            + "</font>"
                            + controlHTMLStringLength("", SINGLE_NAME_LENGTH_SHORT)
                            + "<font color=\"#f44336\">"
                            + controlHTMLStringLength("", SINGLE_MONEY_LENGTH)
                            + "</font>"
                            + "<br />");
                }
            }
        } else {
            for (int i = 0; i < mPersonsToShow.size(); ++i) {
                str += (controlHTMLStringLength(mPersonsToShow.get(i).getInfo(), SINGLE_NAME_LENGTH_SHORT)
                        + "<font color=\"#f44336\">"
                        + controlHTMLStringLength("¥" + mRedPackageMoneys.get(i).toString() + ".00", SINGLE_MONEY_LENGTH)
                        + "</font>"
                        + "<br />");
            }
        }

        mRandomTextView.setText(Html.fromHtml(str));
        calculateRandomTextShowStyleByLines(getDrawCountForThisTime());
    }

    private void calculateRandomTextShowStyleByLines(int lines) {
        if (lines == 1) {
            updateRandomTextStyle(24f, 0, 0);
            return;
        }
        if (lines > 1 && lines <= 3) {
            updateRandomTextStyle(20f, 0, 0);
            return;
        }
        if (lines <= 10 && lines > 3) {
            updateRandomTextStyle(16f, 0, 0);
            return;
        }
        if (lines > 10) {
            updateRandomTextStyle(14f, 0, 0);
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

    private String getNumbersHTMLOfSpace(int num) {
        String str = "";
        for (int i = 0; i < num; i++) {
            str += "&emsp;";
        }
        return str;
    }

    private String controlStringLength(String str, int length) {
        int strRealLength = StringUtil.getStringRealLength(str);
        if (strRealLength < length) {
            str += getNumbersOfSpace(length - strRealLength);
        } else {
            str = str.substring(0, length - 1); // 这个会有问题
        }
        return str;
    }

    private String controlHTMLStringLength(String str, int length) {
        int strRealLength = StringUtil.getStringRealLength(str);
        if (strRealLength < length) {
            str += getNumbersHTMLOfSpace(length - strRealLength);
        } else {
            str = str.substring(0, length - 1); // 这个会有问题
        }
        return str;
    }

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
