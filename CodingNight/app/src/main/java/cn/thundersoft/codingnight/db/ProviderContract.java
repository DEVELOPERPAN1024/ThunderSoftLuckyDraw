package cn.thundersoft.codingnight.db;

import android.net.Uri;

/**
 * Created by wanyu2 on 16-12-18.
 */

public class ProviderContract {
    private static final Uri BASE = Uri.parse("content://tscodingnight");
    public static final Uri INFO_URI = Uri.withAppendedPath(BASE, "info");
    public static final Uri AWARD_URI = Uri.withAppendedPath(BASE, "award");
    public static final Uri WIN_URI = Uri.withAppendedPath(BASE, "wininfo");
    public static final Uri SEARCH_URI = Uri.withAppendedPath(BASE, "search");
    public static final Uri UNAWARD_URI = Uri.withAppendedPath(BASE, "unaward");
    public static final Uri WINNER_URI = Uri.withAppendedPath(BASE, "winner");

    public class AwardColumns {
        public static final int ID = 0;  //int
        public static final int NAME = 1;  //String
        public static final int count = 2;  //int
        public static final int detail = 3;  //String
        public static final int picuri = 4;  //String
        public static final int order_index = 5;  //int
        public static final int total_times = 6;  //int
        public static final int drawed_times = 7;  //int
        public static final int can_repeat = 8; //int
    }

    public class InfoColumns {
        public static final int ID = 0;
        public static final int INFO = 1;
        public static final int AWARD_ID = 2;
    }

    public class WinInfoColumns {
        public static final int ID = 0;
        public static final int INFO_ID = 1;
        public static final int AWARD_ID =2;
    }
}