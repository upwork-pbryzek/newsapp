package android.ali.com.briefme.adapters;

import android.ali.com.briefme.data.RowObj;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Paul on 3/3/17.
 */

public class NewsAdapter extends BaseAdapter {

    private static final String LOG_TAG = NewsAdapter.class.getName();

    private ArrayList<RowObj> data;

    public enum ROW_TYPE {
        NEWS_OVERVIEW,
    }

    public NewsAdapter() {
        super();
        data = new ArrayList<RowObj>();
    }

    public void addRow(RowObj row) {
        data.add(row);
        notifyDataSetChanged();
    }

    public int getCount() {
        return data.size();
    }

    public RowObj getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        RowObj obj = data.get(position);
        return obj.type;
    }

    public void clear() {
        data.clear();
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {
        RowObj obj = data.get(position);
        View v = obj.getView(position, convertView, parent);
        return v;
    }

}
