package cn.thundersoft.codingnight.models;

import android.database.Cursor;

public class Person {
    private int id;
    private String info;
    private int prize;
    private String prizeName;
    private boolean isShowMenu = false;

    private Person() {
    }

    public Person(String line) {
        setId(0);
        setInfo(line);
        setPrize(0);
    }

    public static Person bindCursor(Cursor c) {
        Person p = new Person();
        p.setId(Integer.valueOf(c.getString(0)));
        p.setInfo(c.getString(1));
        p.setPrize(c.getInt(2));
        p.setPrizeName(c.getString(3));
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

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getPrizeName() {
        return prizeName;
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
