package cn.thundersoft.codingnight.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.models.Prize;

/**
 * @author GreenShadow
 */

public class PrizeFragment extends Fragment {
    private ImageView prizeImage;
    private Prize mPrize;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lucky_draw, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        prizeImage = (ImageView) view.findViewById(R.id.lucky_draw_award_image);
        Bundle b = getArguments();
        mPrize = b == null ? null : (Prize) b.getParcelable(Prize.PRIZE_BUNDLE_KEY);
        if (mPrize != null) {
            prizeImage.setImageURI(mPrize.getImgUri());
        }
    }
}
