package cn.thundersoft.codingnight.acitivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.models.Person;

public class DataActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "DataActivity";

    private static final int REQUEST_SELECT_FILE = 0;

    private static final int WHAT_UPDATE_DIALOG_MESSAGE = 0;

    private ProgressDialog mProgressDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_UPDATE_DIALOG_MESSAGE:
                    if (mProgressDialog != null) {
                        mProgressDialog.setMessage((String) msg.obj);
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        findViewById(R.id.btn_import).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.setType("text/*");
        i.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(i, REQUEST_SELECT_FILE);
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
                    br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = br.readLine()) != null) {
                        // TODO: 2016/11/25 DB
                        Message message = mHandler.obtainMessage(WHAT_UPDATE_DIALOG_MESSAGE,
                                "Reading...\n" + line);
                        message.sendToTarget();
                        Person p = new Person(line);
                        Log.d(TAG, p.toString());
                    }
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
