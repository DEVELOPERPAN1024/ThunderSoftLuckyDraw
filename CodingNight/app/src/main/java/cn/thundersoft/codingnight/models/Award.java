package cn.thundersoft.codingnight.models;

import java.io.Serializable;

/**
 * Created by pandroid on 11/25/16.
 */

public class Award implements Serializable {

    private int id;
    private String name;
    private int count;
    private String detial;
    private String picUrl;
    private int orderIndex;
    private int totalDrawTimes;
    private int drawedTimes;
    private boolean isRepeatable;

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

    public String getDetial() {
        return detial;
    }

    public void setDetial(String detial) {
        this.detial = detial;
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

    public int getDrawedTimes() {
        return drawedTimes;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public void setTotalDrawTimes(int totalDrawTimes) {
        this.totalDrawTimes = totalDrawTimes;
    }

    public void setDrawedTimes(int drawedTimes) {
        this.drawedTimes = drawedTimes;
    }
    public boolean isRepeatable() {
        return isRepeatable;
    }

    public void setRepeatable(boolean repeatable) {
        isRepeatable = repeatable;
    }


}
