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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.adapter.PrizeListAdapter;
import cn.thundersoft.codingnight.db.AwardAndEmployeeInfoProvider;
import cn.thundersoft.codingnight.fragment.EnvelopeAnimatorFragment;
import cn.thundersoft.codingnight.fragment.PrizeFragment;
import cn.thundersoft.codingnight.models.Prize;
import cn.thundersoft.codingnight.util.UiUtils;

public class LuckyDrawActivityNew extends AppCompatActivity implements
        AdapterView.OnItemClickListener, View.OnTouchListener, View.OnClickListener {
    private static final Uri CONTENT_URI = Uri.parse(AwardAndEmployeeInfoProvider.URI);

    @Bind(R.id.prizes)
    ListView prizeList;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.back_button)
    ImageView mBackButton;
    @Bind(R.id.main)
    FrameLayout mMainFrameLayout;

    private Toast mToast;

    private PrizeListAdapter mAdapter;
    private Point mTouchPoint;
    private TransitionSet mEnvelopeEnterTransitions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_draw_new);
        ButterKnife.bind(this);
        initTransitions();
        initView();
        initBackground();
        // start task to query
    }

    private void initView() {
        prizeList.setOnItemClickListener(this);
        prizeList.setOnTouchListener(this);
        mBackButton.setOnClickListener(this);
    }

    private void initBackground() {
        Glide.with(this).load(R.drawable.main_bg1).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                mMainFrameLayout.setBackground(resource);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri u = Uri.withAppendedPath(CONTENT_URI, "award");
        new PrizeLoadProgressAsyncTask("").execute(u);
        UiUtils.hideNavBar(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        prizeList.setItemChecked(position, !prizeList.isItemChecked(position));
        Prize prize = mAdapter.getPrize(position);
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(Prize.PRIZE_BUNDLE_KEY, prize);
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null && fragments.size() > 0) {
            Iterator<Fragment> iterator = fragments.iterator();
            while (iterator.hasNext()){
                fm.beginTransaction().remove(iterator.next()).commit();
            }
        }
        ((FrameLayout)findViewById(R.id.content)).removeAllViews();
        if(prize.isFinish()){
            Fragment prizeFragment = new PrizeFragment();
            FragmentTransaction ft = fm.beginTransaction();
            Bundle b = new Bundle();
            b.putParcelable(Prize.PRIZE_BUNDLE_KEY, prize);
            prizeFragment.setArguments(b);
            prizeFragment.setEnterTransition(mEnvelopeEnterTransitions);
            prizeFragment.setArguments(bundle);
            ft.replace(R.id.content, prizeFragment, "prize");
            ft.commit();
        }else {
            Fragment fragment = new EnvelopeAnimatorFragment();
            FragmentTransaction ft = fm.beginTransaction();
            //if (fragment == null) {
            fragment.setEnterTransition(mEnvelopeEnterTransitions);
            fragment.setArguments(bundle);
            ft.replace(R.id.content, fragment, "envelope");
            ft.commit();
        }

    }

    private void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_button) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
