package cn.thundersoft.codingnight.acitivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.adapter.PrizeListAdapter;
import cn.thundersoft.codingnight.db.AwardAndEmployeeInfoProvider;
import cn.thundersoft.codingnight.fragment.EnvelopeAnimatorFragment;
import cn.thundersoft.codingnight.models.Prize;
import cn.thundersoft.codingnight.ui.PrizeIndicatorItem;
import cn.thundersoft.codingnight.util.UiUtils;

public class LuckyDrawActivityNew extends AppCompatActivity implements
        AdapterView.OnItemClickListener, View.OnTouchListener {
    private static final Uri CONTENT_URI = Uri.parse(AwardAndEmployeeInfoProvider.URI);

    @Bind(R.id.prizes)
    ListView prizeList;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private PrizeListAdapter mAdapter;
    private Point mTouchPoint;
    private TransitionSet mEnvelopeEnterTransitions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_draw_new);
        ButterKnife.bind(this);
        initTransitions();
        prizeList.setOnItemClickListener(this);
        prizeList.setOnTouchListener(this);
        PrizeIndicatorItem addNewFooter = (PrizeIndicatorItem) LayoutInflater.from(this)
                .inflate(R.layout.item_prize_indicator, null);
        addNewFooter.showAddNew(true);
        addNewFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });
        prizeList.addFooterView(addNewFooter);

//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.content, new PrizeWelcomeFragment(), "welcome")
//                .commit();
        // start task to query
        Uri u = Uri.withAppendedPath(CONTENT_URI, "award");
        new PrizeLoadProgressAsyncTask("").execute(u);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UiUtils.hideNavBar(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        prizeList.setItemChecked(position, !prizeList.isItemChecked(position));
        mDrawerLayout.closeDrawer(Gravity.LEFT);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(Prize.PRIZE_BUNDLE_KEY, mAdapter.getPrize(position));
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag("envelope");
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment == null) {
            fragment = new EnvelopeAnimatorFragment();
            fragment.setEnterTransition(mEnvelopeEnterTransitions);
            fragment.setArguments(bundle);
            ft.add(R.id.content, fragment, "envelope");
        } else {
            fragment.setEnterTransition(mEnvelopeEnterTransitions);
            ft.show(fragment);
        }
        ft.commit();

    }

    private void initTransitions() {
        mEnvelopeEnterTransitions = new TransitionSet();
        mEnvelopeEnterTransitions.addTransition(new Explode());
        mEnvelopeEnterTransitions.addTransition(new Fade().setInterpolator(new LinearInterpolator()));
        mEnvelopeEnterTransitions.setDuration(500);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_UP) {
            if (mTouchPoint == null) {
                mTouchPoint = new Point();
            }
            mTouchPoint.set((int) event.getRawX(), (int) event.getRawY());
        }
        return false;
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
