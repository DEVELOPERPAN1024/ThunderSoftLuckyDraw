package cn.thundersoft.codingnight.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by Auser on 2017/1/3.
 */

public class DisplayUtil {
    public static int screenHeght;
    public static int screenWidth;
    private static float density;

    public static void init(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeght = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
        density = metrics.density;
    }

    public static float dp2px(int dp) {
        return dp * density;
    }
}
