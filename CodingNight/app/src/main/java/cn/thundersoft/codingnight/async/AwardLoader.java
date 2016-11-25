package cn.thundersoft.codingnight.async;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import cn.thundersoft.codingnight.models.Award;

/**
 * Created by pandroid on 11/25/16.
 */

public class AwardLoader extends AsyncTaskLoader<List<Award>> {

    private Context mContext;
    public AwardLoader(Context context){
        super(context);
        this.mContext = context;
    }

    @Override
    public List<Award> loadInBackground() {

        return null;
    }
}
