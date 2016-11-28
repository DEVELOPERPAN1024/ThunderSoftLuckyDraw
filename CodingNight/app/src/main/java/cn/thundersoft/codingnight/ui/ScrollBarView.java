package cn.thundersoft.codingnight.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import cn.thundersoft.codingnight.R;

public class ScrollBarView extends RelativeLayout implements View.OnTouchListener {
    private View mSlider;
    private float sliderWidth;
    private float sliderHeight;
    private int slideColor;

    private int heightRaw;
    private double percent = 0;

    private OnProgressChangeListener mListener = null;

    private Drawable mSliderDrawable;

    public ScrollBarView(Context context) {
        this(context, null);
    }

    public ScrollBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScrollBarView);
            sliderWidth = ta.getDimension(R.styleable.ScrollBarView_slider_width, 10);
            sliderHeight = ta.getDimension(R.styleable.ScrollBarView_slider_height, 50);
            slideColor = ta.getColor(R.styleable.ScrollBarView_slider_color, Color.GRAY);
            ta.recycle();
        } else {
            sliderWidth = 10;
            sliderHeight = 50;
            slideColor = Color.GRAY;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mSlider = new View(getContext());
        LayoutParams lp = new LayoutParams((int) sliderWidth, (int) sliderHeight);
        mSlider.setLayoutParams(lp);
        mSliderDrawable = getContext().getDrawable(R.drawable.slider_shape);
        if (mSliderDrawable != null) {
            mSliderDrawable.setTint(slideColor);
            mSlider.setBackground(mSliderDrawable);
        }
        addView(mSlider);

        setOnTouchListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        heightRaw = getHeight() - getPaddingTop() - getPaddingBottom() - mSlider.getHeight();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSliderDrawable.setTint(Color.BLUE);
                mSlider.setBackground(mSliderDrawable);
                // do not break here
            case MotionEvent.ACTION_MOVE:
                double percent;
                float y = event.getY();
                if (y < (mSlider.getHeight() + 0.5) / 2) {
                    percent = 0;
                } else if (y >= getHeight() - getPaddingTop() - getPaddingBottom() - mSlider.getHeight() / 2) {
                    percent = 1;
                } else {
                    percent = (event.getY() - getPaddingTop() - getPaddingBottom()) / (double) heightRaw;
                }
                setProgress(percent, true);
                break;
            case MotionEvent.ACTION_UP:
                mSliderDrawable.setTint(slideColor);
                mSlider.setBackground(mSliderDrawable);
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * @param percent Between zero and one.
     */
    public void setProgress(double percent) {
        setProgress(percent, false); // outside call should not call listener
    }

    private void setProgress(double percent, boolean callListener) {
        this.percent = percent;
        LayoutParams lp = (LayoutParams) mSlider.getLayoutParams();
        lp.topMargin = (int) (heightRaw * percent);
        mSlider.setLayoutParams(lp);
        if (callListener && mListener != null) {
            mListener.onProgressChange(percent);
        }
    }

    public void setOnProgressChangeListener(OnProgressChangeListener listener) {
        mListener = listener;
    }

    public interface OnProgressChangeListener {
        /**
         * @param percent Between zero and one.
         */
        void onProgressChange(double percent);
    }
}
