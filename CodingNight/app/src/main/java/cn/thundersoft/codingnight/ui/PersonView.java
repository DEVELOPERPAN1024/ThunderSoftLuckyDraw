package cn.thundersoft.codingnight.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.acitivity.DataActivity;
import cn.thundersoft.codingnight.models.Person;

public class PersonView extends FrameLayout implements View.OnClickListener {
    private static final Uri CONTENT_URI = Uri.parse("content://tscodingnight/info");
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
        switch (v.getId()) {
            case R.id.btn_delete:
                getContext().getContentResolver().delete(CONTENT_URI, "_id=?",
                        new String[]{mPerson.getId() + ""});
                ((DataActivity) getContext()).loadData();
                break;
            case R.id.btn_modify:
                View dialogView = LayoutInflater.from(getContext())
                        .inflate(R.layout.layout_edit_person, null);
                final EditText newInfo = (EditText) dialogView.findViewById(R.id.et_input_info);
                new AlertDialog.Builder(getContext())
                        .setView(dialogView)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentValues cv = new ContentValues();
                                cv.put("info", newInfo.getText().toString());
                                getContext().getContentResolver().update(CONTENT_URI, cv, "_id=?",
                                        new String[]{mPerson.getId() + ""});
                                toggleBottomLayoutVisibility();
                                ((DataActivity) getContext()).loadData();
                            }
                        })
                        .create()
                        .show();
                break;
            default:
        }
    }

    public void toggleBottomLayoutVisibility() {
        boolean isShow = mBottomLayout.getVisibility() != VISIBLE;
        if (isShow) {
            Animation in = new AlphaAnimation(0.0f, 1.0f);
            in.setDuration(200);
            mBottomLayout.startAnimation(in);
            mBottomLayout.setVisibility(VISIBLE);
        } else {
            Animation out = new AlphaAnimation(1.0f, 0.0f);
            out.setDuration(200);
            mBottomLayout.startAnimation(out);
            mBottomLayout.setVisibility(GONE);
        }
//        mBottomLayout.setVisibility(isShow ? VISIBLE : GONE);
        mPerson.setShowMenu(isShow);
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
        mBottomLayout.setVisibility(p.isShowMenu() ? VISIBLE : GONE);
    }

    public Person getPerson() {
        return mPerson;
    }
}
