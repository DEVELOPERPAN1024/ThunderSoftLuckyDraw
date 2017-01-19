package cn.thundersoft.codingnight.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import cn.thundersoft.codingnight.models.Award;
import cn.thundersoft.codingnight.models.Person;

/**
 * Created by wy on 16-11-25.
 */

public class DbUtil {
    private static Uri URI = Uri.parse(AwardAndEmployeeInfoProvider.URI);

    public static List<Award> getAwards(Context context) {
        List<Award> list = new ArrayList<>();
        Uri u = Uri.withAppendedPath(URI, "award");
        Cursor c = context.getContentResolver().query(u, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                Award award = new Award();
                fillAward(award, c);
                list.add(award);
            }
            c.close();
        }
        return list;

    }

    public static List<Person> getAllPerson(Context context) {
        List<Person> list = new ArrayList<>();
        Uri u = Uri.withAppendedPath(URI, "info");
        Cursor c = context.getContentResolver().query(u, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                Person person = new Person(c.getInt(0), c.getString(1));
                Cursor ac = context.getContentResolver().query(Uri.withAppendedPath(ProviderContract.PERSON_AWARDS_URI, String.valueOf(c.getInt(0))),
                        null, null, null, null, null);
                if (ac != null) {
                    while (ac.moveToNext()) {
                        Award a = new Award();
                        fillAward(a, ac);
                        person.getPrizes().add(a);
                    }
                    ac.close();
                }
                list.add(person);
            }
            c.close();
        }
        return list;
    }

    public static List<Person> getUnawardPersons(Context context) {
        List<Person> list = new ArrayList<>();
        Cursor c = context.getContentResolver().query(ProviderContract.UNAWARD_URI, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                Person person = new Person(c.getString(1));
                person.setId(c.getInt(0));
                list.add(person);
            }
            c.close();
        }
        return list;
    }

    public static void insertWinner(Context context, Integer winnerId, Integer awardId, Integer money) {
        ContentValues cv = new ContentValues();
        cv.put("info_id", winnerId);
        cv.put("award_id", awardId);
        cv.put("money", money);
        context.getContentResolver().insert(ProviderContract.WIN_URI, cv);
    }

    public static void insertWinner(Context context, Integer wid, Integer aId) {
        insertWinner(context, wid, aId, -1);
    }


    public static void fillAward(Award award, Cursor c) {
        award.setId(c.getInt(0));
        award.setName(c.getString(1));
        award.setCount(c.getInt(2));
        award.setDetail(c.getString(3));
        award.setPicUrl(c.getString(4));
        award.setOrderIndex(c.getInt(ProviderContract.AwardColumns.ORDER_INDEX));
        award.setTotalDrawTimes(c.getInt(ProviderContract.AwardColumns.TOTAL_TIMES));
        award.setDrewTimes(c.getInt(ProviderContract.AwardColumns.DRAWN_TIMES));
        award.setRepeatable(c.getInt(ProviderContract.AwardColumns.CAN_REPEAT) == 0);
        award.setIsSpecial(c.getInt(ProviderContract.AwardColumns.IS_SPECIAL) == 0);
    }


    public static void insertAward(Context context, Award bean) {
        ContentValues values = new ContentValues();
        values.put("name", bean.getName());
        values.put("count", bean.getCount());
        values.put("detail", bean.getDetail());
        values.put("picuri", bean.getPicUrl());
        values.put("order_index", bean.getOrderIndex());
        values.put("total_times", bean.getTotalDrawTimes());
        values.put("drawed_times", bean.getDrewTimes());
        values.put("can_repeat", bean.isRepeatable() ? 0 : 1);
        values.put("is_special", bean.isSpecial() ? 0 : 1);
        context.getContentResolver().insert(ProviderContract.AWARD_URI, values);
    }

    public static void updateAward(Context context, Award bean) {
        ContentValues values = new ContentValues();
        values.put("drawed_times", bean.getDrewTimes());
        context.getContentResolver().update(ProviderContract.AWARD_URI, values, "_id=?", new String[]{bean.getId() + ""});
    }

    public static void deleteAward(Context context, Award bean) {
        context.getContentResolver().delete(ProviderContract.AWARD_URI, "_id=?", new String[]{bean.getId() + ""});
    }

    public static String getAwardPeopleList(Context context, Award bean) {
        StringBuilder sb = new StringBuilder();
        Uri u = Uri.withAppendedPath(URI, "wininfo/" + bean.getId());
        Cursor c = context.getContentResolver().query(u, null, null, null, null);
        if (c != null) {
            if (bean.isSpecial()) {
                while (c.moveToNext()) {
                    sb.append(c.getString(1) + " ").append(c.getString(2)).append("\n");
                }
            } else {
                while (c.moveToNext()) {
                    sb.append(c.getString(1)).append("\n");

                }

            }
            c.close();
        }
        return sb.toString();
    }

    public static List<String> getAwardPeopleArrayList(Context context, Award bean) {
        List<String> arrayList = new ArrayList<>();
        Uri u = Uri.withAppendedPath(URI, "wininfo/" + bean.getId());
        Cursor c = context.getContentResolver().query(u, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                arrayList.add(c.getString(1));
            }
            c.close();
        }
        return arrayList;
    }

    public static Award getAwardById(Context context, int id) {
        Uri u = Uri.withAppendedPath(URI, "award/" + id);
        Cursor c = context.getContentResolver().query(u, null, null, null, null);
        Award ad = new Award();
        if (c != null && c.getCount() > 0 && c.moveToFirst())
            fillAward(ad, c);
        return ad;
    }

    public static void cleanWininfo(Context context) {
        context.getContentResolver().delete(ProviderContract.CLEAN_WININFO_URI, null, null);
    }

    public static List<Person>  getMoneyPersonList(Context context) {
        return internalGetMoneyPersonList(context, true);
    }

    public static List<Person> getNoMoneyPersonList(Context context) {
        return internalGetMoneyPersonList(context, false);
    }

    public static List<Person> getNoMoneyAndNoAwardPersonList(Context context) {
        List<Person> list = new ArrayList<>();
        Cursor c = context.getContentResolver().query(ProviderContract.NO_MONEY_AND_NO_AWARD_LIST_URI, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                Person p = new Person(c.getInt(0), c.getString(1));
                p.setMoney(-1);
                list.add(p);
            }
        }
        return list;
    }

    private static List<Person> internalGetMoneyPersonList(Context context, boolean hasMoney) {
        List<Person> list = new ArrayList<>();
        Uri uri = hasMoney ? ProviderContract.MONEY_LIST_URI : ProviderContract.NO_MONEY_LIST_URI;
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                Person p = new Person(c.getInt(0), c.getString(1));
                p.setMoney(hasMoney ? c.getInt(2) : -1);
                list.add(p);
            }
        }
        return list;

    }

    public static String sqliteEscape(String keyWord){
        keyWord = keyWord.replace("/", "//");
        keyWord = keyWord.replace("'", "''");
        keyWord = keyWord.replace("[", "/[");
        keyWord = keyWord.replace("]", "/]");
        keyWord = keyWord.replace("%", "/%");
        keyWord = keyWord.replace("&","/&");
        keyWord = keyWord.replace("_", "/_");
        keyWord = keyWord.replace("(", "/(");
        keyWord = keyWord.replace(")", "/)");
        return keyWord;
    }
}
