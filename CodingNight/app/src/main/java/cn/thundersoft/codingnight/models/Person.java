package cn.thundersoft.codingnight.models;

import android.util.SparseArray;

import cn.thundersoft.codingnight.App;

public class Person {
    public static final String SP_COLUMN_COUNT = "SP_COLUMN_COUNT";

    private SparseArray<String> data;

    public Person(String line) {
        data = new SparseArray<>();
        String[] s = line.split("[\t ,;]");
        int savedColumn = App.getInstance().getSharedPreferences().getInt(SP_COLUMN_COUNT, -1);
        int columnCount = savedColumn == -1 ? s.length : savedColumn;
        for (int i = 0; i < columnCount; i++) {
            data.put(i, s[i]);
        }
    }

    public void setProp(int index, String value) {
        data.put(index, value);
    }

    public String getProp(int index) {
        return data.get(index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            sb.append(i).append(", ").append(data.get(i)).append("\n");
        }
        return sb.toString();
    }
}
