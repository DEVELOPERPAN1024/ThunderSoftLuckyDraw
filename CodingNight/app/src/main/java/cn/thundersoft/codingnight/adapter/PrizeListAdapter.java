package cn.thundersoft.codingnight.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.models.Prize;
import cn.thundersoft.codingnight.ui.PrizeIndicatorItem;

/**
 * @author GreenShadow
 */

public class PrizeListAdapter extends CursorAdapter {
    private LayoutInflater mInflater;
    private Cursor mCursor;

    public PrizeListAdapter(Context context, Cursor c) {
        super(context, c, true);
        mInflater = LayoutInflater.from(context);
        mCursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.item_prize_indicator, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Prize prize = Prize.parseFromCursor(cursor);
        if (view.findViewById(R.id.prize_item_color_container)
                instanceof PrizeIndicatorItem) {
            ((PrizeIndicatorItem) view.findViewById(R.id.prize_item_color_container))
                    .bindPrize(prize);
        }
    }

    public Prize getPrize(int position) {
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        return Prize.parseFromCursor(mCursor);
    }
}
