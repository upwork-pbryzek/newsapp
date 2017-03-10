package android.ali.com.briefme.data;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Paul on 3/3/17.
 */

public abstract class RowObj {
    public int type;

    public RowObj(int type) {
        this.type = type;
    }

    public abstract View getView(int position, View convertView, ViewGroup parent);

}
