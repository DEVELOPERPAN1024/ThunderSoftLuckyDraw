package cn.thundersoft.codingnight.acitivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.db.DbUtil;
import cn.thundersoft.codingnight.models.Award;

public class AwardDetailActivity extends AppCompatActivity {

    @Bind(R.id.detail_award_name)
    TextView mAwardNameTV;
    @Bind(R.id.detail_award_count)
    TextView mAwardCountTV;
    @Bind(R.id.detail_award_detail)
    TextView mAwardDetailTV;
    @Bind(R.id.check_award_people_list)
    TextView mCheckAwardPeopleListBtn;

    private Award mMainBean;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            new AlertDialog.Builder(AwardDetailActivity.this)
                    .setTitle(mMainBean.getName()+"中将名单")
                    .setMessage(msg.getData().getString("peoplelist"))
                    .create()
                    .show();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award_detail_layout);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.award_detail);
        }
        mMainBean = (Award) getIntent().getSerializableExtra("bean");
        if (mMainBean != null) {
            initView();
        }
    }

    private void initView() {
        mAwardDetailTV.setText(mMainBean.getDetail());
        mAwardNameTV.setText(mMainBean.getName());
        mAwardCountTV.setText("共" + mMainBean.getCount() + "个");
        mCheckAwardPeopleListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String peopleList = DbUtil.getAwardPeopleList(AwardDetailActivity.this, mMainBean);
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("peoplelist", peopleList);
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                    }
                }).start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("删除");
//        item.setIcon(R.drawable.ic_delete);
        item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (Objects.equals(item.getTitle(), "删除")) {
            DbUtil.deleteAward(this, mMainBean);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
