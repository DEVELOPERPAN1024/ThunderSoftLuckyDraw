package cn.thundersoft.codingnight.acitivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.adapter.PersonAdapter;
import cn.thundersoft.codingnight.db.DbUtil;
import cn.thundersoft.codingnight.models.Person;
import cn.thundersoft.codingnight.ui.PersonView;
import cn.thundersoft.codingnight.ui.ScrollBarView;

public class DataActivity extends AppCompatActivity implements View.OnClickListener,
        PersonView.Reloadable, AbsListView.OnScrollListener,
        ScrollBarView.OnProgressChangeListener {
    private final Uri CONTENT_URI = Uri.parse("content://tscodingnight/info");

    private static final int REQUEST_SELECT_FILE = 0;

    private static final int WHAT_UPDATE_IMPORT_DIALOG_MESSAGE = 0;
    private static final int WHAT_IMPORT_COMPLETE = 1;

    private boolean isReload = false;

    private ProgressDialog mProgressDialog;

    @Bind(R.id.list_person)
    ListView mList;
    @Bind(R.id.btn_import)
    View mImportButton;
    @Bind(R.id.scroll_bar)
    ScrollBarView mScrollBar;

    private PersonAdapter mAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_UPDATE_IMPORT_DIALOG_MESSAGE:
                    if (mProgressDialog != null) {
                        mProgressDialog.setMessage((String) msg.obj);
                    }
                    break;
                case WHAT_IMPORT_COMPLETE:
                    reload();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        ButterKnife.bind(this);
        mImportButton.setOnClickListener(this);
        mList.setEmptyView(findViewById(R.id.list_person_empty));
        mList.setOnScrollListener(this);
        mScrollBar.setOnProgressChangeListener(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.set_data);
        }

        reload();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_person_actvity, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem add = menu.findItem(R.id.menu_add_new_person);
        MenuItem search = menu.findItem(R.id.search);
        MenuItem reload = menu.findItem(R.id.menu_reload);
        add.setVisible(!(mAdapter == null || mAdapter.isEmpty()));
        search.setVisible(!(mAdapter == null || mAdapter.isEmpty()));
        reload.setVisible(!(mAdapter == null || mAdapter.isEmpty()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_add_new_person:
                View dialogView = LayoutInflater.from(this)
                        .inflate(R.layout.dialog_edit_person, null);
                final EditText newInfo = (EditText) dialogView.findViewById(R.id.et_input_info);
                new AlertDialog.Builder(this)
                        .setView(dialogView)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentValues cv = new ContentValues();
                                cv.put("info", newInfo.getText().toString());
                                getContentResolver().insert(CONTENT_URI, cv);
                                reload();
                            }
                        })
                        .create()
                        .show();
                break;
            case R.id.search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.menu_reload:
                isReload = true;
                reload();
                mImportButton.performClick();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_import:
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                i.setType("text/*");
                i.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(i, REQUEST_SELECT_FILE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_SELECT_FILE) {
            if (isReload) {
                getContentResolver().delete(CONTENT_URI, null, null);
                isReload = false;
            }
            Uri uri = data.getData();
            readFile(uri);
        }
    }

    private void readFile(final Uri uri) {
        getDialog().show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                BufferedReader br = null;
                try {
                    is = getContentResolver().openInputStream(uri);
                    if (is == null) {
                        Toast.makeText(DataActivity.this, "input stream is null", Toast.LENGTH_LONG).show();
                        return;
                    }
                    ArrayList<Person> data = new ArrayList<>();
                    br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = br.readLine()) != null) {
                        Person p = new Person(line);
                        data.add(p);
                        ContentValues cv = new ContentValues();
                        cv.put("info", line);
                        getContentResolver().insert(DataActivity.this.CONTENT_URI, cv);
                        Message message = mHandler.obtainMessage(WHAT_UPDATE_IMPORT_DIALOG_MESSAGE,
                                "Reading...\n" + line);
                        message.sendToTarget();
                    }
                    Message m = mHandler.obtainMessage(WHAT_IMPORT_COMPLETE);
                    m.sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (br != null) br.close();
                        if (is != null) is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (mProgressDialog != null) mProgressDialog.dismiss();
                    DbUtil.cleanWininfo(DataActivity.this);
                }
            }
        }).start();
    }

    private ProgressDialog getDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });
        }
        return mProgressDialog;
    }

    @Override
    public void reload() {
        Cursor c = getContentResolver().query(CONTENT_URI, null, null, null, null);
        if (mAdapter == null) {
            mAdapter = new PersonAdapter(this, c);
            mList.setAdapter(mAdapter);
        } else {
            mAdapter.changeCursor(c);
            mAdapter.notifyDataSetChanged();
        }
        mScrollBar.setVisibility(mAdapter != null && !mAdapter.isEmpty() ?
                View.VISIBLE : View.GONE);
        invalidateOptionsMenu();
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
