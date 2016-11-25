package cn.thundersoft.codingnight.acitivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.db.DbUtil;
import cn.thundersoft.codingnight.models.Award;

/**
 * Created by pandroid on 11/26/16.
 */

public class AwardDetailActivity extends AppCompatActivity {

    private Award mMainBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award_detail_layout);
        mMainBean = (Award) getIntent().getSerializableExtra("bean");
        if (mMainBean != null) {
            initView();
        }
    }

    private void initView() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("删除");
        item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("删除")) {
            DbUtil.deleteAward(this, mMainBean);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
