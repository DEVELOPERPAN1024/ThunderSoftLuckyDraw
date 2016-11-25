package cn.thundersoft.codingnight.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.models.Person;

public class PersonView extends FrameLayout implements View.OnClickListener {
    private Person mPerson;

    private TextView mPersonInfo;
    private TextView mPrizeInfo;
    private View mBottomLayout;
    private View mDeleteButton;
    private View mModifyButton;

    public PersonView(Context context) {
        this(context, null, 0);
    }

    public PersonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PersonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setOnClickListener(this);

        mPersonInfo = (TextView) findViewById(R.id.tv_person_info);
        mPrizeInfo = (TextView) findViewById(R.id.tv_win_prize_info);
        mBottomLayout = findViewById(R.id.ll_person_item_bottom);
        mDeleteButton = findViewById(R.id.btn_delete);
        if (mDeleteButton != null) mDeleteButton.setOnClickListener(this);
        mModifyButton = findViewById(R.id.btn_modify);
        if (mModifyButton != null) mModifyButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof PersonView) {
            ((PersonView) v).toggleBottomLayoutVisibility();
            return;
        }
        switch (v.getId()) {
            case R.id.btn_delete:
                break;
            case R.id.btn_modify:
                break;
            default:
        }
    }

    private void toggleBottomLayoutVisibility() {
        boolean isShow = mBottomLayout.getVisibility() != VISIBLE;
        mBottomLayout.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setPersonInfo(String info) {
        mPersonInfo.setText(TextUtils.isEmpty(info) ? getContext().getString(R.string.none) : info);
    }

    public void setWinPrize(String prize) {
        mPrizeInfo.setText(TextUtils.isEmpty(prize) ?
                getContext().getString(R.string.none) : prize);
    }

    public void bindPerson(Person p) {
        mPerson = p;
        setPersonInfo(p.getInfo());
        setWinPrize(p.getPrize());
    }

    public Person getPerson() {
        return mPerson;
    }
}
