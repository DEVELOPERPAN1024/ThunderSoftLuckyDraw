package cn.thundersoft.codingnight.acitivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.adapter.PersonAdapter;
import cn.thundersoft.codingnight.ui.ClearableEditText;
import cn.thundersoft.codingnight.ui.PersonView;
import cn.thundersoft.codingnight.ui.ScrollBarView;

public class SearchActivity extends BaseActivity implements TextWatcher,
        PersonView.Reloadable, AbsListView.OnScrollListener,
        ScrollBarView.OnProgressChangeListener {

    @Bind(R.id.list_result)
    ListView mList;
    @Bind(R.id.tv_search_empty)
    TextView mEmpty;
    @Bind(R.id.scroll_bar)
    ScrollBarView mScrollBar;

    private PersonAdapter mAdapter;

    private ClearableEditText clearEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mList.setEmptyView(mEmpty);
        mList.setOnScrollListener(this);
        mScrollBar.setOnProgressChangeListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            clearEdit = (ClearableEditText) LayoutInflater.from(this)
                    .inflate(R.layout.view_clear_edit, null);
            ActionBar.LayoutParams lp = new ActionBar
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.width = ActionBar.LayoutParams.MATCH_PARENT;
            lp.height = ActionBar.LayoutParams.WRAP_CONTENT;
            clearEdit.setLayoutParams(lp);
            actionBar.setCustomView(clearEdit);
            clearEdit.addTextWatcher(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String searchString = s.toString().trim();
        if (TextUtils.isEmpty(s)) {
            if (mAdapter != null) {
                mAdapter.changeCursor(null);
            }
            return;
        }
        search(searchString);
        mScrollBar.setVisibility(mAdapter != null && !mAdapter.isEmpty() ?
                View.VISIBLE : View.GONE);
    }

    private void search(String searchString) {
        Uri uri = Uri.parse("content://tscodingnight/search/" + searchString);
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        if (mAdapter == null) {
            mAdapter = new PersonAdapter(this, c);
            mList.setAdapter(mAdapter);
        } else {
            mAdapter.changeCursor(c);
        }
    }

    @Override
    public void reload() {
        search(clearEdit.getText());
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        double percent = (double) firstVisibleItem / (totalItemCount - visibleItemCount);
        mScrollBar.setProgress(percent);
    }

    @Override
    public void onProgressChange(double percent) {
        if (mAdapter == null) return;
        mList.setSelection((int) (mAdapter.getCount() * percent + 0.5));
    }
}
