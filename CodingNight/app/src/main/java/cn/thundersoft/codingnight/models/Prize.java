package cn.thundersoft.codingnight.models;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import cn.thundersoft.codingnight.db.ProviderContract;

public class Prize implements Parcelable {
    public static final String PRIZE_BUNDLE_KEY = "key_prize";

    private int id;
    private String name;
    private Uri imgUri = null;
    private int count;
    private String detail;
    private int index;
    private int totalTime;
    private int drawnTimes;
    private boolean canRepeat;

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }

    private boolean isSpecial;

    public Prize() {
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

    @Nullable
    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
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

    public boolean isFinish() {
        return totalTime - drawnTimes == 0;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getDrawnTimes() {
        return drawnTimes;
    }

    public void setDrawnTimes(int drawnTimes) {
        this.drawnTimes = drawnTimes;
    }

    public boolean canRepeat() {
        return canRepeat;
    }

    public void setCanRepeat(boolean canRepeat) {
        this.canRepeat = canRepeat;
    }

    public static Prize parseFromCursor(Cursor cursor) {
        Prize result = new Prize();
        result.setId(cursor.getInt(ProviderContract.AwardColumns.ID));
        result.setName(cursor.getString(ProviderContract.AwardColumns.NAME));
        result.setCount(cursor.getInt(ProviderContract.AwardColumns.COUNT));
        result.setDetail(cursor.getString(ProviderContract.AwardColumns.DETAIL));
        String imgUri = cursor.getString(ProviderContract.AwardColumns.PIC_URI);
        if (!TextUtils.isEmpty(imgUri)) {
            result.setImgUri(Uri.parse(imgUri));
        }
        result.setIndex(cursor.getInt(ProviderContract.AwardColumns.ORDER_INDEX));
        result.setTotalTime(cursor.getInt(ProviderContract.AwardColumns.TOTAL_TIMES));
        result.setDrawnTimes(cursor.getInt(ProviderContract.AwardColumns.DRAWN_TIMES));
        result.setCanRepeat(cursor.getInt(ProviderContract.AwardColumns.CAN_REPEAT) == 1);
        result.setSpecial(cursor.getInt(ProviderContract.AwardColumns.IS_SPECIAL) == 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.imgUri, flags);
        dest.writeInt(this.count);
        dest.writeString(this.detail);
        dest.writeInt(this.index);
        dest.writeInt(this.totalTime);
        dest.writeInt(this.drawnTimes);
        dest.writeByte(this.canRepeat ? (byte) 1 : (byte) 0);
    }

    protected Prize(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.imgUri = in.readParcelable(Uri.class.getClassLoader());
        this.count = in.readInt();
        this.detail = in.readString();
        this.index = in.readInt();
        this.totalTime = in.readInt();
        this.drawnTimes = in.readInt();
        this.canRepeat = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Prize> CREATOR = new Parcelable.Creator<Prize>() {
        @Override
        public Prize createFromParcel(Parcel source) {
            return new Prize(source);
        }

        @Override
        public Prize[] newArray(int size) {
            return new Prize[size];
        }
    };
}
