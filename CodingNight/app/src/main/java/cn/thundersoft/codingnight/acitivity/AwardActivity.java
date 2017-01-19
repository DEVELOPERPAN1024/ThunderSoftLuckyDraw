package cn.thundersoft.codingnight.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.async.AwardLoader;
import cn.thundersoft.codingnight.models.Award;

public class AwardActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<List<Award>> {

    @Bind(R.id.main_listview)
    ListView mMainListView;
    @Bind(R.id.main_hint_tv)
    TextView mMainHintTV;

    private AwardAdapter mMainAdapter;
    private List<Award> mDataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award_layout);
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
        ButterKnife.bind(this);
        initView();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.set_award);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }


    private void initView() {
        mMainAdapter = new AwardAdapter(mDataList);
        mMainListView.setAdapter(mMainAdapter);
        mMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AwardActivity.this, AwardDetailActivity.class);
                intent.putExtra("bean", mDataList.get(position));
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.add_menu, menu);
        //MenuItemCompat.setActionProvider(item, );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getTitle().equals("普通奖项")) {
            Intent intent = new Intent(this, AddAwardActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getTitle().equals("现金红包")) {
            Intent intent = new Intent(this, AddSpecialActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<List<Award>> onCreateLoader(int id, Bundle args) {
        return new AwardLoader(AwardActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<List<Award>> loader, List<Award> data) {
        mDataList = data;
        if (data.size() > 0) {
            mMainHintTV.setVisibility(View.GONE);
        } else {
            mMainHintTV.setVisibility(View.VISIBLE);
        }
        mMainAdapter.setDataList(data);
        mMainAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Award>> loader) {

    }


    class AwardAdapter extends BaseAdapter {

        private List<Award> mList;

        public AwardAdapter(List<Award> list) {
            this.mList = list;
        }

        public void setDataList(List<Award> list) {
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AwardViewHolder awardViewHolder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_award_layout, parent, false);
                awardViewHolder = new AwardViewHolder(convertView);
                convertView.setTag(awardViewHolder);
            } else {
                awardViewHolder = (AwardViewHolder) convertView.getTag();
            }
            awardViewHolder.mAwardNameTV.setText(mList.get(position).getName());
            awardViewHolder.mAwardDetailTV.setText(mList.get(position).getDetail());
            awardViewHolder.mAwardCountTV.setText("共" + mList.get(position).getCount() + "名");
            return convertView;
        }
    }

    class AwardViewHolder {

        public TextView mAwardNameTV;
        public TextView mAwardCountTV;
        public TextView mAwardDetailTV;

        public AwardViewHolder(View itemView) {
            mAwardNameTV = (TextView) itemView.findViewById(R.id.item_award_name_tv);
            mAwardCountTV = (TextView) itemView.findViewById(R.id.item_award_count_tv);
            mAwardDetailTV = (TextView) itemView.findViewById(R.id.item_award_detail_tv);
        }
    }
}
