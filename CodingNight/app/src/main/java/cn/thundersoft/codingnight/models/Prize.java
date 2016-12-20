package cn.thundersoft.codingnight.models;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import cn.thundersoft.codingnight.R;

public class Prize implements Parcelable {
    public static final String PRIZE_BUNDLE_KEY = "key_prize";

    private static final Uri DEFAULT_IMG_URI = Uri
            .parse("android.resource://cn.thundersoft.codingnight.models/" + R.raw.default_img);

    private int id;
    private String name;
    private Uri imgUri = Uri.EMPTY;
    private int count;
    private int left;
    private String detail;

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

    public Uri getImgUri() {
        return imgUri == null ? DEFAULT_IMG_URI : imgUri;
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
        return left == 0;
    }

    public static Prize parseFromCursor(Cursor cursor) {
        Prize result = new Prize();
        result.setId(cursor.getInt(0));
        result.setName(cursor.getString(1));
        result.setCount(cursor.getInt(2));
        result.setDetail(cursor.getString(3));
        result.setImgUri(Uri.parse(cursor.getString(4)));
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
        dest.writeInt(this.left);
        dest.writeString(this.detail);
    }

    protected Prize(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.imgUri = in.readParcelable(Uri.class.getClassLoader());
        this.count = in.readInt();
        this.left = in.readInt();
        this.detail = in.readString();
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
