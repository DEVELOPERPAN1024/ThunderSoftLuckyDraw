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
}
