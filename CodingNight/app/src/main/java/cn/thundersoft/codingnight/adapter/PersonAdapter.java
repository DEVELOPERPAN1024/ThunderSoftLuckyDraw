package cn.thundersoft.codingnight.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import java.util.HashMap;

import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.models.Person;
import cn.thundersoft.codingnight.ui.PersonView;

public class PersonAdapter extends CursorAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private HashMap<Integer, Person> personCache;

    public PersonAdapter(Context context, Cursor c) {
        super(context, c, true);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        personCache = new HashMap<>();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.item_person, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (!(view instanceof PersonView)) return;
        final Person p = personCache.containsKey(cursor.getInt(0)) ?
                personCache.get(cursor.getInt(0)) : Person.bindCursor(cursor);
        PersonView pv = (PersonView) view;
        pv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PersonView) v).toggleBottomLayoutVisibility();
                personCache.put(p.getId(), p);
            }
        });
        pv.bindPerson(p);
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        personCache.clear();
    }
}
