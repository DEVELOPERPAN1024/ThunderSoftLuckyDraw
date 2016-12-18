package cn.thundersoft.codingnight.ui;

import android.content.Context;
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
    private boolean mIsSelected = false;
    private ImageView mImage;
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
        mIsSelected = isSelected();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mImage = (ImageView) findViewById(R.id.prize_item_image);
        mName = (TextView) findViewById(R.id.prize_item_name);
    }

    @Override
    public void dispatchSetSelected(boolean selected) {
        super.dispatchSetSelected(selected);
        if (mImage == null) {
            return;
        }
        if (mIsSelected) {
            mImage.setScaleX(1f);
            mImage.setScaleY(1f);
        } else {
            mImage.setScaleX(0.5f);
            mImage.setScaleY(0.5f);
        }
    }

    public void bindPrize(Prize prize) {
        mImage.setImageURI(prize.getImgUri());
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

    public interface OnCheckedChangeListener {
        void onCheckedChanged(boolean isChecked);
    }
}
