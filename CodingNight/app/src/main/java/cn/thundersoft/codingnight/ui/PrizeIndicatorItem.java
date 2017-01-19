package cn.thundersoft.codingnight.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
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
    private TextView mName;
    private View colorContainer;
    private ImageView mIndicatorIcon;

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
        mName = (TextView) findViewById(R.id.prize_item_name);
        colorContainer = findViewById(R.id.prize_item_color_container);
        mIndicatorIcon = (ImageView) findViewById(R.id.indicator_icon);
    }

    public void bindPrize(Prize prize) {
        mName.setText(prize.getName());
        mName.setTextColor(prize.isFinish() ?
                ContextCompat.getColor(getContext(), R.color.prize_item_disabled) :
                ContextCompat.getColor(getContext(), R.color.prize_item_enabled));
        mIndicatorIcon.setImageResource(prize.isFinish() ?
                R.drawable.envelop_open_icon :
                R.drawable.envelop_icon
        );
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
        mName.setVisibility(show ? GONE : VISIBLE);
    }
}
