package cn.thundersoft.codingnight.ui;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.models.Prize;

/**
 * @author GreenShadow
 */

public class PrizeIndicatorItem extends LinearLayout implements Checkable {
    private ImageView addNew;
    private TextView mName;

    private boolean isChecked;

    public PrizeIndicatorItem(Context context) {
        this(context, null);
    }

    public PrizeIndicatorItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrizeIndicatorItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addNew = (ImageView) findViewById(R.id.prize_add_new);
        mName = (TextView) findViewById(R.id.prize_item_name);
    }

    public void bindPrize(Prize prize) {
        mName.setText(prize.getName());
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        isChecked = !isChecked;
    }

    public String getName() {
        return mName.getText().toString();
    }

    public void showAddNew(boolean show) {
        addNew.setVisibility(show ? VISIBLE : GONE);
        mName.setVisibility(show ? GONE : VISIBLE);
    }
}
