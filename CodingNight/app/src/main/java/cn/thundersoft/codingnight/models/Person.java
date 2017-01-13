package cn.thundersoft.codingnight.models;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import cn.thundersoft.codingnight.db.ProviderContract;

import static android.R.id.list;
import static cn.thundersoft.codingnight.db.DbUtil.fillAward;

public class Person {
    private int id;
    private String info;
    private int prize = -1;
    private List<Award> prizes = new ArrayList<>();
    private boolean isShowMenu = false;

    private Person() {
    }

    public Person(String line) {
        setId(0);
        setInfo(line);
    }

    public Person(int id, String info) {
        this.id = id;
        this.info = info;
    }

    public static Person bindCursor(Context context, Cursor c) {
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
        return person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public boolean isShowMenu() {
        return isShowMenu;
    }

    public void setShowMenu(boolean showMenu) {
        isShowMenu = showMenu;
    }

    public List<Award> getPrizes() {
        return prizes;
    }

    @Override
    public String toString() {
        return "id = " + id + ", info = " + info + ", prize = " + prize;
    }
}
