package cn.thundersoft.codingnight.ui;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import cn.thundersoft.codingnight.R;

public class ClearableEditText extends LinearLayout {
    private EditText mEdit;
    private ImageButton mClear;

    public ClearableEditText(Context context) {
        this(context, null);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mEdit = (EditText) findViewById(R.id.et_search);
        mClear = (ImageButton) findViewById(R.id.bt_clear);
        mClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdit.setText("");
            }
        });
    }

    public void addTextWatcher(TextWatcher watcher) {
        mEdit.addTextChangedListener(watcher);
    }

    public String getText() {
        return mEdit.getText().toString();
    }
}
