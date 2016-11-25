package cn.thundersoft.codingnight;

import android.app.Application;
import android.content.SharedPreferences;

public class App extends Application {
    private static App mInstance;
    private SharedPreferences mSp;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mSp = getSharedPreferences("app", MODE_PRIVATE);
    }

    public static App getInstance() {
        return mInstance;
    }

    public SharedPreferences getSharedPreferences() {
        return mSp;
    }
}
