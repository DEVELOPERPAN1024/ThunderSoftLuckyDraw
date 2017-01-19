package cn.thundersoft.codingnight.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;

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

    public static void hideNavBar(Activity activity) {
        int newUiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();
        newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        activity.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }
}
