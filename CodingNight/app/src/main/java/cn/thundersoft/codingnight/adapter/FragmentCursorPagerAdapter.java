package cn.thundersoft.codingnight.adapter;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public abstract class FragmentCursorPagerAdapter extends FragmentPagerAdapter {
    private Cursor mCursor;

    public FragmentCursorPagerAdapter(FragmentManager fm, Cursor cursor) {
        super(fm);
        mCursor = cursor;
    }

    @Override
    public Fragment getItem(int position) {
        mCursor.moveToPosition(position);
        return getItem(mCursor);
    }

    /**
     * to create the fragment
     */
    public abstract Fragment getItem(Cursor cursor);

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public void changeCursor(@Nullable Cursor cursor) {
        if (cursor != mCursor) {
            if (mCursor != null) {
                mCursor.close();
            }
            mCursor = cursor;
            notifyDataSetChanged();
        }
    }

    @Nullable
    public Cursor getCursor() {
        return mCursor;
    }
}
