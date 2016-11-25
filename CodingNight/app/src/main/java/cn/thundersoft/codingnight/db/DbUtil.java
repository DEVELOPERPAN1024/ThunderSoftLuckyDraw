package cn.thundersoft.codingnight.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import cn.thundersoft.codingnight.models.Award;

/**
 * Created by wy on 16-11-25.
 */

public class DbUtil {
    private static Uri uri = Uri.parse(AwardAndEmployeeInfoProvider.URI);

    public static List<Award> getAwards(Context context) {
        List<Award> list = new ArrayList<>();
        Uri u = Uri.withAppendedPath(uri, "award");
        Cursor c = context.getContentResolver().query(u, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                Award award = new Award();
                fillAward(award, c);
                list.add(award);
            }
        }
        return list;

    }


    private static void fillAward(Award award, Cursor c) {
        award.setId(c.getInt(0));
        award.setName(c.getString(1));
        award.setCount(c.getInt(2));
        award.setDetial(c.getString(3));
        award.setPicUrl(c.getString(4));
    }


    public static void insertAward(Context context, Award bean) {
        ContentValues values = new ContentValues();
        values.put("name", bean.getName());
        values.put("count", bean.getCount());
        values.put("detail", bean.getDetial());
        values.put("picuri", bean.getPicUrl());
        Uri u = Uri.withAppendedPath(uri, "award");
        context.getContentResolver().insert(u, values);
    }

    public static void deleteAward(Context context, Award bean) {
        Uri u = Uri.withAppendedPath(uri, "award");
        context.getContentResolver().delete(u, "_id=?", new String[]{bean.getId() + ""});
    }
}
