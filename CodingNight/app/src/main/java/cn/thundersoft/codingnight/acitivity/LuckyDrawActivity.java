package cn.thundersoft.codingnight.acitivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.thundersoft.codingnight.R;

public class LuckyDrawActivity extends AppCompatActivity {

    TextView mTvAwardDetail;
    Spinner mSpAwards;
    Spinner mSpDrawTimes;
    Button mBtnStart;
    ImageView mIvAwardImage;

    ArrayList<String> mArrayAward = new ArrayList<>();
    ArrayList<String> mArrayCount = new ArrayList<>();
    ArrayAdapter<String> mSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_draw);

        initData();
        initViews();
        initActions();

    }

    private void initData() {
        mArrayCount.add("1次");
        mArrayCount.add("2次");
        mArrayCount.add("3次");
        mArrayCount.add("4次");
        mArrayCount.add("5次");

        mArrayAward.add("特等奖");
        mArrayAward.add("一等奖");
        mArrayAward.add("二等奖");
        mArrayAward.add("三等奖");

    }

    private void initViews() {

        mTvAwardDetail = (TextView) findViewById(R.id.lucky_draw_award_detail);
        mSpAwards = (Spinner) findViewById(R.id.lucky_draw_award_spinner);
        mSpDrawTimes = (Spinner) findViewById(R.id.lucky_draw_count_spinner);
        mIvAwardImage = (ImageView) findViewById(R.id.lucky_draw_award_image);
        mBtnStart = (Button) findViewById(R.id.lucky_draw_award_start_button);




    }

    private void initActions() {

        mSpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, mArrayAward);
        mSpinnerAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        mSpAwards.setAdapter(mSpinnerAdapter);
        mSpAwards.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showToast("Spinner1: position=" + position + " id=" + id);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showToast("Spinner2: unselected");
            }
        });



        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


    void showToast(CharSequence msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
