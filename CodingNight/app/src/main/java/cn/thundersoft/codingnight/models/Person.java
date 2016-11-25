package cn.thundersoft.codingnight.models;

import android.database.Cursor;

public class Person {
    private int id;
    private String info;
    private String prize;
    private boolean isShowMenu = false;

    private Person() {
    }

    public Person(String line) {
        setId(0);
        setInfo(line);
        setPrize("");
    }

    public static Person bindCursor(Cursor c) {
        Person p = new Person();
        p.setId(Integer.valueOf(c.getString(0)));
        p.setInfo(c.getString(1));
        p.setPrize(c.getString(2));
        return p;
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

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public boolean isShowMenu() {
        return isShowMenu;
    }

    public void setShowMenu(boolean showMenu) {
        isShowMenu = showMenu;
    }

    @Override
    public String toString() {
        return "id = " + id + ", info = " + info + ", prize = " + prize;
    }
}
