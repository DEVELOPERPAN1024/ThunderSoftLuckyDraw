package cn.thundersoft.codingnight.acitivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.db.DbUtil;
import cn.thundersoft.codingnight.models.Award;
import cn.thundersoft.codingnight.util.RegexCheckUtil;

/**
 * Created by pandroid on 2017/1/18.
 */

public class AddSpecialActivity extends AbsStoragePermissionCheckActivity {

    @Bind(R.id.award_count_edt)
    EditText mAwardCountEdt;
    @Bind(R.id.award_name_edt)
    EditText mAwardNameEdt;
    @Bind(R.id.award_is_repeatable_cb)
    CheckBox mIsRepeatablCB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_special_award_activity);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("现金红包");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("保存");
        item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (Objects.equals(item.getTitle(), "保存")) {
            if (mAwardNameEdt.getText().length() < 1 || mAwardCountEdt.getText().length() < 1) {
                Toast.makeText(this, "必要数据为空，请再次检查输入", Toast.LENGTH_SHORT).show();
                return true;
            }
            int totalMoney = Integer.valueOf(mAwardNameEdt.getText().toString());
            if (totalMoney < 100){
                Toast.makeText(this,"钱少了点吧",Toast.LENGTH_SHORT).show();
            }
            int peopleCount = Integer.valueOf(mAwardCountEdt.getText().toString());
            if(peopleCount<1){
                Toast.makeText(this,"数据错误",Toast.LENGTH_SHORT).show();
            }
            //save to db
            Award bean = new Award();
            bean.setName(mAwardNameEdt.getText().toString()+"现金红包");
            bean.setCount(peopleCount);
            bean.setDetail(totalMoney+"");
            bean.setRepeatable(mIsRepeatablCB.isChecked());
            bean.setIsSpecial(true);
            bean.setTotalDrawTimes(1);
            DbUtil.insertAward(this, bean);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStoragePermissionGranted() {

    }
}
