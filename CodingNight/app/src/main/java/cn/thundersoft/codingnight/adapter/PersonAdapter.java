package cn.thundersoft.codingnight.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.models.Person;
import cn.thundersoft.codingnight.ui.PersonView;

public class PersonAdapter extends CursorAdapter {
    private Context mContext;
    private LayoutInflater mInflater;

    public PersonAdapter(Context context, Cursor c) {
        super(context, c, true);
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.item_person, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (!(view instanceof PersonView)) return;
        PersonView pv = (PersonView) view;
        pv.bindPerson(Person.bindCursor(cursor));
    }
}
