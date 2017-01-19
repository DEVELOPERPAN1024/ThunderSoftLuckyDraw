package cn.thundersoft.codingnight.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.acitivity.AddAwardActivity;
import cn.thundersoft.codingnight.acitivity.AddSpecialActivity;
import cn.thundersoft.codingnight.acitivity.AwardActivity;

/**
 * Created by pandroid on 2017/1/18.
 */

public class SpecialAwardActionProvider extends ActionProvider {
    private Context context;

    public SpecialAwardActionProvider(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        super.onPrepareSubMenu(subMenu);
        subMenu.clear();
        subMenu.addSubMenu("普通奖项")
                .setIcon(R.drawable.normal_award);
        subMenu.addSubMenu("现金红包")
                .setIcon(R.drawable.red_package);
    }
}
