package cn.thundersoft.codingnight.util;


import android.content.Context;
import android.widget.Toast;

public class CommonUtil {

    public static void showToast(CharSequence msg, Context mContext) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
