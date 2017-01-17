package cn.thundersoft.codingnight.util;

/**
 * Created by wow on 17/01/2017.
 */

public class StringUtil {

    private static final String CHINESE = "[\u0391-\uFFE5]";

    public static int getStringRealLength(String str) {
        int length = 0;

        for (int i = 0; i < str.length(); ++i) {
            String character = str.substring(i, i + 1);
            if (character.matches(CHINESE))
                length += 2;
            else
                length += 1;
        }
        return length;
    }

}
