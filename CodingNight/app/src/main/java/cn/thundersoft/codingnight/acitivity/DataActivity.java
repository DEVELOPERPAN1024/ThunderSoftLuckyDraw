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
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import cn.thundersoft.codingnight.models.Person;

public class DataActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DataActivity";

    private static final int REQUEST_SELECT_FILE = 0;

    private static final int WHAT_UPDATE_IMPORT_DIALOG_MESSAGE = 0;
    private static final int WHAT_UPDATE_QUERY_DIALOG_MESSAGE = 1;
    private static final int WHAT_IMPORT_COMPLETE = 2;

    private ProgressDialog mProgressDialog;

    @Bind(R.id.list_person)
    ListView mList;
    @Bind(R.id.btn_import)
    Button mImportButton;

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
                case WHAT_UPDATE_QUERY_DIALOG_MESSAGE:
                    break;
                case WHAT_IMPORT_COMPLETE:
                    loadData();
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
        mList.setEmptyView(mImportButton);
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_person_actvity, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem add = menu.findItem(R.id.menu_add);
        add.setVisible(mImportButton.getVisibility() != View.VISIBLE);
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
                        Uri insertUri = Uri.parse("content://tscodingnight/info");
                        ContentValues cv = new ContentValues();
                        cv.put("info", line);
                        getContentResolver().insert(insertUri, cv);
                        // TODO: 2016/11/25 DB
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
                }
            }
        }).start();
    }

    private void loadData() {
        Uri uri = Uri.parse("content://tscodingnight/info");
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        mAdapter = new PersonAdapter(this, c);
        mList.setAdapter(mAdapter);
        invalidateOptionsMenu();
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
}
