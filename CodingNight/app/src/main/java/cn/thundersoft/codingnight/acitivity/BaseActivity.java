package cn.thundersoft.codingnight.acitivity;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import cn.thundersoft.codingnight.util.DisplayUtil;

/**
 * @author greenshadow
 */
public class BaseActivity extends AppCompatActivity {
    private Toast mToast;

    @Override
    protected void onResume() {
        super.onResume();
//        DisplayUtil.hideNavBar(this);
    }

    protected void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    protected void showToast(String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(this, msg, duration);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
