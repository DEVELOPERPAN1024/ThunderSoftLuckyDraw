package cn.thundersoft.codingnight.acitivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.db.DbUtil;
import cn.thundersoft.codingnight.models.Award;
import cn.thundersoft.codingnight.util.RegexCheckUtil;

public class AddAwardActivity extends AppCompatActivity {

    @Bind(R.id.award_count_edt)
    EditText mAwardCountEdt;
    @Bind(R.id.award_name_edt)
    EditText mAwardNameEdt;
    @Bind(R.id.award_detail_edt)
    EditText mAwardDetailEdit;
    @Bind(R.id.award_picurl_imgv)
    ImageView mAwardPicurlImageV;

    @Bind(R.id.award_draw_count)
    EditText mDrawCountEdt;
    @Bind(R.id.award_is_repeatable_cb)
    CheckBox mIsRepeatablCB;

    String mPicurlPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_award_layout);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("添加奖项");
        }
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("保存");
        item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        mAwardPicurlImageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (Objects.equals(item.getTitle(), "保存")) {
            if (mAwardNameEdt.getText().length() < 1 || mAwardCountEdt.getText().length() < 1
                    || mAwardDetailEdit.getText().length() < 1 || mDrawCountEdt.getText().length() < 1) {
                Toast.makeText(this, "必要数据为空，请再次检查输入", Toast.LENGTH_SHORT).show();
                return true;
            }

            if (!RegexCheckUtil.isUserfulNum(mAwardCountEdt.getText().toString()) ||
                    !RegexCheckUtil.isUserfulNum(mDrawCountEdt.getText().toString())) {
                Toast.makeText(this,"输入数据奖品总数或抽奖次数有误，请重新输入",Toast.LENGTH_SHORT).show();
                return true;
            }
            int totalDrawCount = Integer.valueOf(mDrawCountEdt.getText().toString());
            int awardCount = Integer.valueOf(mAwardCountEdt.getText().toString());
            if (awardCount < totalDrawCount) {
                Toast.makeText(this, "抽奖次数大于奖品总数，请重新设置奖项数据", Toast.LENGTH_SHORT).show();
                return true;
            }
            //save to db
            Award bean = new Award();
            bean.setName(mAwardNameEdt.getText().toString());
            bean.setCount(awardCount);
            bean.setDetail(mAwardDetailEdit.getText().toString());
            bean.setTotalDrawTimes(totalDrawCount);
            bean.setRepeatable(mIsRepeatablCB.isChecked());
            if (mPicurlPath != null) {
                bean.setPicUrl(mPicurlPath);
            }
            DbUtil.insertAward(this, bean);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mPicurlPath = cursor.getString(columnIndex);
            Glide.with(this).load(mPicurlPath).centerCrop().into(mAwardPicurlImageV);
            cursor.close();
        }
    }

}
