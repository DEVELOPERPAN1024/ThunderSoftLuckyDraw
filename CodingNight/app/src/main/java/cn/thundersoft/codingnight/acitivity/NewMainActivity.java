package cn.thundersoft.codingnight.acitivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.thundersoft.codingnight.R;

/**
 * Created by pandroid on 11/25/16.
 */

public class NewMainActivity extends AppCompatActivity {

    @Bind(R.id.main_data_cv)
    CardView mMainDataCV;
    @Bind(R.id.main_award_cv)
    CardView mMainAwardCV;
    @Bind(R.id.main_luckydraw_cv)
    CardView mMainLuckyDrawCV;
    @Bind(R.id.main_item_ll)
    LinearLayout mMainItemLL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        ButterKnife.bind(this);
    }

    


}
