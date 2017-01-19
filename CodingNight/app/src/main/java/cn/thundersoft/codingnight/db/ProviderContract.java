package cn.thundersoft.codingnight.db;

import android.net.Uri;

/**
 * Created by wanyu2 on 16-12-18.
 */

public class ProviderContract {
    private static final Uri BASE       = Uri.parse("content://tscodingnight");
    public static final Uri INFO_URI    = Uri.withAppendedPath(BASE, "info");
    public static final Uri AWARD_URI   = Uri.withAppendedPath(BASE, "award");
    public static final Uri WIN_URI     = Uri.withAppendedPath(BASE, "wininfo");
    public static final Uri SEARCH_URI  = Uri.withAppendedPath(BASE, "search");
    public static final Uri UNAWARD_URI = Uri.withAppendedPath(BASE, "unaward");
    public static final Uri WINNER_URI  = Uri.withAppendedPath(BASE, "winner");
    public static final Uri PERSON_AWARDS_URI = Uri.withAppendedPath(BASE, "wininfo/person");
    public static final Uri CLEAN_WININFO_URI = Uri.withAppendedPath(BASE, "wininfo/clean");
    public static final Uri MONEY_LIST_URI = Uri.withAppendedPath(BASE, "wininfo/money_list");
    public static final Uri NO_MONEY_LIST_URI = Uri.withAppendedPath(BASE, "wininfo/no_money_list");
    public static final Uri NO_MONEY_AND_NO_AWARD_LIST_URI = Uri.withAppendedPath(BASE, "wininfo/no_money_no_award_list");

    public class AwardColumns {
        public static final int ID          = 0;  //int
        public static final int NAME        = 1;  //String
        public static final int COUNT       = 2;  //int
        public static final int DETAIL      = 3;  //String
        public static final int PIC_URI     = 4;  //String
        public static final int ORDER_INDEX = 5;  //int
        public static final int TOTAL_TIMES = 6;  //int
        public static final int DRAWN_TIMES = 7;  //int
        public static final int CAN_REPEAT  = 8;  //int
        public static final int IS_SPECIAL  = 9;  //int
    }

    public class InfoColumns {
        public static final int ID          = 0;
        public static final int INFO        = 1;
        public static final int AWARD_ID    = 2;
    }

    public class WinInfoColumns {
        public static final int ID          = 0;
        public static final int INFO_ID     = 1;
        public static final int AWARD_ID    = 2;
    }
}
