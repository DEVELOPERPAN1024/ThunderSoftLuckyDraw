package cn.thundersoft.codingnight.acitivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.adapter.PrizeListAdapter;
import cn.thundersoft.codingnight.db.AwardAndEmployeeInfoProvider;

public class LuckyDrawActivityNew extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final Uri CONTENT_URI = Uri.parse(AwardAndEmployeeInfoProvider.URI);

    @Bind(R.id.prizes)
    ListView prizeList;

    private PrizeListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_draw_new);
        ButterKnife.bind(this);

        prizeList.setOnItemClickListener(this);

        // start task to query
        Uri u = Uri.withAppendedPath(CONTENT_URI, "award");
        new PrizeLoadProgressAsyncTask("").execute(u);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        prizeList.setItemChecked(position, !prizeList.isItemChecked(position));
    }

    private class PrizeLoadProgressAsyncTask extends AsyncTask<Uri/* uri */, Integer/* progress */,
            Cursor/* result */> implements DialogInterface.OnCancelListener {
        private ProgressDialog dialog;

        PrizeLoadProgressAsyncTask(String message) {
            dialog = new ProgressDialog(LuckyDrawActivityNew.this);
            dialog.setMessage(message);
            dialog.setCancelable(false);
            dialog.setOnCancelListener(this);
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Uri... params) {
            if (params.length == 0) {
                return null;
            }
            return getContentResolver().query(params[0], null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor == null) {
                dialog.dismiss();
                Toast.makeText(LuckyDrawActivityNew.this, "null cursor", Toast.LENGTH_LONG).show();
                return;
            }
            if (mAdapter == null) {
                mAdapter = new PrizeListAdapter(LuckyDrawActivityNew.this, cursor);
                prizeList.setAdapter(mAdapter);
            } else {
                mAdapter.changeCursor(cursor);
                mAdapter.notifyDataSetChanged();
            }
            dialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            dialog.setProgress(progress);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            cancel(true);
            dialog.dismiss();
        }
    }
}
