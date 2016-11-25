package cn.thundersoft.codingnight.acitivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.db.DbUtil;
import cn.thundersoft.codingnight.models.Award;

public class AddAwardActivity extends AppCompatActivity {

    @Bind(R.id.award_count_edt)
    EditText mAwardCountEdt;
    @Bind(R.id.award_name_edt)
    EditText mAwardNameEdt;
    @Bind(R.id.award_detail_edt)
    EditText mAwardDetailEdit;
    @Bind(R.id.award_picurl_imgv)
    ImageView mAwardPicurlImageV;

    String mPicurlPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_award_layout);
        ButterKnife.bind(this);
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
        if (Objects.equals(item.getTitle(), "保存")) {
            if (mAwardNameEdt.getText().length() < 1 || mAwardCountEdt.getText().length() < 1
                    || mAwardDetailEdit.getText().length() < 1) {
                Toast.makeText(this, "必要数据为空，请再次检查输入", Toast.LENGTH_SHORT).show();
                return true;
            }
            //save to db
            Award bean = new Award();
            bean.setName(mAwardNameEdt.getText().toString());
            bean.setCount(Integer.valueOf(mAwardCountEdt.getText().toString()));
            bean.setDetial(mAwardDetailEdit.getText().toString());
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
