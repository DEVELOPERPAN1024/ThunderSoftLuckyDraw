package cn.thundersoft.codingnight.models;

import java.io.Serializable;

/**
 * Created by pandroid on 11/25/16.
 */

public class Award implements Serializable {

    private int id;
    private String name;
    private int count;
    private String detail;
    private String picUrl;
    private int orderIndex;
    private int totalDrawTimes;
    private int drewTimes;
    private boolean isRepeatable;
    private boolean isSpecial;

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setIsSpecial(boolean special) {
        isSpecial = special;
    }

    public Award(){

    }
    public Award(Prize prize){
        this.id = prize.getId();
        this.name = prize.getName();
        this.detail = prize.getDetail();
        this.picUrl = prize.getImgUri().toString();
        this.orderIndex  = prize.getIndex();
        this.drewTimes = prize.getDrawnTimes();
        this.isRepeatable = prize.canRepeat();
        this.totalDrawTimes = prize.getTotalTime();
        this.count = prize.getCount();
        this.isSpecial = prize.isSpecial();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public int getTotalDrawTimes() {
        return totalDrawTimes;
    }

    public int getDrewTimes() {
        return drewTimes;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public void setTotalDrawTimes(int totalDrawTimes) {
        this.totalDrawTimes = totalDrawTimes;
    }

    public void setDrewTimes(int drewTimes) {
        this.drewTimes = drewTimes;
    }
    public boolean isRepeatable() {
        return isRepeatable;
    }

    public void setRepeatable(boolean repeatable) {
        isRepeatable = repeatable;
    }

    public void increaseDrewTimes() { drewTimes += 1; }
    public void decreaseDrewTimes() { drewTimes -= 1; }
}
