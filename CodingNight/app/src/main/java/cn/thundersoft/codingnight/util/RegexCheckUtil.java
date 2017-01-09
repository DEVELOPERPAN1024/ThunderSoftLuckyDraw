package cn.thundersoft.codingnight.util;

import android.text.TextUtils;


public class RegexCheckUtil {

    public static boolean isUserfulNum(String code) {
        String regex = "^[0-9]{1,3}";
        if (TextUtils.isEmpty(code)) {
            return false;
        } else {
            return code.matches(regex);
        }
    }

}
