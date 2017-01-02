package cn.thundersoft.codingnight.acitivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.models.Award;

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
    private List<String> mRandomNameList = new ArrayList<>();
    private ArrayAdapter<String> mArrayAdapter;
    // state
    private boolean mIsDrawing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_draw_final);
        init();
    }

    private void init() {
        initData();
        initView();
        initAction();
    }

    private void initData() {
        // 是否需要检查award对象为空？？
        mCurrentAward = (Award) getIntent().getExtras().get("award");

        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mAwardNameList);

        mIsDrawing = true; // 打开该Activity即开始滚动
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
                // 列表旋转状态切换
                // 按钮状态切换
                // hint信息切换
                updateHintText();
                // 获奖列表更新
            }
        });
    }


    private void updateHintText() {
        if (mIsDrawing) return;
        String strToShow = getResources().getString(R.string.lucky_draw_final_draw_hint);
        mHintTextView.setText(String.format(strToShow, mCurrentAward.getDrewTimes(), mCurrentAward.getCount()));
    }

    // 中断恢复
    private void checkDrawState() {

    }



}
