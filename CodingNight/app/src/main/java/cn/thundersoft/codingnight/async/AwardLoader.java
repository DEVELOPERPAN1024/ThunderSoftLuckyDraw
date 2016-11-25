package cn.thundersoft.codingnight.async;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import cn.thundersoft.codingnight.db.DbUtil;
import cn.thundersoft.codingnight.models.Award;

/**
 * Created by pandroid on 11/25/16.
 */

public class AwardLoader extends AsyncTaskLoader<List<Award>> {

    private Context mContext;

    public AwardLoader(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public List<Award> loadInBackground() {

        return DbUtil.getAwards(mContext);
    }
}
